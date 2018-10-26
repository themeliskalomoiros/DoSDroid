package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_lists_screen.AllAttackListsViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_lists_screen.AllAttackListsViewMvcImpl;

public class AllAttackListsActivity extends AppCompatActivity{

    private AllAttackListsViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewMvc();
        setContentView(viewMvc.getRootView());
    }

    private void initializeViewMvc() {
        viewMvc = new AllAttackListsViewMvcImpl(LayoutInflater.from(this), null);
        setSupportActionBar(viewMvc.getToolbar());
    }
}
