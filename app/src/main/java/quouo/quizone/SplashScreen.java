package quouo.quizone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Matteo on 30/05/2016.
 */

public class SplashScreen extends Activity {
    int i = 0;
    int n = 100;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(n);

        final TextView tv = (TextView) findViewById(R.id.textView8);

        Thread timerThread = new Thread() {
            public void run() {
                for (i = 0; i < n; i += 10) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (i % 3 == 0) {
                                tv.setText(".");
                            }
                            if (i % 3 == 1) {
                                tv.setText("..");
                            }
                            if (i % 3 == 2) {
                                tv.setText("...");
                            }
                        }
                    });
                    try {
                        progressBar.setProgress(i);
                        sleep(280);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
                finish();
            }
        };
        timerThread.start();
    }

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
        return;
    }
}