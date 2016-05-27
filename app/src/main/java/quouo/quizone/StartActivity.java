package quouo.quizone;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

public class StartActivity extends AppCompatActivity {

    Handler handler = new Handler();
    Boolean con;
    Boolean accesso;
    EditText et1;
    EditText et2;
    TextView t1;
    TextView t2;
    Button accedi;
    Button registrati;
    String user;
    Dialog d;
    Dialog l;
    String ris = new String();
    ConnectionHandler hand = new ConnectionHandler();
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_start);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        d = new Dialog(this);
        l = new Dialog(this);

        accesso = Boolean.getBoolean(preferences.getString("Accesso", "null"));
        makeToast("fuori:" + accesso);

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

        if (accesso == false) {

            et1 = (EditText) findViewById(R.id.editText);
            et2 = (EditText) findViewById(R.id.editText2);
            t1 = (TextView) findViewById(R.id.textView4);
            t2 = (TextView) findViewById(R.id.textView5);
            accedi = (Button) findViewById(R.id.button2);
            registrati = (Button) findViewById(R.id.button3);

            accedi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("nome", String.valueOf(et1.getText()));
                    editor.putString("pass", String.valueOf(et2.getText()));
                    editor.apply();
                    ris = hand.Login(preferences.getString("nome", "vuoto"), preferences.getString("pass", "vuoto"));

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

                        user = String.valueOf(et1.getText());
                        if (con == true) {/*
                            d.setTitle("ciao");
                            d.setCancelable(false);
                            d.setContentView(R.layout.login);
                            d.show();*/
                            accesso = true;
                            editor.putString("Accesso", "true");
                            editor.apply();
                            makeToast("funzione:" + preferences.getString("Accesso", "null"));
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                                    Player.nome = user;
                                    Player.id = Integer.valueOf(ris);
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
                    ris = hand.Registrazione(String.valueOf(et1.getText()), String.valueOf(et2.getText()));
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
                        user = String.valueOf(et1.getText());

                        if (con == true) {
                            accesso = true;
                            editor.putString("Accesso", "true");
                            editor.apply();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                                    intent.putExtra("User", user);
                                    startActivity(intent);
                                }
                            }, 1000);
                        }
                    }
                }
            });
        } else {

            et1.setVisibility(View.INVISIBLE);
            et2.setVisibility(View.INVISIBLE);
            t1.setVisibility(View.INVISIBLE);
            t2.setVisibility(View.INVISIBLE);
            accedi.setVisibility(View.INVISIBLE);
            registrati.setVisibility(View.INVISIBLE);

            ris = hand.Login(preferences.getString("nome", "null"), preferences.getString("pass", "null"));

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

                user = String.valueOf(et1.getText());
                if (con == true) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            Player.nome = user;
                            Player.id = Integer.valueOf(ris);
                            startActivity(intent);
                            finish();
                        }


                    }, 1000);
                }
            }
        }
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

