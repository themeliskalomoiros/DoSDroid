package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.instance_id;

public class FirebaseInstanceId extends InstanceIdProvider {
    @Override
    public String getInstanceId() {
        return com.google.firebase.iid.FirebaseInstanceId.getInstance().getId();
    }
}
