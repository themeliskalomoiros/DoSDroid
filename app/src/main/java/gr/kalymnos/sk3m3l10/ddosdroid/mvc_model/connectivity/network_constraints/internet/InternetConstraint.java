package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.internet;

import android.content.Context;
import android.net.ConnectivityManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints.NetworkConstraint;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.InternetConnectivity;

public class InternetConstraint extends NetworkConstraint {

    public InternetConstraint(Context context) {
        super(context);
    }

    @Override
    public void resolve() {
        if (isResolved()) {
            onResolveConstraintListener.onConstraintResolved(context, this);
        } else {
            onResolveConstraintListener.onConstraintResolveFailed(context, this);
        }
    }

    @Override
    public boolean isResolved() {
        return isConnected();
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return InternetConnectivity.isConnectionEstablishedOverWifi(cm.getActiveNetworkInfo());
    }
}
