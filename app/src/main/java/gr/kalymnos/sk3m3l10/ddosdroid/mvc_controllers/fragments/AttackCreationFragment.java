package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_screens.AttackCreationViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_screens.AttackCreationViewMvcImpl;

public class AttackCreationFragment extends Fragment implements AttackCreationViewMvc.OnSpinnerItemSelectedListener,
        AttackCreationViewMvc.OnAttackCreationButtonClickListener, AttackCreationViewMvc.OnWebsiteTextChangeListener {

    private AttackCreationViewMvc viewMvc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initializeViewMvc(inflater, container);
        return viewMvc.getRootView();
    }

    @Override
    public void onAttackCreationButtonClicked() {
        Toast.makeText(getContext(), "Fab Clicked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSpinnerItemSelected(String hint) {
        viewMvc.setNetworkConfigHint(hint);
    }

    private void initializeViewMvc(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        viewMvc = new AttackCreationViewMvcImpl(inflater, container);
        viewMvc.setOnAttackCreationButtonClickListener(this);
        viewMvc.setOnSpinnerItemSelectedListener(this);
        viewMvc.setOnWebsiteTextChangeListener(this);
    }

    @Override
    public void websiteTextChanged(String text) {
        if (TextUtils.isEmpty(text)) {
            viewMvc.setWebsiteHint(getString(R.string.set_the_target_of_the_attack_label));
        } else {
            viewMvc.setWebsiteHint(getString(R.string.target_set_to_label) + " " + text + ".");
        }
    }
}
