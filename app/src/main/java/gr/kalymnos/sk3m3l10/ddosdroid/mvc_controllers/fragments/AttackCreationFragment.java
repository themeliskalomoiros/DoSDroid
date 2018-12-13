package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attacks.attack_repo.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attacks.attack_repo.FirebaseRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase.AttackCreationViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase.AttackCreationViewMvcImpl;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.Bot;

public class AttackCreationFragment extends Fragment implements AttackCreationViewMvc.OnSpinnerItemSelectedListener,
        AttackCreationViewMvc.OnAttackCreationButtonClickListener, AttackCreationViewMvc.OnWebsiteTextChangeListener, AttackRepository.OnAttackUploadedListener {

    private AttackCreationViewMvc viewMvc;
    private AttackRepository attackRepo;
    private OnAttackCreationListener callback;

    public interface OnAttackCreationListener {
        void onAttackCreated(Attack attack);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attackRepo = new FirebaseRepository();
        attackRepo.addOnAttackUploadedListener(this);
    }

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
            callback = (OnAttackCreationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement" + callback.getClass().getCanonicalName());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        attackRepo.removeOnAttacksFetchListener();
    }

    @Override
    public void onAttackCreationButtonClicked(String website) {
        viewMvc.showLoadingIndicator();
        Attack attack = new Attack(website, viewMvc.getNetworkConf(), Bot.getLocalUser());
        attackRepo.uploadAttack(attack);
    }


    @Override
    public void onAttackUploaded(Attack attack) {
        callback.onAttackCreated(attack);
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
