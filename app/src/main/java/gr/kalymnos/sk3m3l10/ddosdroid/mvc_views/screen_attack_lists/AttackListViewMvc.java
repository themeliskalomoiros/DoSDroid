package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists;

import java.util.LinkedHashSet;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public interface AttackListViewMvc extends ViewMvc {
    interface OnAttackClickListener {
        void onAttackClick(int position);
    }

    interface OnJoinSwitchCheckedStateListener {
        void onJoinSwitchCheckedState(int position, boolean isChecked);
    }

    interface OnActivateSwitchCheckedStateListener {
        void onActivateSwitchCheckedState(int position, boolean isChecked);
    }

    void bindAttacks(LinkedHashSet<Attack> attacks);

    void showLoadingIndicator();

    void hideLoadingIndicator();

    void setOnAttackClickListener(OnAttackClickListener listener);

    void setOnJoinSwitchCheckedStateListener(OnJoinSwitchCheckedStateListener listener);

    void setOnActivateSwitchCheckedStateListener(OnActivateSwitchCheckedStateListener listener);
}
