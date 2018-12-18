package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class AttackListViewMvcImpl implements AttackListViewMvc {

    private View root;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private AttackAdapter attackAdapter;

    public AttackListViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        initializeViews(inflater, container);
    }

    @Override
    public void bindAttacks(List<Attack> attacks) {
        attackAdapter.addAttacks(attacks);
        attackAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadingIndicator() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setOnAttackItemClickListener(OnAttackItemClickListener listener) {
        if (attackAdapter != null) {
            attackAdapter.setOnItemClickListener(listener);
        }
    }

    @Override
    public void setOnJoinSwitchCheckedStateListener(OnJoinSwitchCheckedStateListener listener) {
        attackAdapter.setOnSwitchCheckedStateListener(listener);
    }

    @Override
    public void setOnActivateSwitchCheckedStateListener(OnActivateSwitchCheckedStateListener listener) {
        attackAdapter.setOnActivateSwitchCheckedStateListener(listener);
    }

    @Override
    public View getRootView() {
        return root;
    }

    private void initializeViews(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.screen_attack_list, container, false);
        progressBar = root.findViewById(R.id.progressBar);
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        attackAdapter = new AttackAdapter(root.getContext());
        recyclerView.setAdapter(attackAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }
}
