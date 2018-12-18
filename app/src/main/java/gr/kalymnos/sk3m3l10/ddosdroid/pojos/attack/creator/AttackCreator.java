package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.creator;

public abstract class AttackCreator {
    private String uuid;

    protected AttackCreator() {
        // For firebase
    }

    protected AttackCreator(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
