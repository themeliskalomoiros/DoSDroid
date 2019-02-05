package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.JoinAttackActivity;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.ServersHost;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.FirebaseRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AttackListViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AttackListViewMvcImpl;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.AttackType.TYPE_FETCH_JOINED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.AttackType.TYPE_FETCH_NOT_JOINED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.AttackType.TYPE_NONE;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_TYPE;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.bundleIsValidAndContainsKey;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.listHasItems;

public abstract class AttackListFragment extends Fragment implements AttackListViewMvc.OnAttackItemClickListener,
        AttackListViewMvc.OnJoinSwitchCheckedStateListener, AttackListViewMvc.OnActivateSwitchCheckedStateListener,
        AttackRepository.OnAttacksFetchListener {
    private static final String TAG = "AttackListFragment";

    private AttackListViewMvc viewMvc;
    protected AttackRepository attackRepo;
    private List<Attack> cachedAttacks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeAttackRepo();
    }

    private void initializeAttackRepo() {
        attackRepo = new FirebaseRepository();
        attackRepo.addOnAttacksFetchListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initializeViewMvc(inflater, container);
        return viewMvc.getRootView();
    }

    private void initializeViewMvc(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        viewMvc = new AttackListViewMvcImpl(inflater, container);
        viewMvc.setOnAttackItemClickListener(this);
        viewMvc.setOnJoinSwitchCheckedStateListener(this);
        viewMvc.setOnActivateSwitchCheckedStateListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (cachedAttacksExist(savedInstanceState)) {
            viewMvc.bindAttacks(cachedAttacks);
        } else {
            viewMvc.showLoadingIndicator();
            fetchAttacksAccordingToType();
        }
    }

    private boolean cachedAttacksExist(Bundle savedInstanceState) {
        if (bundleIsValidAndContainsKey(savedInstanceState, EXTRA_ATTACKS)) {
            List<Attack> temp = savedInstanceState.getParcelableArrayList(EXTRA_ATTACKS);
            if (listHasItems(temp)) {
                cachedAttacks = temp;
                return true;
            }
        }
        return false;
    }

    protected abstract void fetchAttacksAccordingToType();

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (listHasItems(cachedAttacks)) {
            outState.putParcelableArrayList(EXTRA_ATTACKS, (ArrayList<? extends Parcelable>) cachedAttacks);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeFetchingAttacksListener();
    }

    private void removeFetchingAttacksListener() {
        if (attackRepo != null) {
            attackRepo.removeOnAttacksFetchListener();
            attackRepo = null;
        }
    }

    @Override
    public void attacksFetched(List<Attack> attacks) {
        cachedAttacks = attacks;
        viewMvc.hideLoadingIndicator();
        viewMvc.bindAttacks(cachedAttacks);
    }

    @Override
    public void attacksFetchedFail(String msg) {
        //  TODO: Display the error somewhere besides the toast
    }

    protected int getAttacksType(Bundle bundle) {
        if (bundleIsValidAndContainsKey(bundle, EXTRA_TYPE)) {
            return bundle.getInt(EXTRA_TYPE);
        }
        return TYPE_NONE;
    }

    @Override
    public void onAttackItemClick(int position) {
        if (listHasItems(cachedAttacks)) {
            if (getAttacksType(getArguments()) == TYPE_FETCH_NOT_JOINED) {
                Attack attack = cachedAttacks.get(position);
                startJoinAttackActivity(attack);
            } else if (getAttacksType(getArguments()) == TYPE_FETCH_JOINED) {
                //  TODO: what sould be done when user clicked an attack that he already joined?
            }
        }
    }

    private void startJoinAttackActivity(Attack attack) {
        //  Why this way? see BUNDLE_SAMSUNG_BUG_KEY for details
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.Extra.EXTRA_ATTACK, attack);
        Intent intent = new Intent(getContext(), JoinAttackActivity.class);
        intent.putExtra(Constants.BUNDLE_SAMSUNG_BUG_KEY, bundle);
        getContext().startActivity(intent);
    }

    @Override
    public void onJoinSwitchCheckedState(int position, boolean isChecked) {
        Toast.makeText(getContext(), "Item at position" + (position) + " switch set to " + isChecked, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivateSwitchCheckedState(int position, boolean isChecked) {
        Attack attack = cachedAttacks.get(position);
        if (isChecked) {
            ServersHost.Action.startServer(getContext(), attack);
            Log.d(TAG, "Started a server");
        } else {
            ServersHost.Action.stopServer(getContext(), attack.getPushId());
            Log.d(TAG, "Stopped a server");
        }
    }

    /*
     * Baring down a switch statement. Technique used to clean the code. Justification lies in
     * Uncle Bob's "Clean Code", chapter 3, page 39.
     *
     * */

    public interface Builder {
        AttackListFragment build(String tabTitle, int attackType);
    }

    public static class BuilderImp implements Builder {

        @Override
        public AttackListFragment build(String tabTitle, int attackType) {
            AttackListFragment instance = getAttackListFragmentImplFromTabTitle(tabTitle);
            instance.setArguments(createFragmentArgs(attackType));
            return instance;
        }

        private AttackListFragment getAttackListFragmentImplFromTabTitle(String tabTitle) {
            switch (tabTitle) {
                // Titles were copied from R.arrays.network_technologies_titles
                case "Internet":
                    return new InternetAttackListFragment();
                case "WiFi P2P":
                    return new WiFiP2PAttackListFragment();
                case "NSD":
                    return new NSDAttackListFragment();
                case "Bluetooth":
                    return new BluetoothAttackListFragment();
                default:
                    throw new UnsupportedOperationException(TAG + " " + tabTitle + " is not a valid tab title");
            }
        }

        @NonNull
        protected static Bundle createFragmentArgs(int attacksType) {
            Bundle args = new Bundle();
            args.putInt(EXTRA_TYPE, attacksType);
            return args;
        }
    }
}
