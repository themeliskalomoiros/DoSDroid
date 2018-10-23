package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_screens;

import android.text.Editable;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvc;

public interface AttackCreationViewMvc extends ViewMvc {

    interface OnAttackCreationButtonClickListener {
        void onAttackCreationButtonClicked();
    }

    interface OnSpinnerItemSelectedListener {
        void onSpinnerItemSelected(String hint);
    }

    interface OnWebsiteTextChangeListener {
        void websiteTextChanged(String text);
    }

    String getWebsite();

    void setWebsiteHint(String hint);

    void setNetworkConfigHint(String hint);

    void setOnAttackCreationButtonClickListener(OnAttackCreationButtonClickListener listener);

    void setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener listener);

    void setOnWebsiteTextChangeListener(OnWebsiteTextChangeListener listener);
}
