package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_main;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvcWithToolbar;

public interface MainScreenViewMvc extends ViewMvcWithToolbar {
    interface OnMainActionClickListener {
        void onCreateAttackClick();

        void onJoinAttackClick();

        void onContributionClick();
    }

    void setOnMainActionClickListener(OnMainActionClickListener listener);
}
