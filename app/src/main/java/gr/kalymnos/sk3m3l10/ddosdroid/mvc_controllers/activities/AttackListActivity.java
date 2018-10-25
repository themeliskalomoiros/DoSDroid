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

    private static final String TAG = AttackListActivity.class.getSimpleName();

    public static final String ATTACK_TYPE_KEY = TAG + "attack type key";

    public static final int TYPE_FETCH_ALL = 101;
    public static final int TYPE_FETCH_FOLLOWING = 102;
    private static final int TYPE_NONE = -1;


    private AttackListViewMvc viewMvc;
    private AttackRepository attackRepo;
    private List<DDoSAttack> cachedAttacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewMvc = new AttackListViewMvcImpl(LayoutInflater.from(this), null);
        startFetchingAttacks();
        setContentView(viewMvc.getRootView());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        attackRepo.unRegisterOnAttacksFetchListener();
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

    private void startFetchingAttacks() {
        initializeAttackRepo();

        int attacksType = getAttacksType(getIntent().getExtras());
        if (attacksType == TYPE_FETCH_ALL) {
            attackRepo.fetchAllAttacks();
        } else if (attacksType == TYPE_FETCH_FOLLOWING) {
            //  TODO: when the fake attack repo is removed replace "bot3" argument with userId variable
//            String userId = DDoSBot.getLocalUserDDoSBot().getId();
            attackRepo.fetchFollowingAttakcs("bot3");
        } else {
            throw new UnsupportedOperationException(TAG + ": Type of attacks to fetch not specified");
        }
        viewMvc.showLoadingIndicator();
    }

    private void initializeAttackRepo() {
        attackRepo = new FakeAttackRepo(this);
        attackRepo.registerOnAttacksFetchListener(this);
    }

    private int getAttacksType(Bundle bundle) {
        if (bundleIsValidAndContainsKey(bundle, ATTACK_TYPE_KEY)) {
            return bundle.getInt(ATTACK_TYPE_KEY);
        }
        return TYPE_NONE;
    }
}
