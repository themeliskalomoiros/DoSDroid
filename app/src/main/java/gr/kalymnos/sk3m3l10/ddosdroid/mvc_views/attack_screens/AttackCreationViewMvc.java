package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_screens;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvc;

public interface AttackCreationViewMvc extends ViewMvc {

    interface OnAttackCreationButtonClickListener {
        void onAttackCreationButtonClicked();
    }

    interface OnSpinnerItemSelectedListener {
        void onSpinnerItemSelected(String hint);
    }

    String getWebsite();

    void setNetworkConfigHint(String hint);

    void setOnAttackCreationButtonClickListener(OnAttackCreationButtonClickListener listener);

    void setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener listener);
}
