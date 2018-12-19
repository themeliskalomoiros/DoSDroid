package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.creator.AttackCreator;

public abstract class Server {
    private AttackCreator attackCreator;

    public Server(AttackCreator attackCreator) {
        this.attackCreator = attackCreator;
    }
}
