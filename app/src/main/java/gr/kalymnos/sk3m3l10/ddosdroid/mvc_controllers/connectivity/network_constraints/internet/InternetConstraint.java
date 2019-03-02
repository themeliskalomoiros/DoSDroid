package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.network_constraints.internet;

import android.content.Context;
import android.net.ConnectivityManager;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.network_constraints.NetworkConstraint;

import static gr.kalymnos.sk3m3l10.ddosdroid.utils.connectivity.InternetConnectivity.hasInternetConnection;

public class InternetConstraint extends NetworkConstraint {

    private ConnectivityManager connectivityManager;

    public InternetConstraint(Context context) {
        super(context);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        return hasInternetConnection(connectivityManager);
    }
}
