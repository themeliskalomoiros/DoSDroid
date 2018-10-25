package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.FakeAttackRepo;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_list_screen.AttackListViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_list_screen.AttackListViewMvcImpl;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;

import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.bundleIsValidAndContainsKey;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.listHasItems;

public class AttackListActivity extends AppCompatActivity implements AttackRepository.OnAttacksFetchListener {

    private static final String TAG = AttackListActivity.class.getSimpleName();

    public static final String ATTACK_TYPE_KEY = TAG + "attack type key";
    public static final String CACHED_ATTACKS_KEY = TAG + "caching attacks key";

    public static final int TYPE_FETCH_ALL = 101;
    public static final int TYPE_FETCH_FOLLOWING = 102;
    private static final int TYPE_NONE = -1;


    private AttackListViewMvc viewMvc;
    private AttackRepository attackRepo;
    private List<DDoSAttack> cachedAttacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewMvc();
        if (cachedAttacksExist(savedInstanceState)) {
            drawListAndSubtitle(cachedAttacks);
        } else {
            startFetchingAttacks();
        }
        setContentView(viewMvc.getRootView());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (listHasItems(cachedAttacks)) {
            outState.putParcelableArrayList(CACHED_ATTACKS_KEY, (ArrayList<? extends Parcelable>) cachedAttacks);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearFetchingAttacksListener();
    }


    @Override
    public void attacksFetchedSuccess(List<DDoSAttack> attacks) {
        cachedAttacks = attacks;
        viewMvc.hideLoadingIndicator();
        drawListAndSubtitle(attacks);
    }

    @Override
    public void attacksFetchedFail(String msg) {
        //  TODO: Display the error somewhere besides the toast
    }

    private boolean cachedAttacksExist(Bundle savedInstanceState) {
        if (bundleIsValidAndContainsKey(savedInstanceState, CACHED_ATTACKS_KEY)) {
            List<DDoSAttack> temp = savedInstanceState.getParcelableArrayList(CACHED_ATTACKS_KEY);
            if (listHasItems(temp)) {
                cachedAttacks = temp;
                return true;
            }
        }
        return false;
    }

    private void initializeViewMvc() {
        viewMvc = new AttackListViewMvcImpl(LayoutInflater.from(this), null);
        setSupportActionBar(viewMvc.getToolbar());
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

    private void drawListAndSubtitle(List<DDoSAttack> cachedAttacks) {
        viewMvc.bindToolbarSubtitle(getString(R.string.attack_list_toolbar_subtitle_prefix) + " "
                + cachedAttacks.size() + " " + getString(R.string.items_label) + ".");
        viewMvc.bindAttacks(cachedAttacks);
    }

    private int getAttacksType(Bundle bundle) {
        if (bundleIsValidAndContainsKey(bundle, ATTACK_TYPE_KEY)) {
            return bundle.getInt(ATTACK_TYPE_KEY);
        }
        return TYPE_NONE;
    }

    private void clearFetchingAttacksListener() {
        if (attackRepo != null) {
            attackRepo.unRegisterOnAttacksFetchListener();
            attackRepo = null;
        }
    }
}
