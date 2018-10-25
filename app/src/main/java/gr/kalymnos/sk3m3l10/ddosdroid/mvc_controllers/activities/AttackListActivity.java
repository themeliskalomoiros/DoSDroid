package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.AttackRepository;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.FakeAttackRepo;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_list_screen.AttackListViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_list_screen.AttackListViewMvcImpl;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;

public class AttackListActivity extends AppCompatActivity implements AttackRepository.OnAttacksFetchListener {

    private AttackListViewMvc viewMvc;
    private AttackRepository attackRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewMvc = new AttackListViewMvcImpl(LayoutInflater.from(this), null);
        attackRepo = new FakeAttackRepo(this);
        setContentView(viewMvc.getRootView());
    }

    @Override
    protected void onStart() {
        super.onStart();
        attackRepo.registerOnAttacksFetchListener(this);
        attackRepo.fetchAllAttacks();
        viewMvc.showLoadingIndicator();
    }

    @Override
    protected void onStop() {
        super.onStop();
        attackRepo.unRegisterOnAttacksFetchListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        attackRepo = null;
    }


    @Override
    public void attacksFetchedSuccess(List<DDoSAttack> attacks) {
        //  This method was called from a worker thread and as we know
        //  the ui must always be updated through the main (UI) thread
        viewMvc.hideLoadingIndicator();
        viewMvc.bindAttacks(attacks);
    }

    @Override
    public void attacksFetchedFail(String msg) {
        //  TODO: Display the error somewhere besides the toast
    }
}
