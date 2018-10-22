package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.main_screen;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvcWithToolbar;

public interface MainScreenViewMvc extends ViewMvcWithToolbar {

    interface OnOptionClickListener {
        void onCreateAttackClick();

        void onJoinAttackClick();

        void onFollowingAttacksClick();
    }

    void setOnOptionClickListener(OnOptionClickListener listener);
}
