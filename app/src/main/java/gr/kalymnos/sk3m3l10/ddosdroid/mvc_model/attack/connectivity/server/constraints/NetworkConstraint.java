package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.constraints;

import android.content.Context;

public abstract class NetworkConstraint {
    protected Context context;

    public NetworkConstraint(Context context) {
        this.context = context;
    }

    public abstract boolean resolve();

    public static class UnresolvedNetworkConstraintException extends Exception {
        public UnresolvedNetworkConstraintException(String message) {
            super(message);
        }
    }
}
