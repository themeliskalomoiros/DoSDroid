package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists;

import java.util.LinkedHashSet;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public interface AttackListViewMvc extends ViewMvc {
    interface OnAttackClickListener {
        void onAttackClick(int position);
    }

    interface OnJoinedAttackDeleteClickListener {
        void onJoinedAttackDeleteClick(int position);
    }

    interface OnOwnerAttackDeleteClickListener {
        void onOwnerAttackDeleteClick(int position);
    }

    void bindAttacks(LinkedHashSet<Attack> attacks);

    void showLoadingIndicator();

    void hideLoadingIndicator();

    void setOnAttackClickListener(OnAttackClickListener listener);

    void setOnJoinedAttackDeleteClickListener(OnJoinedAttackDeleteClickListener listener);

    void setOnOwnerAttackDeleteClickListener(OnOwnerAttackDeleteClickListener listener);
}
