package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Toast;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.main_screen.MainScreenViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.main_screen.MainScreenViewMvcImpl;

public class MainActivity extends AppCompatActivity implements MainScreenViewMvc.OnOptionClickListener{

    private MainScreenViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewMvc = new MainScreenViewMvcImpl(LayoutInflater.from(this),null);
        viewMvc.setOnOptionClickListener(this);
        setContentView(viewMvc.getRootView());
    }

    @Override
    public void onCreateAttackClick() {
        Toast.makeText(this, "oncreateattack", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onJoinAttackClick() {
        Toast.makeText(this, "onjoinattack", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFollowingAttacksClick() {
        Toast.makeText(this, "onfollowingattacks", Toast.LENGTH_SHORT).show();
    }
}
