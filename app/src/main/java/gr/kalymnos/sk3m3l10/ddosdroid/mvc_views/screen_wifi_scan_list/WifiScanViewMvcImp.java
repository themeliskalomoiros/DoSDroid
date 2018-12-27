package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_wifi_scan_list;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.R;

public class WifiScanViewMvcImp implements WifiScanViewMvc {
    private View root;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private WifiScanAdapter adapter;

    public WifiScanViewMvcImp(LayoutInflater inflater, ViewGroup parent) {
        initializeViews(inflater, parent);
    }

    private void initializeViews(LayoutInflater inflater, ViewGroup parent) {
        root = inflater.inflate(R.layout.screen_wifi_list, parent, false);
        toolbar = root.findViewById(R.id.toolBar);
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        adapter = new WifiScanAdapter(root.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void bindResults(List<String> scanResults) {
        adapter.addScanResults(scanResults);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setOnWifiScanItemClickListener(OnWifiScanItemClickListener listener) {
        adapter.setItemClickListener(listener);
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public View getRootView() {
        return root;
    }
}
