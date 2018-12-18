package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.creator;

public interface AttackCreatorFactory {
    AttackCreator build(int networkType, String uuid);
}
