package quouo.quizone;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Matteo on 30/05/2016.
 */

public class SplashScreen extends Activity {
    int i = 0;
    int n = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

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
                        sleep(300);
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