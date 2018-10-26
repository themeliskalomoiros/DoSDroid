package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments;

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

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.FakeAttackRepo;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_lists_screen.AttackListViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_lists_screen.AttackListViewMvcImpl;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.ATTACK_TYPE_KEY;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.CACHED_ATTACKS_KEY;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.TYPE_FETCH_ALL;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.TYPE_FETCH_FOLLOWING;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.TYPE_FETCH_OWNER;
import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack.AttackType.TYPE_NONE;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.bundleIsValidAndContainsKey;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.listHasItems;

public class AttackListFragment extends Fragment implements AttackListViewMvc.OnAttackItemClickListener,
        AttackRepository.OnAttacksFetchListener {

    private static final String TAG = AttackListFragment.class.getSimpleName();
    private AttackListViewMvc viewMvc;
    private AttackRepository attackRepo;
    private List<DDoSAttack> cachedAttacks;

    public static Fragment getInstance(int attacksType) {
        Bundle args = new Bundle();
        args.putInt(ATTACK_TYPE_KEY, attacksType);
        Fragment instance = new AttackListFragment();
        instance.setArguments(args);
        return instance;
    }

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
            startFetchingAttacks();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (listHasItems(cachedAttacks)) {
            outState.putParcelableArrayList(CACHED_ATTACKS_KEY, (ArrayList<? extends Parcelable>) cachedAttacks);
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
        if (bundleIsValidAndContainsKey(savedInstanceState, CACHED_ATTACKS_KEY)) {
            List<DDoSAttack> temp = savedInstanceState.getParcelableArrayList(CACHED_ATTACKS_KEY);
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
    }

    private void startFetchingAttacks() {
        initializeAttackRepo();
        if (getAttacksType(getArguments()) == TYPE_FETCH_ALL) {
            attackRepo.fetchAllAttacks();
        } else if (getAttacksType(getArguments()) == TYPE_FETCH_FOLLOWING) {
            //  TODO: when the fake attack repo is removed replace "bot3" argument with userId variable
//            String userId = DDoSBot.getLocalUserDDoSBot().getId();
            attackRepo.fetchFollowingAttakcs("bot3");
        } else if (getAttacksType(getArguments()) == TYPE_FETCH_OWNER) {
            attackRepo.fetchOwnerAttacks();
        } else {
            throw new UnsupportedOperationException(TAG + ": Type of attacks to fetch not specified");
        }
        viewMvc.showLoadingIndicator();
    }

    private void initializeAttackRepo() {
        attackRepo = new FakeAttackRepo(getActivity());
        attackRepo.registerOnAttacksFetchListener(this);
    }

    private int getAttacksType(Bundle bundle) {
        if (bundleIsValidAndContainsKey(bundle, ATTACK_TYPE_KEY)) {
            return bundle.getInt(ATTACK_TYPE_KEY);
        }
        return TYPE_NONE;
    }

    private void removeFetchingAttacksListener() {
        if (attackRepo != null) {
            attackRepo.unRegisterOnAttacksFetchListener();
            attackRepo = null;
        }
    }

    @Override
    public void onAttackItemClick(int position) {
        Toast.makeText(getContext(), cachedAttacks.get(position).getTargetWebsite(), Toast.LENGTH_SHORT).show();
    }
}
