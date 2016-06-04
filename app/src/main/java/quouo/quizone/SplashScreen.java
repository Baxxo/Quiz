package quouo.quizone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

        if (preferences.getString("log", "false").equals("false")) {
            editor.putString("log", "apply");
            editor.apply();
            addShortcut();
        }

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

    private void addShortcut() {
        Intent shortcutIntent = new Intent(getApplicationContext(),
                StartActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "QuizOne");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        R.drawable.logo));

        addIntent
                .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);

        Toast.makeText(this, "Aggiunta una scorciatoia nella Home Screen", Toast.LENGTH_SHORT);
    }
}