package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.creator.HostInfo;

public abstract class Server {
    private Attack attack;
    private HostInfo hostInfo;

    public Server(Attack attack, HostInfo hostInfo) {
        this.attack = attack;
        this.hostInfo = hostInfo;
    }

    public final String getId(){
        return attack.getPushId();
    }
}
