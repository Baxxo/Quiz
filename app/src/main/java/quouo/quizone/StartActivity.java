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
    Boolean accesso1;
    Boolean accesso2;
    EditText et1;
    EditText et2;
    TextView t1;
    TextView t2;
    Button accedi;
    Button registrati;
    Dialog d;
    String nome;
    String pass;
    String ris;
    ConnectionHandler hand = new ConnectionHandler();
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_start);

        et1 = (EditText) findViewById(R.id.editText);
        et2 = (EditText) findViewById(R.id.editText2);
        t1 = (TextView) findViewById(R.id.textView4);
        t2 = (TextView) findViewById(R.id.textView5);
        accedi = (Button) findViewById(R.id.button2);
        registrati = (Button) findViewById(R.id.button3);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        if (!Functions.hasConnection(this)) {
            d = new Dialog(this);
            d.setTitle("Login");
            d.setCancelable(false);
            d.setContentView(R.layout.dialog);
            d.show();

            Button b = (Button) d.findViewById(R.id.button);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    finish();
                    startActivity(intent);
                }
            });
            return;
        }


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

        if (accesso1 == true && accesso2 == true && Functions.hasConnection(this)) {

            editor.putString("nome", nome);
            editor.putString("pass", pass);
            editor.apply();
            ProgressDialog progress = ProgressDialog.show(StartActivity.this, "Attendere", "Accesso in corso...", true);
            String ris = hand.Login(nome, pass);
            final Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            Player.nome = nome;
            Player.id = Integer.valueOf(ris);
            progress.dismiss();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                }
            }, 500);
            finish();

        }

        accedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome = String.valueOf(et1.getText());
                pass = String.valueOf(et2.getText());

                if (Functions.hasConnection(getApplicationContext())) {
                    ris = hand.Login(nome, pass);

                    if (ris.equals("FAILED")) {

                        makeToast("LOGIN " + ris + "!!!!!");

                    } else {

                        nome = String.valueOf(et1.getText());

                        editor.putString("nome", nome);
                        editor.putString("pass", pass);
                        editor.apply();
                        ProgressDialog progress = ProgressDialog.show(StartActivity.this, "Attendere", "Accesso in corso...", true);
                        final Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                        Player.nome = nome;
                        Player.id = Integer.valueOf(ris);
                        progress.dismiss();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(intent);
                            }
                        }, 500);
                        finish();
                    }
                } else {
                    makeToast("Non c'e' internet");
                }
            }
        });

        registrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nome = String.valueOf(et1.getText());
                pass = String.valueOf(et2.getText());

                if (nome.length() < 5 || pass.length() < 5) {
                    makeToast("il nome o la password devono contenere almeno 5 caratteri");
                    ris = "FAILED";
                }

                if (Functions.hasConnection(getApplicationContext())) {

                    if (ris.equals("FAILED") == true) {
                        makeToast("REGISTRATION " + ris + "!!!!!");
                    } else {
                        ris = hand.Registrazione(nome, pass);
                    }

                    if (ris.equals("FAILED")) {

                        makeToast("REGISTRATION " + ris + "!!!!!");

                    }
                    if (ris.equals("USER ALREADY EXISTS")) {

                        makeToast(ris);

                    }

                    if (ris.equals("SUCCESS")) {
                        editor.putString("nome", nome);
                        editor.putString("pass", pass);
                        editor.apply();
                        ProgressDialog progress = ProgressDialog.show(StartActivity.this, "Attendere", "Registrazione in corso...", true);
                        ris = hand.Login(nome, pass);
                        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                        Player.nome = nome;
                        Player.id = Integer.valueOf(ris);
                        progress.dismiss();
                        startActivity(intent);
                        finish();
                        makeToast("Il tuo nome e': " + nome + "\nIl tuo password e': " + pass);
                    }
                } else {
                    makeToast("Non c'e' internet");
                }
            }
        });
    }

    public void onBackPressed() {
        d = new Dialog(this);
        d.setCancelable(true);
        d.setContentView(R.layout.esci);
        d.show();

        Button esci = (Button) d.findViewById(R.id.esci1);
        esci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        Button torna = (Button) d.findViewById(R.id.torna);
        torna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        return;
    }

    private void makeToast(String text) {
        Toast.makeText(StartActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}

