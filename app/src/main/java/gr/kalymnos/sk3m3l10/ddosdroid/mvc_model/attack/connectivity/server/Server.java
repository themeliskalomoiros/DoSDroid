package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server;

import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.creator.HostInfo;

public abstract class Server {
    private HostInfo hostInfo;

    public Server(HostInfo hostInfo) {
        this.hostInfo = hostInfo;
    }
}
