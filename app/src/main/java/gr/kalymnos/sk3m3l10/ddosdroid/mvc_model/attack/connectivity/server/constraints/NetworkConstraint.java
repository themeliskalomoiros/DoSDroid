package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.constraints;

import android.content.Context;

public abstract class NetworkConstraint {
    protected OnResolveConstraintListener callback;

    public interface OnResolveConstraintListener {
        void onConstraintResolved(NetworkConstraint constraint);

        void onConstraintResolveFailed(NetworkConstraint constraint);
    }

    public void setOnResolveConstraintListener(OnResolveConstraintListener listener) {
        callback = listener;
    }

    public abstract void resolve(Context context);

    public abstract boolean isResolved(Context context);

    public abstract void clearResources(Context context);
}
