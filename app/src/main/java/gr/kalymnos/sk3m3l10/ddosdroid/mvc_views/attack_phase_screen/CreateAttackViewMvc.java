package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_phase_screen;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvcWithToolbar;

public interface CreateAttackViewMvc extends ViewMvcWithToolbar {
    void bindToolbarSubtitle(CharSequence subtitle);

    int getFragmentContainerId();
}
