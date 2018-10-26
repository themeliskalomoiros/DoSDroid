package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_lists_screen;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;

public interface AttackListViewMvc extends ViewMvc {
    interface OnAttackItemClickListener {
        void onAttackItemClick(int position);
    }

    void bindAttacks(List<DDoSAttack> attacks);

    void showLoadingIndicator();

    void hideLoadingIndicator();

    void setOnAttackItemClickListener(OnAttackItemClickListener listener);
}
