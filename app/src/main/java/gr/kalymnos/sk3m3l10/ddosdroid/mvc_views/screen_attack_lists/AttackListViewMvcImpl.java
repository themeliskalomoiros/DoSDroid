package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.LinkedHashSet;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public class AttackListViewMvcImpl implements AttackListViewMvc {
    private View root;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private AttacksAdapter attacksAdapter;

    public AttackListViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        initFields(inflater, container);
        setupRecyclerView();
    }

    private void initFields(LayoutInflater inflater, ViewGroup container) {
        initViews(inflater, container);
        attacksAdapter = new AttacksAdapter(root.getContext());
    }

    private void initViews(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.screen_attack_list, container, false);
        progressBar = root.findViewById(R.id.progressBar);
        recyclerView = root.findViewById(R.id.recyclerView);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setAdapter(attacksAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void bindAttacks(LinkedHashSet<Attack> attacks) {
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
    public void setOnAttackClickListener(OnAttackClickListener listener) {
        if (attacksAdapter != null) {
            attacksAdapter.setOnItemClickListener(listener);
        }
    }

    @Override
    public void setOnJoinedAttackDeleteClickListener(OnJoinedAttackDeleteClickListener listener) {
        attacksAdapter.setOnJoinedAttackDeleteClickListener(listener);
    }

    @Override
    public void setOnOwnerAttackDeleteClickListener(OnOwnerAttackDeleteClickListener listener) {
        attacksAdapter.setOnOwnerAttackDeleteClickListener(listener);
    }

    @Override
    public View getRootView() {
        return root;
    }
}
