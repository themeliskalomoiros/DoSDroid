package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_wifi_scan_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.R;

import static gr.kalymnos.sk3m3l10.ddosdroid.utils.CollectionUtils.hasItems;

public class WifiScanAdapter extends RecyclerView.Adapter<WifiScanAdapter.ScanResultHolder> {
    private Context context;
    private List<String> scanResults;
    private WifiScanViewMvc.OnWifiScanItemClickListener itemClickListener;

    public WifiScanAdapter(Context context) {
        this.context = context;
    }

    public void addScanResults(List<String> scanResults) {
        this.scanResults = scanResults;
    }

    public void setItemClickListener(WifiScanViewMvc.OnWifiScanItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ScanResultHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_spinner, viewGroup, false);
        return new ScanResultHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanResultHolder viewHolder, int i) {
        if (hasItems(scanResults)) {
            viewHolder.bind(scanResults.get(i));
        }
    }

    @Override
    public int getItemCount() {
        if (hasItems(scanResults)) {
            return scanResults.size();
        }
        return 0;
    }

    class ScanResultHolder extends RecyclerView.ViewHolder {
        public ScanResultHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener((view) -> {
                if (itemClickListener != null)
                    itemClickListener.onWifiScanItemClick(getAdapterPosition());
            });
        }

        void bind(String result) {
            TextView textView = (TextView) itemView;
            textView.setText(result);
        }
    }
}
