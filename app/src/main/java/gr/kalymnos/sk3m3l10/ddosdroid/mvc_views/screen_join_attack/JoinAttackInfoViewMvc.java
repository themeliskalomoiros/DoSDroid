package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_join_attack;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvcWithToolbar;

public interface JoinAttackInfoViewMvc extends ViewMvcWithToolbar {
    interface OnJoinAttackClickListener {
        void onJoinAttackClicked();
    }

    void setOnJoinAttackClickListener(OnJoinAttackClickListener listener);

    void bindWebsite(String website);

    void bindWebsiteDate(long dateMilli);

    void bindAttackForce(int count);

    void bindNetworkConfiguration(String networkConf);

}
