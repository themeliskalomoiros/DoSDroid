package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity.network_constraints;

import android.content.Context;

public abstract class NetworkConstraint {
    protected Context context;
    protected OnResolveConstraintListener onResolveConstraintListener;  //  TODO: Leaks, never removed!

    public NetworkConstraint(Context context) {
        this.context = context;
    }

    public interface OnResolveConstraintListener {
        void onConstraintResolved(Context context, NetworkConstraint constraint);

        void onConstraintResolveFailed(Context context, NetworkConstraint constraint);
    }

    public final void setOnResolveConstraintListener(OnResolveConstraintListener listener) {
        onResolveConstraintListener = listener;
    }

    public abstract void resolve();

    public abstract boolean isResolved();

    //  TODO: Can be declared abstract and all resources should released in implementations
    public void releaseResources() {
        context = null;
    }
}
