package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvc;

public interface AttackInfoViewMvc extends ViewMvc {

    String WEBSITE_KEY = AttackInfoViewMvc.class.getSimpleName()+"website_key";

    interface OnBeginAttackButtonClickListener {
        void onBeginAttackButtonClick();
    }

    void bindWebsite(String website);

    void bindWebsiteHint(String text);

    void setOnBeginAttacButtonClickListener(OnBeginAttackButtonClickListener listener);
}
