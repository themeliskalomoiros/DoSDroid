package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase.AttackInfoViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase.AttackInfoViewMvcImpl;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.DateFormatter;

import static gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.AttackConstants.Extra.EXTRA_ATTACK;

public class AttackInfoFragment extends Fragment implements AttackInfoViewMvc.OnBeginAttackButtonClickListener {

    private AttackInfoViewMvc viewMvc;
    private Attack attack;

    public interface OnBeginAttackButtonClickListener {
        void onBeginAttackButtonClick();
    }

    private OnBeginAttackButtonClickListener callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attack = getArguments().getParcelable(EXTRA_ATTACK);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initializeViewMvc(inflater, container);
        bindWebsite();
        return viewMvc.getRootView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (OnBeginAttackButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement" + callback.getClass().getCanonicalName());
        }
    }

    @Override
    public void onBeginAttackButtonClick() {
        callback.onBeginAttackButtonClick();
    }

    private void initializeViewMvc(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        viewMvc = new AttackInfoViewMvcImpl(inflater, container);
        viewMvc.setOnBeginAttacButtonClickListener(this);
    }

    private void bindWebsite() {
        viewMvc.bindWebsite(attack.getWebsite());
        viewMvc.bindWebsiteHint(createDateText());
    }

    private String createDateText() {
        Configuration configuration = getContext().getResources().getConfiguration();
        String datePrefix = getString(R.string.attack_date_prefix);
        String date = DateFormatter.getDate(configuration, attack.getTimeMillis());
        return datePrefix + " " + date;
    }

    public static class Builder {
        public static AttackInfoFragment build(Attack attack) {
            AttackInfoFragment instance = new AttackInfoFragment();
            instance.setArguments(createFragmentArgs(attack));
            return instance;
        }

        @NonNull
        private static Bundle createFragmentArgs(Attack attack) {
            Bundle args = new Bundle();
            args.putParcelable(EXTRA_ATTACK, attack);
            return args;
        }
    }
}
