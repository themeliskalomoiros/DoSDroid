package gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.creator;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.instance_id.FirebaseInstanceId;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.instance_id.InstanceIdProvider;

public final class AttackCreators {
    public static AttackCreator getLocalAttackCreatorInstance(int networkType){
        InstanceIdProvider idProvider = new FirebaseInstanceId();
        String uuid = idProvider.getInstanceId();
        return new AttackCreatorFactoryImp().build(networkType,uuid);
    }
}
