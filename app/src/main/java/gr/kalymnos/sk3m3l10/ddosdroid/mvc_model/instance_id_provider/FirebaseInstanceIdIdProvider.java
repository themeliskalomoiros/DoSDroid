package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.instance_id_provider;

import com.google.firebase.iid.FirebaseInstanceId;

public class FirebaseInstanceIdIdProvider extends InstanceIdProvider {
    @Override
    public String getInstanceId() {
        return FirebaseInstanceId.getInstance().getId();
    }
}
