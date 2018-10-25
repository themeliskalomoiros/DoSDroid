package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.FakeAttackRepo;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_list_screen.AttackListViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_list_screen.AttackListViewMvcImpl;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSBot;

import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.bundleIsValidAndContainsKey;

public class AttackListActivity extends AppCompatActivity implements AttackRepository.OnAttacksFetchListener {

    public static final String FETCH_ALL_ATTACKS_KEY = "fetch_all_attacks_key";
    public static final String FETCH_FOLLOWING_ATTACKS_KEY = "fetch_following_attacks_key";
    private static final String TAG = AttackListActivity.class.getSimpleName();

    private AttackListViewMvc viewMvc;
    private AttackRepository attackRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewMvc = new AttackListViewMvcImpl(LayoutInflater.from(this), null);
        attackRepo = new FakeAttackRepo(this);
        setContentView(viewMvc.getRootView());
    }

    @Override
    protected void onStart() {
        super.onStart();
        attackRepo.registerOnAttacksFetchListener(this);
        viewMvc.showLoadingIndicator();
    }

    @Override
    protected void onStop() {
        super.onStop();
        attackRepo.unRegisterOnAttacksFetchListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        attackRepo = null;
    }


    @Override
    public void attacksFetchedSuccess(List<DDoSAttack> attacks) {
        //  This method was called from a worker thread and as we know
        //  the ui must always be updated through the main (UI) thread
        viewMvc.hideLoadingIndicator();
        viewMvc.bindAttacks(attacks);
    }

    @Override
    public void attacksFetchedFail(String msg) {
        //  TODO: Display the error somewhere besides the toast
    }

    private static class AttackTypeValidator {
        private static final String TAG = AttackTypeValidator.class.getSimpleName();
        private static final int TYPE_FETCH_ALL = 101;
        private static final int TYPE_FETCH_FOLLOWING = 102;
        private static final int TYPE_NONE = -1;

        private int getAttacksType(Bundle bundle) {
            if (bundleContainsAllAttacksTypeKeys(bundle)) {
                throw new UnsupportedOperationException(getContainsAllAttacksTypeKeysErrorMessage());
            }

            if (bundleIsValidAndContainsKey(bundle, FETCH_ALL_ATTACKS_KEY)) {
                return bundle.getInt(FETCH_ALL_ATTACKS_KEY);
            } else if (bundleIsValidAndContainsKey(bundle, FETCH_FOLLOWING_ATTACKS_KEY)) {
                return bundle.getInt(FETCH_FOLLOWING_ATTACKS_KEY);
            }

            return TYPE_NONE;
        }

        private boolean bundleContainsAllAttacksTypeKeys(Bundle bundle) {
            return bundleIsValidAndContainsKey(bundle, FETCH_ALL_ATTACKS_KEY)
                    && bundleIsValidAndContainsKey(bundle, FETCH_FOLLOWING_ATTACKS_KEY);
        }

        private String getContainsAllAttacksTypeKeysErrorMessage() {
            return TAG + ": " + AttackListActivity.TAG
                    + "'s bundle must not contain all the attacks type keys. Only one to choose " +
                    "which type of the attack it should display";
        }
    }
}
