package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.JoinAttackActivity;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.connectivity.server.ServersHost;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.AttackRepositoryReporter;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.FirebaseRepositoryReporter;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.service.AttackService;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AttackListViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AttackListViewMvcImpl;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.ContentType.FETCH_ONLY_NOT_JOINED_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.ContentType.INVALID_CONTENT_TYPE;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Constants.Extra.EXTRA_CONTENT_TYPE;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.bundleIsValidAndContainsKey;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.listHasItems;

public abstract class AttackListFragment extends Fragment implements AttackListViewMvc.OnAttackItemClickListener,
        AttackListViewMvc.OnJoinSwitchCheckedStateListener, AttackListViewMvc.OnActivateSwitchCheckedStateListener,
        AttackRepositoryReporter.OnRepositoryChangeListener {
    protected static final String TAG = "AttackListFrag";

    private AttackListViewMvc viewMvc;
    private AttackRepositoryReporter repository;
    private Set<Attack> cachedAttacks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cachedAttacks = new HashSet<>();
        initializeRepository();
    }

    private void initializeRepository() {
        repository = new FirebaseRepositoryReporter();
        repository.addOnRepositoryChangeListener(this);
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
            bindAttacks();
        }
    }

    protected final void bindAttacks() {
        List<Attack> attacksCopy = new ArrayList<>(cachedAttacks);
        viewMvc.bindAttacks(attacksCopy);
    }

    private boolean cachedAttacksExist(Bundle savedInstanceState) {
        if (bundleIsValidAndContainsKey(savedInstanceState, EXTRA_ATTACKS)) {
            List<Attack> temp = savedInstanceState.getParcelableArrayList(EXTRA_ATTACKS);
            if (listHasItems(temp)) {
                cachedAttacks.addAll(temp);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        repository.attach();
    }

    @Override
    public void onStop() {
        super.onStop();
        repository.detach();
    }

    @Override
    public final void onSaveInstanceState(@NonNull Bundle outState) {
        List<Attack> attacksCopy = new ArrayList<>(cachedAttacks);
        if (listHasItems(attacksCopy)) {
            outState.putParcelableArrayList(EXTRA_ATTACKS, (ArrayList<? extends Parcelable>) attacksCopy);
        }
    }

    protected final void deleteFromCacheAttackWith(String attackId) {
        Iterator<Attack> iterator = cachedAttacks.iterator();
        while (iterator.hasNext()) {
            boolean foundAttack = iterator.next().getPushId().equals(attackId);
            if (foundAttack) {
                iterator.remove();
            }
        }
    }

    protected final void cacheAttackAndBind(Attack attack) {
        cachedAttacks.add(attack);
        bindAttacks();
    }

    @Override
    public void onAttackItemClick(int position) {
        List<Attack> attacksCopy = new ArrayList<>(cachedAttacks);
        if (listHasItems(attacksCopy)) {
            if (getContentType(getArguments()) == FETCH_ONLY_NOT_JOINED_ATTACKS) {
                Attack attack = attacksCopy.get(position);
                JoinAttackActivity.startAnInstance(getContext(), attack);
            }
        }
    }

    protected final int getContentType(Bundle bundle) {
        if (bundleIsValidAndContainsKey(bundle, EXTRA_CONTENT_TYPE)) {
            return bundle.getInt(EXTRA_CONTENT_TYPE);
        }
        return INVALID_CONTENT_TYPE;
    }

    @Override
    public void onJoinSwitchCheckedState(int position, boolean isChecked) {
        if (!isChecked) {
            List<Attack> attacksCopy = new ArrayList<>(cachedAttacks);
            Attack attack = attacksCopy.get(position);
            AttackService.Action.stopAttack(attack, getContext());
            Snackbar.make(viewMvc.getRootView(), getString(R.string.not_following_attack) + " " + attack.getWebsite(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivateSwitchCheckedState(int position, boolean isChecked) {
        if (!isChecked) {
            List<Attack> attacksCopy = new ArrayList<>(cachedAttacks);
            Attack attack = attacksCopy.get(position);
            ServersHost.Action.stopServer(getContext(), attack.getPushId());
            Snackbar.make(viewMvc.getRootView(), getString(R.string.canceled_attack) + " " + attack.getWebsite(), Snackbar.LENGTH_SHORT).show();
        }
    }

    /*
     * Baring down a switch statement. Technique used to clean the code. Justification lies in
     * Uncle Bob's "Clean Code", chapter 3, page 39.
     *
     * */

    public interface Builder {
        AttackListFragment build(String tabTitle, int contentType);
    }

    public static class BuilderImp implements Builder {

        @Override
        public AttackListFragment build(String tabTitle, int contentType) {
            AttackListFragment instance = getAttackListFragmentImplFromTabTitle(tabTitle);
            instance.setArguments(createFragmentArgs(contentType));
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
        protected static Bundle createFragmentArgs(int contentType) {
            Bundle args = new Bundle();
            args.putInt(EXTRA_CONTENT_TYPE, contentType);
            return args;
        }
    }
}
