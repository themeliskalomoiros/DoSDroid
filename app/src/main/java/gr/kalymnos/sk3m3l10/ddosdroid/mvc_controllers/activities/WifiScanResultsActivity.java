package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_wifi_scan_list.WifiScanViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_wifi_scan_list.WifiScanViewMvcImp;

public class WifiScanResultsActivity extends AppCompatActivity implements WifiScanViewMvc.OnWifiScanItemClickListener {
    private static final String EXTRA_WIFI_SCAN_RESULTS = "extra wifi scan results";
    public static final String ACTION_SCAN_RESULT_CHOSEN = "action scan result chosen from user";
    public static final String ACTION_SCAN_RESULT_CANCELLED = "action scan result cancelled from user";
    public static final String ACTION_SCAN_RESULT_CONNECTION_ERROR = "action scan result connection error";

    private WifiScanViewMvc viewMvc;
    private List<ScanResult> scanResults;

    public static void startInstance(Context context, List<String> scanResults) {
        Intent intent = new Intent(context, WifiScanResultsActivity.class);
        intent.putStringArrayListExtra(EXTRA_WIFI_SCAN_RESULTS, (ArrayList<String>) scanResults);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeViewMvc();
        setupUi();
        setContentView(viewMvc.getRootView());
    }

    private void initializeViewMvc() {
        viewMvc = new WifiScanViewMvcImp(LayoutInflater.from(this), null);
        viewMvc.setOnWifiScanItemClickListener(this);
    }

    private void setupUi() {
        setupActionBar();
        bindScanResults();
    }

    private void setupActionBar() {
        setSupportActionBar(viewMvc.getToolbar());
        getSupportActionBar().setLogo(R.drawable.ic_wifi_black_24dp);
    }

    private void bindScanResults() {
        scanResults = getIntent().getParcelableArrayListExtra(EXTRA_WIFI_SCAN_RESULTS);
        List<String> scanResultTitles = new ArrayList<>();
        for (ScanResult scanResult : scanResults) {
            scanResultTitles.add(scanResult.SSID);
        }
        viewMvc.bindResults(scanResultTitles);
    }

    @Override
    public void onWifiScanItemClick(int position) {
        // TODO must implement to connect to the network
        // For mocking we will just assume that is connected
        Intent intent = new Intent(ACTION_SCAN_RESULT_CHOSEN);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.sendBroadcast(intent);
    }
}
