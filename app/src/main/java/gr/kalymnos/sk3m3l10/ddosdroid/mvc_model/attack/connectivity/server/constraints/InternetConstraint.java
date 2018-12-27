package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.constraints;

import android.content.Context;
import android.net.ConnectivityManager;

import gr.kalymnos.sk3m3l10.ddosdroid.utils.InternetConnectivity;

class InternetConstraint extends NetworkConstraint {

    @Override
    public void resolve(Context context) {

    }

    @Override
    public boolean isResolved(Context context) {
        return isConnected(context);
    }

    private boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return InternetConnectivity.isConnectionEstablishedOverWifi(cm.getActiveNetworkInfo());
    }
}
