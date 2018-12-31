package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.network_constraints;

import android.content.Context;

public abstract class NetworkConstraint {
    protected OnResolveConstraintListener callback;
    protected Context context;

    public NetworkConstraint(Context context) {
        this.context = context;
    }

    public abstract void resolve();

    public abstract boolean isResolved();

    public abstract void cleanResources();

    public interface OnResolveConstraintListener {
        void onConstraintResolved(Context context, NetworkConstraint constraint);

        void onConstraintResolveFailed(Context context, NetworkConstraint constraint);
    }

    public void setOnResolveConstraintListener(OnResolveConstraintListener listener) {
        callback = listener;
    }
}
