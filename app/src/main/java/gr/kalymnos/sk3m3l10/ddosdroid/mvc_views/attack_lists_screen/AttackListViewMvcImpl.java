package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_lists_screen;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.DDoSAttack;

public class AttackListViewMvcImpl implements AttackListViewMvc {

    private View root;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    private AttacksAdapter attacksAdapter;

    public AttackListViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        initializeViews(inflater, container);
    }

    @Override
    public void bindAttacks(List<DDoSAttack> attacks) {
        attacksAdapter.addAttacks(attacks);
        attacksAdapter.notifyDataSetChanged();
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
        if (attacksAdapter != null) {
            attacksAdapter.setOnItemClickListener(listener);
        }
    }

    @Override
    public View getRootView() {
        return root;
    }

    private void initializeViews(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.screen_part_attack_list, container, false);
        progressBar = root.findViewById(R.id.progressBar);
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        attacksAdapter = new AttacksAdapter(root.getContext());
        recyclerView.setAdapter(attacksAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }
}
