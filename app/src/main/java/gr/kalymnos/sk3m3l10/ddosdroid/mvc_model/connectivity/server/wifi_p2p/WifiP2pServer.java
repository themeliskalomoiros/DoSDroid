package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.wifi_p2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.server.status.ServerStatusBroadcaster;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

import static android.net.wifi.p2p.WifiP2pManager.EXTRA_WIFI_STATE;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_DISABLED;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_ATTACK_HOST_UUID;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_DEVICE_NAME;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_LOCAL_PORT;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_MAC_ADDRESS;

public class WifiP2pServer extends Server {
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private WifiP2pManager.GroupInfoListener groupInfoListener;

    private BroadcastReceiver wifiDirectReceiver, portReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private boolean receiversRegistered = false;

    private AcceptClientThread acceptClientThread;

    public WifiP2pServer(Context context, Attack attack) {
        super(context, attack);
        initFields();
    }

    private void initFields() {
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        wifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(context, Looper.getMainLooper(), null);
        acceptClientThread = new AcceptClientThread(executor, localBroadcastManager);
        initWifiDirectReceiver();
        initPortReceiver();
        initGroupInfoListener();
    }

    private void initWifiDirectReceiver() {
        wifiDirectReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (WIFI_P2P_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                    stopIfStateDisabled(context, intent);
                } else if (WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(intent.getAction())) {
                    requestGroupInfoIfConnected(intent);
                }
            }

            private void stopIfStateDisabled(Context context, Intent intent) {
                if (isStateDisabled(intent)) {
                    stop();
                    ServerStatusBroadcaster.broadcastStopped(getAttackedWebsite(), localBroadcastManager);
                }
            }

            private boolean isStateDisabled(Intent intent) {
                return intent.getIntExtra(EXTRA_WIFI_STATE, -1) == WIFI_P2P_STATE_DISABLED;
            }

            private void requestGroupInfoIfConnected(Intent intent) {
                if (isConnected(intent)) {
                    wifiP2pManager.requestGroupInfo(channel, groupInfoListener);
                }
            }

            private boolean isConnected(Intent intent) {
                NetworkInfo info = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                return info.isConnected();
            }
        };
    }

    private void initPortReceiver() {
        portReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isPortAction = intent.getAction().equals(AcceptClientThread.ACTION_LOCAL_PORT_OBTAINED);
                if (isPortAction) {
                    addPortToAttack(intent);
                    repo.upload(attack);
                    ServerStatusBroadcaster.broadcastRunning(getAttackedWebsite(), localBroadcastManager);
                }
            }

            private void addPortToAttack(Intent intent) {
                int port = intent.getIntExtra(EXTRA_LOCAL_PORT, -1);
                attack.addSingleHostInfo(EXTRA_LOCAL_PORT, String.valueOf(port));
            }
        };
    }

    private void initGroupInfoListener() {
        groupInfoListener = group -> {
            if (group.isGroupOwner() && !acceptClientThread.isAlive()) {
                setHostInfoFrom(group);
                acceptClientThread.start();
            }
        };
    }

    private void setHostInfoFrom(WifiP2pGroup group) {
        WifiP2pDevice thisDevice = group.getOwner();
        attack.addSingleHostInfo(EXTRA_ATTACK_HOST_UUID, Bots.local().getId());
        attack.addSingleHostInfo(EXTRA_DEVICE_NAME, thisDevice.deviceName);
        attack.addSingleHostInfo(EXTRA_MAC_ADDRESS, thisDevice.deviceAddress);
    }

    @Override
    public void start() {
        constraintsResolver.resolveConstraints();
    }

    @Override
    public void stop() {
        acceptClientThread.close();
        if (receiversRegistered) {
            localBroadcastManager.unregisterReceiver(portReceiver);
            context.unregisterReceiver(wifiDirectReceiver);
        }
        removeGroup();
        super.stop();
    }

    private void removeGroup() {
        wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Wifi peer to peer group removed.");
                ServerStatusBroadcaster.broadcastStopped(getAttackedWebsite(), localBroadcastManager);
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "Failed to remove wifi peer to peer group.");
            }
        });
    }

    @Override
    public void onConstraintsResolved() {
        registerReceivers();
    }

    private void registerReceivers() {
        localBroadcastManager.registerReceiver(portReceiver, new IntentFilter(AcceptClientThread.ACTION_LOCAL_PORT_OBTAINED));
        context.registerReceiver(wifiDirectReceiver, getIntentFilter());
        receiversRegistered = true;
    }

    @NonNull
    private IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WIFI_P2P_CONNECTION_CHANGED_ACTION);
        return filter;
    }

    @Override
    public void onConstraintResolveFailure() {
        ServerStatusBroadcaster.broadcastError(getAttackedWebsite(), localBroadcastManager);
    }

    public WifiP2pManager getWifiP2pManager() {
        return wifiP2pManager;
    }

    public WifiP2pManager.Channel getChannel() {
        return channel;
    }
}
