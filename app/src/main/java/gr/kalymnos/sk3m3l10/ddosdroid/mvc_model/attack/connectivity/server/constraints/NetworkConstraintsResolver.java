package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.constraints;

import android.content.Context;

import java.util.LinkedList;
import java.util.Queue;

public class NetworkConstraintsResolver {
    private Queue<NetworkConstraint> constraints;

    private NetworkConstraintsResolver() {
        constraints = new LinkedList<>();
    }

    public void resolveConstraints(Context context) {
        while (!constraints.isEmpty())
            constraints.poll().resolve(context);
    }

    // TODO: Create a Factory to get instances of NetworkConstraintsResolver
}
