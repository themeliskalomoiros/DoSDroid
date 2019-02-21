package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_join_attack;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvc;

public interface JoinAttackInfoViewMvc extends ViewMvc {
    interface OnJoinAttackClickListener {
        void onJoinAttackClicked();
    }

    void setOnJoinAttackClickListener(OnJoinAttackClickListener listener);

    void bindWebsite(String website);

    void bindCreationDate(String date);

    void bindAttackForce(int count);

    void bindLaunchDate(String date);

    void bindNetworkConfiguration(String networkConf);
}
