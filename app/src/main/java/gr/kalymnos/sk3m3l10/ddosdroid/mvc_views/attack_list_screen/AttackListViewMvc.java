package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_list_screen;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvcWithToolbar;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;

public interface AttackListViewMvc extends ViewMvcWithToolbar {

    interface OnAttackItemClickListener {
        void onAttackItemClick(int position);
    }

    void bindAttacks(List<DDoSAttack> attacks);

    void showLoadingIndicator();

    void hideLoadingIndicator();

    void bindToolbarSubtitle(String subtitle);

    void bindToolbarTitle(String title);

    void setOnAttackItemClickListener(OnAttackItemClickListener listener);

}
