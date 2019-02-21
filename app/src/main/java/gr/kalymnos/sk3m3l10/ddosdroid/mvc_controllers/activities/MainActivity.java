package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities.attack_creation.CreateAttackActivity;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_main.MainScreenViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_main.MainScreenViewMvcImpl;

public class MainActivity extends AppCompatActivity implements MainScreenViewMvc.OnMainActionClickListener {
    private MainScreenViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewMvc();
        setupUiFrom(viewMvc);
    }

    private void initViewMvc() {
        viewMvc = new MainScreenViewMvcImpl(LayoutInflater.from(this), null);
        viewMvc.setOnMainActionClickListener(this);
    }

    private void setupUiFrom(MainScreenViewMvc viewMvc) {
        setSupportActionBar(viewMvc.getToolbar());
        setContentView(viewMvc.getRootView());
    }

    @Override
    public void onCreateAttackClick() {
        startActivity(new Intent(this, CreateAttackActivity.class));
    }

    @Override
    public void onJoinAttackClick() {
        AllAttackListsActivity.Action.startForNonJoinedAttacks(this);
    }

    @Override
    public void onContributionClick() {
        AllAttackListsActivity.Action.startForJoinedAttacks(this);
    }
}
