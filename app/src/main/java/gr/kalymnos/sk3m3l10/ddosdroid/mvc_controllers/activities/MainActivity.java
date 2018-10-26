package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.main_screen.MainScreenViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.main_screen.MainScreenViewMvcImpl;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;

public class MainActivity extends AppCompatActivity implements MainScreenViewMvc.OnOptionClickListener {

    private MainScreenViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewMvc = new MainScreenViewMvcImpl(LayoutInflater.from(this), null);
        viewMvc.setOnOptionClickListener(this);
        setSupportActionBar(viewMvc.getToolbar());
        setContentView(viewMvc.getRootView());
    }

    @Override
    public void onCreateAttackClick() {
        startActivity(new Intent(this, AttackPhaseActivity.class));
    }

    @Override
    public void onJoinAttackClick() {
        Toast.makeText(this, "onjoinattack", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFollowingAttacksClick() {
        Bundle extras = new Bundle();
        extras.putInt(DDoSAttack.ATTACK_TYPE_KEY, DDoSAttack.TYPE_FETCH_FOLLOWING);
        Intent intent = new Intent(this, AllAttackListsActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
