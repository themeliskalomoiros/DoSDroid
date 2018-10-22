package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_screens;

import android.view.View;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvcWithToolbar;

public interface AttackPhaseViewMvc extends ViewMvcWithToolbar {
    void bindToolbarSubtitle(CharSequence subtitle);

    View getFragmentContainer();
}
