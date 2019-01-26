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
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.ServersHost;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.status.ServerStatusBroadcaster;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

import static android.net.wifi.p2p.WifiP2pManager.EXTRA_WIFI_STATE;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_DISABLED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_DEVICE_ADDRESS;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_DEVICE_NAME;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_UUID;

public class WifiP2pServer extends Server {
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver wifiDirectReceiver;
    private WifiP2pManager.GroupInfoListener groupInfoListener;

    private AcceptClientThread acceptClientThread;
    private boolean attackUploaded;

    public WifiP2pServer(Context context, Attack attack) {
        super(context, attack);
        initializeFields();
        registerWifiDirectReceiver();
    }

    private void initializeFields() {
        wifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(context, Looper.getMainLooper(), null);
        acceptClientThread = new AcceptClientThread(attack, executor, attackRepo);
        initializeWifiDirectReceiver();
        initializeGroupInfoListener();
    }

    private void initializeWifiDirectReceiver() {
        wifiDirectReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case WIFI_P2P_STATE_CHANGED_ACTION:
                        handleStateChangedAction(context, intent);
                        break;
                    case WIFI_P2P_CONNECTION_CHANGED_ACTION:
                        handleConnectionChangedAction(intent);
                        break;
                    default:
                        throw new IllegalArgumentException(TAG + ": unknown action");
                }
            }

            private void handleStateChangedAction(Context context, Intent intent) {
                if (isStateDisabled(intent))
                    ServersHost.Action.stopServer(context, getId());
                return;
            }

            private boolean isStateDisabled(Intent intent) {
                return intent.getIntExtra(EXTRA_WIFI_STATE, -1) == WIFI_P2P_STATE_DISABLED;
            }

            private void handleConnectionChangedAction(Intent intent) {
                boolean isConnected = getNetworkInfoFrom(intent).isConnected();
                if (isConnected) {
                    wifiP2pManager.requestGroupInfo(channel, groupInfoListener);
                }
                return;
            }

            private NetworkInfo getNetworkInfoFrom(Intent intent) {
                return intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            }
        };
    }

    private void initializeGroupInfoListener() {
        groupInfoListener = group -> {
            if (group.isGroupOwner() && !attackUploaded) {
                uploadAttack(group); // triggers onAttackUploaded()
            }
        };
    }

    private void uploadAttack(WifiP2pGroup group) {
        WifiP2pDevice thisDevice = group.getOwner();
        attack.addSingleHostInfo(EXTRA_UUID, Bots.getLocalUser().getId());
        attack.addSingleHostInfo(EXTRA_DEVICE_NAME, thisDevice.deviceName);
        attack.addSingleHostInfo(EXTRA_DEVICE_ADDRESS, thisDevice.deviceAddress);
        attackRepo.uploadAttack(attack);
    }

    private void registerWifiDirectReceiver() {
        context.registerReceiver(wifiDirectReceiver, getIntentFilter());
    }

    @NonNull
    private IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WIFI_P2P_CONNECTION_CHANGED_ACTION);
        return filter;
    }

    @Override
    public void onAttackUploaded(Attack attack) {
        super.onAttackUploaded(attack);
        attackUploaded = true;
        ServerStatusBroadcaster.broadcastRunning(getId(), LocalBroadcastManager.getInstance(context));
        acceptClientThread.start();
    }

    @Override
    public void start() {
        constraintsResolver.resolveConstraints();
    }

    @Override
    public void stop() {
        acceptClientThread.close();
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
        //  No code needed here. When constraints are resolved the wifiDirectReceiver
        //  will receive its actions and act accordingly.
    }

    @Override
    public void onConstraintResolveFailure() {
        ServerStatusBroadcaster.broadcastError(getId(), LocalBroadcastManager.getInstance(context));
    }

    public WifiP2pManager getWifiP2pManager() {
        return wifiP2pManager;
    }

    public WifiP2pManager.Channel getChannel() {
        return channel;
    }
}
