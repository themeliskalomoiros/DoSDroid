package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;

public interface AttackListViewMvc extends ViewMvc {
    interface OnAttackItemClickListener {
        void onAttackItemClick(int position);
    }

    interface OnJoinSwitchCheckedStateListener {
        void onJoinSwitchCheckedState(int position, boolean isChecked);
    }

    interface OnActivateSwitchCheckedStateListener {
        void onActivateSwitchCheckedState(int position, boolean isChecked);
    }

    void bindAttacks(List<DDoSAttack> attacks);

    void showLoadingIndicator();

    void hideLoadingIndicator();

    void setOnAttackItemClickListener(OnAttackItemClickListener listener);

    void setOnJoinSwitchCheckedStateListener(OnJoinSwitchCheckedStateListener listener);

    void setOnActivateSwitchCheckedStateListener(OnActivateSwitchCheckedStateListener listener);
}
