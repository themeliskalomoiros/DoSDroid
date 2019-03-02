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
import java.util.LinkedHashSet;
import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.JoinAttackActivity;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.host_services.JoinAttackService;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.connectivity.host_services.HostAttackService;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.FirebaseRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AttackListViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AttackListViewMvcImpl;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.bot.Bots;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.ContentTypes.INVALID_CONTENT_TYPE;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.ContentTypes.joinedAttacks;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.ContentTypes.notJoinedAttacks;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.ContentTypes.ownAttacks;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_CONTENT_TYPE;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks.includesBot;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks.isAttackOwnedByBot;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.BundleUtil.containsKey;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.CollectionUtil.hasItems;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.CollectionUtil.itemFromLinkedHashSet;

public abstract class AttackListFragment extends Fragment implements AttackListViewMvc.OnAttackClickListener,
        AttackListViewMvc.OnJoinedAttackDeleteClickListener, AttackListViewMvc.OnOwnerAttackDeleteClickListener,
        AttackRepository.OnRepositoryChangeListener {
    protected static final String TAG = "AttackListFrag";

    protected AttackListViewMvc viewMvc;
    private AttackRepository repo;
    protected LinkedHashSet<Attack> cachedAttacks;
    private int contentType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFieldsExceptViewMvc();
        repo.startListenForChanges();
    }

    private void initFieldsExceptViewMvc() {
        contentType = getContentType();
        cachedAttacks = new LinkedHashSet<>();
        initRepo();
    }

    protected final int getContentType() {
        if (containsKey(getArguments(), EXTRA_CONTENT_TYPE)) {
            return getArguments().getInt(EXTRA_CONTENT_TYPE);
        }
        return INVALID_CONTENT_TYPE;
    }

    private void initRepo() {
        repo = new FirebaseRepository();
        repo.setOnRepositoryChangeListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewMvc(inflater, container);
        return viewMvc.getRootView();
    }

    private void initViewMvc(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        viewMvc = new AttackListViewMvcImpl(inflater, container);
        viewMvc.setOnAttackClickListener(this);
        viewMvc.setOnJoinedAttackDeleteClickListener(this);
        viewMvc.setOnOwnerAttackDeleteClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<Attack> savedAttacks = getAttacksFrom(savedInstanceState);
        if (hasItems(savedAttacks)) {
            cacheAndDisplay(savedAttacks);
        }
    }

    private List<Attack> getAttacksFrom(Bundle savedInstanceState) {
        if (containsKey(savedInstanceState, EXTRA_ATTACKS)) {
            return getAttacksFrom(savedInstanceState);
        }
        return null;
    }

    private void cacheAndDisplay(List<Attack> savedAttacks) {
        cachedAttacks.clear();
        cachedAttacks.addAll(savedAttacks);
        viewMvc.bindAttacks(cachedAttacks);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repo.stopListenForChanges();
    }

    @Override
    public final void onSaveInstanceState(@NonNull Bundle outState) {
        if (hasItems(cachedAttacks)) {
            List<Attack> attacksCopy = new ArrayList<>(cachedAttacks);
            outState.putParcelableArrayList(EXTRA_ATTACKS, (ArrayList<? extends Parcelable>) attacksCopy);
        }
    }

    @Override
    public void onAttackClick(int position) {
        if (notJoinedAttacks(contentType)) {
            Attack attack = itemFromLinkedHashSet(cachedAttacks, position);
            JoinAttackActivity.startInstance(getContext(), attack);
        }
    }

    @Override
    public void onJoinedAttackDeleteClick(int position) {
        //  TODO: Is this duplicate with onOwnerAttackDeleteClick()?
        Attack attack = itemFromLinkedHashSet(cachedAttacks, position);
        JoinAttackService.Action.leave(attack, getContext());
        Snackbar.make(viewMvc.getRootView(), getString(R.string.not_following_attack) + " " + attack.getWebsite(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onOwnerAttackDeleteClick(int position) {
        Attack attack = itemFromLinkedHashSet(cachedAttacks, position);
        HostAttackService.Action.drop(attack.getWebsite(), getContext());
        Snackbar.make(viewMvc.getRootView(), getString(R.string.canceled_attack) + " " + attack.getWebsite(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public final void onAttackDelete(Attack attack) {
        cachedAttacks.remove(attack);
        viewMvc.bindAttacks(cachedAttacks);
    }

    protected final void cacheAttackAccordingToContentType(Attack attack) {
        if (joinedAttacks(contentType)) {
            if (includesBot(attack, Bots.local())) {
                cachedAttacks.add(attack);
            }
        } else if (notJoinedAttacks(contentType)) {
            boolean attackNotJoinedOrOwnedByUser = !includesBot(attack, Bots.local()) && !isAttackOwnedByBot(attack, Bots.local());
            if (attackNotJoinedOrOwnedByUser) {
                cachedAttacks.add(attack);
            }
        } else if (ownAttacks(contentType)) {
            boolean userOwnsThisAttack = isAttackOwnedByBot(attack, Bots.local()) && !includesBot(attack, Bots.local());
            if (userOwnsThisAttack) {
                cachedAttacks.add(attack);
            }
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
