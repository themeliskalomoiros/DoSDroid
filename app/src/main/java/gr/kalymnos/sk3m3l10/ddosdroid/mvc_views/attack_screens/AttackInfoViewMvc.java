package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_screens;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvc;

public interface AttackInfoViewMvc extends ViewMvc {

    interface OnBeginAttacButtonClickListener {
        void onBeginAttackButtonClick();
    }

    void bindWebsite(String website);

    void bindWebsiteHint(String text);

    void bindAttackForceText(String text);

    void bindAttackForceHint(String text);

    void setOnBeginAttacButtonClickListener(OnBeginAttacButtonClickListener listener);

    void changeFabIconToSwords();

    void changeFabIconToStop();
}
