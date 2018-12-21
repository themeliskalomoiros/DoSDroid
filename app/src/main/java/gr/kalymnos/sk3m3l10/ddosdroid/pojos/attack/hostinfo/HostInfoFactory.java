package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.hostinfo;

public interface HostInfoFactory {
    HostInfo build(int networkType, String uuid);
}
