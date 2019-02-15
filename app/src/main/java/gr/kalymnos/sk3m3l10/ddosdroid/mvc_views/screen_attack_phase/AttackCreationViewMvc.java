package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvc;

public interface AttackCreationViewMvc extends ViewMvc {

    interface OnAttackCreationButtonClickListener {
        void onAttackCreationButtonClicked(String website);
    }

    interface OnNetworkConfigurationSelectedListener {
        void onNetworkSelected(String hint);
    }

    interface OnWebsiteTextChangeListener {
        void websiteTextChanged(String text);
    }

    void setWebsiteHint(String hint);

    void setNetworkConfigHint(String hint);

    void setOnAttackCreationButtonClickListener(OnAttackCreationButtonClickListener listener);

    void setOnNetworkConfigurationSelectedListener(OnNetworkConfigurationSelectedListener listener);

    void setOnWebsiteTextChangeListener(OnWebsiteTextChangeListener listener);

    void showLoadingIndicator();

    void hideLoadingIndicator();

    int getNetworkConf();
}
