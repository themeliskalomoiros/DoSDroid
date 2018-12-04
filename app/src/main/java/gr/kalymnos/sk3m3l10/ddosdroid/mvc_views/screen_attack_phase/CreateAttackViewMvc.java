package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_phase;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvcWithToolbar;

public interface CreateAttackViewMvc extends ViewMvcWithToolbar {
    void bindToolbarSubtitle(CharSequence subtitle);

    int getFragmentContainerId();
}
