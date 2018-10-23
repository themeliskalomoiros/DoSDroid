package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_screens;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvc;

public interface AttackInfoViewMvc extends ViewMvc {

    String WEBSITE_KEY = AttackInfoViewMvc.class.getSimpleName()+"website_key";
    String ATTACK_STARTED_KEY = AttackInfoViewMvc.class.getSimpleName()+"attack_started_key";

    interface OnBeginAttackButtonClickListener {
        void onBeginAttackButtonClick();
    }

    void bindWebsite(String website);

    void bindWebsiteHint(String text);

    void bindAttackForceText(String text);

    void bindAttackForceHint(String text);

    void setOnBeginAttacButtonClickListener(OnBeginAttackButtonClickListener listener);

    void changeFabIconToSwords();

    void changeFabIconToStop();
}
