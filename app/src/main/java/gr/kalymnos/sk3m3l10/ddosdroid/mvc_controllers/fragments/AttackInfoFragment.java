package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_phase_screen.AttackInfoViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_phase_screen.AttackInfoViewMvcImpl;

import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_phase_screen.AttackInfoViewMvc.ATTACK_STARTED_KEY;
import static gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_phase_screen.AttackInfoViewMvc.WEBSITE_KEY;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.bundleIsValidAndContainsKey;

public class AttackInfoFragment extends Fragment implements AttackInfoViewMvc.OnBeginAttackButtonClickListener {

    private AttackInfoViewMvc viewMvc;

    public interface OnBeginAttackButtonClickListener {
        void onBeginAttackButtonClick();
    }

    private OnBeginAttackButtonClickListener mCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initializeViewMvc(inflater, container);
        return viewMvc.getRootView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnBeginAttackButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement" + mCallback.getClass().getCanonicalName());
        }
    }

    public static AttackInfoFragment getInstance(String website) {
        Bundle args = new Bundle();
        args.putString(WEBSITE_KEY, website);
        AttackInfoFragment instance = new AttackInfoFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onBeginAttackButtonClick() {
        mCallback.onBeginAttackButtonClick();
    }

    private void initializeViewMvc(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        viewMvc = new AttackInfoViewMvcImpl(inflater, container);
        viewMvc.setOnBeginAttacButtonClickListener(this);
        if (bundleIsValidAndContainsKey(getArguments(), WEBSITE_KEY)) {
            viewMvc.bindWebsite(getArguments().getString(WEBSITE_KEY));
            // A Fab that will display a sword in case of the attack has not started
            // and a header that will display Begin/Stop the Attack
            drawAttackForceArea();
        }
    }

    private void drawAttackForceArea() {
        if (getArguments().getBoolean(ATTACK_STARTED_KEY)) {
            viewMvc.setFabIconToStop();
            viewMvc.bindBeginOrStopAttackHeader(getString(R.string.stop_attack_label));
        } else {
            viewMvc.setFabIconToSwords();
            viewMvc.bindBeginOrStopAttackHeader(getString(R.string.begin_attack_label));
        }
    }
}
