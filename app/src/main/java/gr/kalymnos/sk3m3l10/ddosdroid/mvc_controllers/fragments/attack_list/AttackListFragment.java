package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments.attack_list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.JoinAttackActivity;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack_repo.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack_repo.FakeAttackRepo;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AttackListViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AttackListViewMvcImpl;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.ATTACK_TYPE_KEY;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.TYPE_FETCH_JOINED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.TYPE_FETCH_NOT_JOINED;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.TYPE_NONE;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.Extra.EXTRA_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.bundleIsValidAndContainsKey;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.listHasItems;

public abstract class AttackListFragment extends Fragment implements AttackListViewMvc.OnAttackItemClickListener,
        AttackListViewMvc.OnJoinSwitchCheckedStateListener, AttackListViewMvc.OnActivateSwitchCheckedStateListener,
        AttackRepository.OnAttacksFetchListener {

    private static final String TAG = AttackListFragment.class.getSimpleName();

    private AttackListViewMvc viewMvc;
    protected AttackRepository attackRepo;
    private List<DDoSAttack> cachedAttacks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initializeViewMvc(inflater, container);
        return viewMvc.getRootView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (cachedAttacksExist(savedInstanceState)) {
            viewMvc.bindAttacks(cachedAttacks);
        } else {
            viewMvc.showLoadingIndicator();
            initializeAttackRepo();
            fetchAttacksAccordingToType();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (listHasItems(cachedAttacks)) {
            outState.putParcelableArrayList(EXTRA_ATTACKS, (ArrayList<? extends Parcelable>) cachedAttacks);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeFetchingAttacksListener();
    }

    @Override
    public void attacksFetchedSuccess(List<DDoSAttack> attacks) {
        cachedAttacks = attacks;
        viewMvc.hideLoadingIndicator();
        viewMvc.bindAttacks(cachedAttacks);
    }

    @Override
    public void attacksFetchedFail(String msg) {
        //  TODO: Display the error somewhere besides the toast
    }

    private boolean cachedAttacksExist(Bundle savedInstanceState) {
        if (bundleIsValidAndContainsKey(savedInstanceState, EXTRA_ATTACKS)) {
            List<DDoSAttack> temp = savedInstanceState.getParcelableArrayList(EXTRA_ATTACKS);
            if (listHasItems(temp)) {
                cachedAttacks = temp;
                return true;
            }
        }
        return false;
    }

    private void initializeViewMvc(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        viewMvc = new AttackListViewMvcImpl(inflater, container);
        viewMvc.setOnAttackItemClickListener(this);
        viewMvc.setOnJoinSwitchCheckedStateListener(this);
        viewMvc.setOnActivateSwitchCheckedStateListener(this);
    }

    protected abstract void fetchAttacksAccordingToType();

    private void initializeAttackRepo() {
        attackRepo = new FakeAttackRepo(getActivity());
        attackRepo.registerOnAttacksFetchListener(this);
    }

    protected int getAttacksType(Bundle bundle) {
        if (bundleIsValidAndContainsKey(bundle, ATTACK_TYPE_KEY)) {
            return bundle.getInt(ATTACK_TYPE_KEY);
        }
        return TYPE_NONE;
    }

    private void removeFetchingAttacksListener() {
        if (attackRepo != null) {
            attackRepo.unRegisterOnAttacksFetchListenerAndController();
            attackRepo = null;
        }
    }

    @Override
    public void onAttackItemClick(int position) {
        if (listHasItems(cachedAttacks)) {
            if (getAttacksType(getArguments()) == TYPE_FETCH_NOT_JOINED) {
                startJoinAttackActivity(cachedAttacks.get(position));
            } else if (getAttacksType(getArguments()) == TYPE_FETCH_JOINED) {
                //  TODO: what sould be done when user clicked an attack that he already joined?
            }
        }
    }

    private void startJoinAttackActivity(DDoSAttack attack) {
        Intent intent = new Intent(getContext(), JoinAttackActivity.class);
        intent.putExtra(DDoSAttack.Extra.EXTRA_ATTACK, attack);
        getContext().startActivity(intent);
    }

    @Override
    public void onJoinSwitchCheckedState(int position, boolean isChecked) {
        Toast.makeText(getContext(), "Item at position" + (position) + " switch set to " + isChecked, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivateSwitchCheckedState(int position, boolean isChecked) {
        Toast.makeText(getContext(), "Item at position" + (position) + " switch set to " + isChecked, Toast.LENGTH_SHORT).show();
    }

    /*
     *
     * I broke a switch statement that was exists in AllAttackListsViewMvcImpl.MyPagerAdapter
     * which was checking the text and returning a specific AttackListFragment subclass.
     *
     * Now AllAttackListsViewMvcImpl.MyPagerAdapter.getItemId() just creates an instance of
     * AttackListFragmentBuilder and call build() to return that specific AttackListFragment subclass
     * instance.
     *
     * This technique was used to clean the code. The justification lies in Uncled Bob's book
     * "Clean Code", chapter 3, page 39.
     *
     * */

    public interface AttackListFragmentBuilder {
        AttackListFragment build(String tabTitle, int attackType);
    }

    public static class AttackListFragmentBuilderImpl implements AttackListFragmentBuilder {

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
            args.putInt(ATTACK_TYPE_KEY, attacksType);
            return args;
        }
    }
}
