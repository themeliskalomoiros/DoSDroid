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
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.attack_creation.DatePicker;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.attack_creation.TimePicker;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase.AttackCreationViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase.AttackCreationViewMvcImpl;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attacks;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.DateFormatter;
import gr.kalymnos.sk3m3l10.ddosdroid.utils.TimeFormatter;

public class AttackCreationFragment extends Fragment implements AttackCreationViewMvc.OnNetworkConfigurationSelectedListener,
        AttackCreationViewMvc.OnAttackCreationClickListener, AttackCreationViewMvc.OnWebsiteTextChangeListener,
        AttackCreationViewMvc.OnPickerClickListener {
    private AttackCreationViewMvc viewMvc;
    private OnAttackCreationListener callback;
    private TimePicker.Time time;
    private DatePicker.Date date;

    public interface OnAttackCreationListener {
        void onAttackCreated(Attack attack);

        void onDatePickerClicked();

        void onTimePickerClicked();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewMvc(inflater, container);
        return viewMvc.getRootView();
    }

    private void initViewMvc(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        viewMvc = new AttackCreationViewMvcImpl(inflater, container);
        viewMvc.setOnAttackCreationClickListener(this);
        viewMvc.setOnNetworkConfigurationSelectedListener(this);
        viewMvc.setOnWebsiteTextChangeListener(this);
        viewMvc.setOnPickerClickListener(this);
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

    public void setTime(TimePicker.Time time) {
        this.time = time;
        viewMvc.bindTimeText(TimeFormatter.from(time));
    }

    public void setDate(DatePicker.Date date) {
        this.date = date;
        viewMvc.bindDateText(DateFormatter.from(date));
    }

    @Override
    public void onAttackCreateClicked(String website) {
        if (userProvidedAllData(website)){
            Attack attack = createAttack(website);
            callback.onAttackCreated(attack);
        }
    }

    private boolean userProvidedAllData(String website) {
        if (!URLUtil.isValidUrl(website)) {
            Snackbar.make(viewMvc.getRootView(), R.string.enter_valid_url_label, Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (date == null) {
            Snackbar.make(viewMvc.getRootView(), R.string.enter_valid_url_label, Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (time == null) {
            Snackbar.make(viewMvc.getRootView(), R.string.enter_valid_url_label, Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @NonNull
    private Attack createAttack(String website) {
        Attack attack = new Attack(website, viewMvc.getNetworkConf());
        attack.setPushId(Attacks.createPushId());
        return attack;
    }

    @Override
    public void onNetworkSelected(String hint) {
        viewMvc.bindNetworkConfig(hint);
    }

    @Override
    public void websiteTextChanged(String text) {
        if (TextUtils.isEmpty(text)) {
            viewMvc.bindWebsiteCreationTime(getString(R.string.set_the_target_of_the_attack_label));
        } else {
            viewMvc.bindWebsiteCreationTime(getString(R.string.target_set_to_label) + " " + text + ".");
        }
    }

    @Override
    public void onTimePickerClick() {
        if (callback != null)
            callback.onTimePickerClicked();
    }

    @Override
    public void onDatePickerClick() {
        if (callback != null)
            callback.onDatePickerClicked();
    }
}
