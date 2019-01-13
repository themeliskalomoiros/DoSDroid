package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.repository.FirebaseRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase.AttackCreationViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase.AttackCreationViewMvcImpl;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.hostinfo.HostInfo;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.hostinfo.HostInfoHelper;

public class AttackCreationFragment extends Fragment implements AttackCreationViewMvc.OnSpinnerItemSelectedListener,
        AttackCreationViewMvc.OnAttackCreationButtonClickListener, AttackCreationViewMvc.OnWebsiteTextChangeListener{

    private AttackCreationViewMvc viewMvc;
    private OnAttackCreationListener callback;

    public interface OnAttackCreationListener {
        void onAttackCreated(Attack attack);
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
    public void onAttackCreationButtonClicked(String website) {
        if (URLUtil.isValidUrl(website)) {
            HostInfo creator = HostInfoHelper.getLocalHostInfo(viewMvc.getNetworkConf());
            Attack attack = new Attack(website, viewMvc.getNetworkConf(), creator);
            callback.onAttackCreated(attack);
        } else {
            Snackbar.make(viewMvc.getRootView(), R.string.enter_valid_url_label, Snackbar.LENGTH_SHORT).show();
        }
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
