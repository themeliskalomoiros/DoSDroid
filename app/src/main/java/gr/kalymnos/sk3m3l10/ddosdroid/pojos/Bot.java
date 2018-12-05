package gr.kalymnos.sk3m3l10.ddosdroid.pojos;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.instance_id_provider.FakeInstanceIdProvider;

public class Bot {
    private String id;

    public Bot(String instanceId) {
        this.id = instanceId;
    }

    public String getId() {
        return id;
    }

    public static Bot getLocalUserDDoSBot() {
        //  TODO: Replace with real InstanceIdProvider
        return new Bot(new FakeInstanceIdProvider().getInstanceId());
    }
}
