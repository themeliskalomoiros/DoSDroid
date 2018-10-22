package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_screens.AttackCreationViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_screens.AttackCreationViewMvcImpl;

public class AttackCreationFragment extends Fragment implements AttackCreationViewMvc.OnSpinnerItemSelectedListener,
AttackCreationViewMvc.OnAttackCreationButtonClickListener{

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
    public void onSpinnerItemSelected(String item) {
        Toast.makeText(getContext(), "Spinner item selected!", Toast.LENGTH_SHORT).show();
    }

    private void initializeViewMvc(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        viewMvc = new AttackCreationViewMvcImpl(inflater, container);
        viewMvc.setOnAttackCreationButtonClickListener(this);
        viewMvc.setOnSpinnerItemSelectedListener(this);
    }
}
