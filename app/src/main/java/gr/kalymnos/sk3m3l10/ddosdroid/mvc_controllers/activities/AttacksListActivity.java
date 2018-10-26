package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.FakeAttackRepo;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_list_screen.AttacksListViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_list_screen.AttacksListViewMvcImpl;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;

import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.bundleIsValidAndContainsKey;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.ValidationUtils.listHasItems;

public class AttacksListActivity extends AppCompatActivity{

    private AttacksListViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewMvc();
        setContentView(viewMvc.getRootView());
    }

    private void initializeViewMvc() {
        viewMvc = new AttacksListViewMvcImpl(LayoutInflater.from(this), null);
        setSupportActionBar(viewMvc.getToolbar());
    }
}
