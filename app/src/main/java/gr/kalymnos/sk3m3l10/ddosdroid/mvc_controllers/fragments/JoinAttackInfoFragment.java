package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gr.kalymnos.sk3m3l10.ddosdroid.constants.Special;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_join_attack.JoinAttackInfoViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_join_attack.JoinAttackInfoViewMvcImp;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.NetworkTypeTranslator;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_ATTACK;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.DateFormatter.dateFrom;

public class JoinAttackInfoFragment extends Fragment implements JoinAttackInfoViewMvc.OnJoinAttackClickListener {
    private Attack attack;
    private JoinAttackInfoViewMvc viewMvc;
    private OnJoinAttackButtonClickListener callback;

    public interface OnJoinAttackButtonClickListener {
        void onJoinAttackButtonClicked(Attack attack);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAttack();
    }

    private void initAttack() {
        Bundle bundle = getActivity().getIntent().getBundleExtra(Special.BUNDLE_SAMSUNG_BUG_KEY);
        attack = bundle.getParcelable(EXTRA_ATTACK);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewMvc(inflater, container);
        bindAttackToUi();
        return viewMvc.getRootView();
    }

    private void initViewMvc(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        viewMvc = new JoinAttackInfoViewMvcImp(inflater, container);
        viewMvc.setOnJoinAttackClickListener(this);
    }

    private void bindAttackToUi() {
        viewMvc.bindAttackForce(attack.getBotIds().size());
        viewMvc.bindNetworkConfiguration(NetworkTypeTranslator.translate(attack.getNetworkType()));
        viewMvc.bindWebsite(attack.getWebsite());
        String date = dateFrom(attack.getCreationTimestamp());
        viewMvc.bindCreationDate(date);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnJoinAttackButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement" + callback.getClass().getCanonicalName());
        }
    }

    @Override
    public void onJoinAttackClicked() {
        callback.onJoinAttackButtonClicked(attack);
    }

    public static JoinAttackInfoFragment getInstance(Bundle args) {
        JoinAttackInfoFragment instance = new JoinAttackInfoFragment();
        instance.setArguments(args);
        return instance;
    }
}
