package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvc;

public interface AttackCreationViewMvc extends ViewMvc {

    interface OnAttackCreationClickListener {
        void onAttackCreateClicked(String website);
    }

    interface OnNetworkConfigurationSelectedListener {
        void onNetworkSelected(String hint);
    }

    interface OnWebsiteTextChangeListener {
        void websiteTextChanged(String text);
    }

    interface OnPickerClickListener {
        void onTimePickerClick();

        void onDatePickerClick();
    }

    void bindWebsiteCreationTime(String hint);

    void bindNetworkConfig(String hint);

    void setOnAttackCreationClickListener(OnAttackCreationClickListener listener);

    void setOnNetworkConfigurationSelectedListener(OnNetworkConfigurationSelectedListener listener);

    void setOnWebsiteTextChangeListener(OnWebsiteTextChangeListener listener);

    void setOnPickerClickListener(OnPickerClickListener listener);

    int getNetworkConf();

    void showLoadingIndicator();

    void hideLoadingIndicator();
}
