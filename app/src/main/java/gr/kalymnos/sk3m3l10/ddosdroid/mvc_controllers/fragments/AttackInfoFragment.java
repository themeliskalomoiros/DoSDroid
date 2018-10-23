package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_screens.AttackInfoViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_screens.AttackInfoViewMvcImpl;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils;

public class AttackInfoFragment extends Fragment implements AttackInfoViewMvc.OnBeginAttackButtonClickListener {

    private AttackInfoViewMvc viewMvc;
    private boolean websiteTextExists;

    private interface OnBeginAttackButtonClickListener {
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

    private void initializeViewMvc(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        viewMvc = new AttackInfoViewMvcImpl(inflater, container);
        viewMvc.setOnBeginAttacButtonClickListener(this);
        websiteTextExists = getArguments() != null && ValidationUtils.bundleContainsKey(getArguments(),
                AttackInfoViewMvc.WEBSITE_KEY);
        if (websiteTextExists) {
            viewMvc.bindWebsite(getArguments().getString(AttackInfoViewMvc.WEBSITE_KEY));
        }
    }

    @Override
    public void onBeginAttackButtonClick() {
        Toast.makeText(getContext(), "Fab clicked", Toast.LENGTH_SHORT).show();
    }
}
