package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.internet;

import android.content.Context;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.Server;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class InternetServer extends Server {

    public InternetServer(Context context, Attack attack) {
        super(context, attack);
    }

    @Override
    public void start() {
        constraintsResolver.resolveConstraints();
    }

    @Override
    public void onConstraintsResolved() {

    }

    @Override
    public void onConstraintResolveFailure() {

    }
}
