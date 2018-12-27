package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_wifi_scan_list.WifiScanViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_wifi_scan_list.WifiScanViewMvcImp;

public class WifiScanResultsActivity extends AppCompatActivity implements WifiScanViewMvc.OnWifiScanItemClickListener {
    public static final String EXTRA_WIFI_SCAN_RESULTS = "extra wifi scan results";

    private WifiScanViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewMvc();
        setContentView(viewMvc.getRootView());
    }

    private void initializeViewMvc() {
        viewMvc = new WifiScanViewMvcImp(LayoutInflater.from(this), null);
        viewMvc.setOnWifiScanItemClickListener(this);
    }

    @Override
    public void onWifiScanItemClick(int position) {

    }
}
