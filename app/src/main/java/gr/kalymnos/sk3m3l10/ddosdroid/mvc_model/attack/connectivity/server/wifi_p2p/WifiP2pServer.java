package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.wifi_p2p;

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

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.ServerHost;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.ServerStatusBroadcaster;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

import static android.net.wifi.p2p.WifiP2pManager.EXTRA_WIFI_STATE;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_DISABLED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_ATTACK_HOST_UUID;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_DEVICE_NAME;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_MAC_ADDRESS;

public class WifiP2pServer extends Server {
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private WifiP2pManager.GroupInfoListener groupInfoListener;

    private BroadcastReceiver wifiDirectReceiver;
    private boolean isReceiverRegistered = false;

    private AcceptClientThread acceptClientThread;

    public WifiP2pServer(Context context, Attack attack) {
        super(context, attack);
        initializeFields();
    }

    private void initializeFields() {
        wifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(context, Looper.getMainLooper(), null);
        acceptClientThread = new AcceptClientThread(attack, executor, repository);
        initializeWifiDirectReceiver();
        initializeGroupInfoListener();
    }

    private void initializeWifiDirectReceiver() {
        wifiDirectReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case WIFI_P2P_STATE_CHANGED_ACTION:
                        if (isStateDisabled(intent)) {
                            stop();
                            ServerStatusBroadcaster.broadcastStopped(getAttackedWebsite(), LocalBroadcastManager.getInstance(context));
                        }
                        break;
                    case WIFI_P2P_CONNECTION_CHANGED_ACTION:
                        if (isConnected(intent)) {
                            wifiP2pManager.requestGroupInfo(channel, groupInfoListener);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException(TAG + ": unknown action");
                }
            }

            private boolean isStateDisabled(Intent intent) {
                return intent.getIntExtra(EXTRA_WIFI_STATE, -1) == WIFI_P2P_STATE_DISABLED;
            }

            private boolean isConnected(Intent intent) {
                NetworkInfo info = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                return info.isConnected();
            }
        };
    }

    private void initializeGroupInfoListener() {
        groupInfoListener = group -> {
            if (group.isGroupOwner() && !acceptClientThread.isAlive()) {
                acceptClientThread.start();
                ServerStatusBroadcaster.broadcastRunning(getAttackedWebsite(), LocalBroadcastManager.getInstance(context));
                uploadAttack(group);
            }
        };
    }

    private void uploadAttack(WifiP2pGroup group) {
        setHostInfoToAttack(group);
        repository.upload(attack);
    }

    private void setHostInfoToAttack(WifiP2pGroup group) {
        WifiP2pDevice thisDevice = group.getOwner();
        attack.addSingleHostInfo(EXTRA_ATTACK_HOST_UUID, Bots.getLocalUser().getId());
        attack.addSingleHostInfo(EXTRA_DEVICE_NAME, thisDevice.deviceName);
        attack.addSingleHostInfo(EXTRA_MAC_ADDRESS, thisDevice.deviceAddress);
    }

    @Override
    public void start() {
        super.start();
        constraintsResolver.resolveConstraints();
    }

    @Override
    public void stop() {
        acceptClientThread.close();
        if (isReceiverRegistered)
            context.unregisterReceiver(wifiDirectReceiver);
        removeGroup();
        super.stop();
    }

    private void removeGroup() {
        wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Wifi peer to peer group removed.");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "Failed to remove wifi peer to peer group.");
            }
        });
    }

    @Override
    public void onConstraintsResolved() {
        registerWifiDirectReceiver();
    }

    private void registerWifiDirectReceiver() {
        context.registerReceiver(wifiDirectReceiver, getIntentFilter());
        isReceiverRegistered = true;
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
        ServerStatusBroadcaster.broadcastError(getAttackedWebsite(), LocalBroadcastManager.getInstance(context));
    }

    public WifiP2pManager getWifiP2pManager() {
        return wifiP2pManager;
    }

    public WifiP2pManager.Channel getChannel() {
        return channel;
    }
}
