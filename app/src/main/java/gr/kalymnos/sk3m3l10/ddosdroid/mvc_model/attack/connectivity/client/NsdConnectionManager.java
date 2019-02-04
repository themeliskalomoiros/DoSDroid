package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.client;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;

class NsdConnectionManager extends ConnectionManager implements NsdManager.DiscoveryListener {
    private static final String TAG = "NsdConnectionManager";

    private NsdManager manager;

    NsdConnectionManager(Context context, Attack attack) {
        super(context, attack);
        manager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    @Override
    void connect() {
        manager.discoverServices(Attacks.getNsdServiceName(attack), NsdManager.PROTOCOL_DNS_SD, this);
    }

    @Override
    void disconnect() {

    }

    @Override
    protected void releaseResources() {
        super.releaseResources();
    }

    @Override
    public void onDiscoveryStarted(String serviceType) {
        Log.d(TAG, "Discovery started");
    }

    @Override
    public void onDiscoveryStopped(String s) {
        Log.d(TAG, "Discovery stopped");
    }

    @Override
    public void onServiceFound(NsdServiceInfo nsdServiceInfo) {
        Log.d(TAG, "Service found");
    }

    @Override
    public void onServiceLost(NsdServiceInfo nsdServiceInfo) {
        Log.d(TAG, "Service lost");
    }

    @Override
    public void onStartDiscoveryFailed(String s, int errorCode) {
        Log.d(TAG, "Start discovery failed");
    }

    @Override
    public void onStopDiscoveryFailed(String s, int errorCode) {
        Log.d(TAG, "Stop discovery failed");
    }
}
