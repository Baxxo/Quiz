package quouo.quizone;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    Handler handler = new Handler();
    Boolean con;
    Boolean accesso;
    Boolean accesso1;
    Boolean accesso2;
    EditText et1;
    EditText et2;
    TextView t1;
    TextView t2;
    Button accedi;
    Button registrati;
    Dialog d;
    String ris = new String();
    String nome;
    String pass;
    String id;
    ConnectionHandler hand = new ConnectionHandler();
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_start);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        d = new Dialog(this);

        if (!hasConnection()) {
            d.setTitle("Login");
            d.setCancelable(false);
            d.setContentView(R.layout.dialog);
            d.show();

            Button b = (Button) d.findViewById(R.id.button);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
        }

        makeToast(preferences.getString("nome", "null") + " " + preferences.getString("pass", "null"));

        if (preferences.getString("nome", "null").equals("null")) {
            accesso1 = false;
        } else {
            accesso1 = true;
            nome = preferences.getString("nome", "null");
        }
        if (preferences.getString("pass", "null").equals("null")) {
            accesso2 = false;
        } else {
            accesso2 = true;
            pass = preferences.getString("pass", "null");
        }

        if (accesso1 == true && accesso2 == true) {
            ris = hand.Login(nome, pass);
            makeToast("login " + ris);

            if (con == true) {
                editor.putString("nome", nome);
                editor.putString("pass", pass);
                editor.apply();
                progress = ProgressDialog.show(StartActivity.this, "Attendere", "Accesso in corso...", true);
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                Player.nome = nome;
                Player.id = Integer.valueOf(ris);
                progress.dismiss();
                startActivity(intent);
                finish();
            }
        }

        et1 = (EditText) findViewById(R.id.editText);
        et2 = (EditText) findViewById(R.id.editText2);
        t1 = (TextView) findViewById(R.id.textView4);
        t2 = (TextView) findViewById(R.id.textView5);
        accedi = (Button) findViewById(R.id.button2);
        registrati = (Button) findViewById(R.id.button3);

        accedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nome = String.valueOf(et1.getText());
                pass = String.valueOf(et2.getText());

                ris = hand.Login(nome, pass);

                if (ris.equals("FAILED")) {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("login " + ris);
                        }
                    }, 1);

                } else {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("login " + ris);
                        }
                    }, 1);

                    nome = String.valueOf(et1.getText());
                    if (con == true) {
                        editor.putString("nome", nome);
                        editor.putString("pass", pass);
                        editor.apply();
                        progress = ProgressDialog.show(StartActivity.this, "Attendere", "Accesso in corso...", true);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                                Player.nome = nome;
                                Player.id = Integer.valueOf(ris);
                                progress.dismiss();
                                startActivity(intent);
                                finish();
                            }

                        }, 1000);
                    }
                }
            }
        });

        registrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nome = String.valueOf(et1.getText());
                pass = String.valueOf(et2.getText());

                ris = hand.Registrazione(nome, pass);

                if (ris.equals("FAILED")) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("registration " + ris);
                        }
                    }, 1);
                }
                if (ris.equals("USER ALREADY EXISTS")) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("registration " + ris);
                        }
                    }, 1);
                }
                if (ris.equals("SUCCESS")) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("registration " + ris);
                        }
                    }, 1);
                    nome = String.valueOf(et1.getText());

                    if (con == true) {
                        editor.putString("nome", nome);
                        editor.putString("pass", pass);
                        editor.apply();
                        progress = ProgressDialog.show(StartActivity.this, "Attendere", "Registrazione in corso...", true);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                                Player.nome = nome;
                                Player.id = Integer.valueOf(ris);
                                progress.dismiss();
                                startActivity(intent);
                                finish();
                            }

                        }, 1000);
                    }
                }
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back.
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void makeToast(String text) {
        Toast.makeText(StartActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    public boolean hasConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            con = true;
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            con = true;
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            con = true;
            return true;
        }

        con = false;
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Debug("Resume");
    }

    void Debug(String s) {
        System.out.println(s);
    }

}

