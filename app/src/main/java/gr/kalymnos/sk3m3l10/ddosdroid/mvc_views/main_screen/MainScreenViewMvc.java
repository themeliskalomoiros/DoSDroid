package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.main_screen;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvcWithToolbar;

public interface MainScreenViewMvc extends ViewMvcWithToolbar {

    interface OnOptionClickListener {
        void onCreateAttackClick();

        void onJoinAttackClick();

        void onFollowingAttacksClick();
    }

    void setOnOptionClickListener(OnOptionClickListener listener);

    void bindCreateAttackTitle(int titleRes);

    void bindJoinAttackTitle(int titleRes);

    void bindFollowingAttacksTitle(int titleRes);

    void bindCreateAttackImage(int imgRes);

    void bindJoinAttackImage(int imgRes);

    void bindFollowingAttacksImage(int imgRes);
}
