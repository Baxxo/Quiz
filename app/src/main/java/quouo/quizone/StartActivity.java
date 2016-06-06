package quouo.quizone;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    Boolean accesso1;
    Boolean accesso2;
    Dialog d;
    String nome;
    String pass;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        final EditText et1 = (EditText) findViewById(R.id.editText);
        final EditText et2 = (EditText) findViewById(R.id.editText2);
        Button accedi = (Button) findViewById(R.id.button2);
        Button registrati = (Button) findViewById(R.id.button3);

        final ConnectionHandler hand = new ConnectionHandler();

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
                    startActivity(intent);
                    finish();
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
            try {
                String[] ris = hand.Login(nome, pass).split("<->");
                final Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                Player.nome = ris[1];
                Player.id = Integer.valueOf(ris[0]);
                progress.dismiss();
                startActivity(intent);
                finish();
            } catch (Exception e) {
                makeToast("C'e' qualcosa che non va");
                progress.dismiss();
            }
        }

        accedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome = String.valueOf(et1.getText());
                pass = String.valueOf(et2.getText());

                if (nome.length() < 5 || pass.length() < 5) {
                    makeToast("il nome o la password devono contenere almeno 5 caratteri");
                    return;
                }

                if (pass.length() < 1) {
                    makeToast("inserisci password");
                    return;
                }
                if (nome.length() < 1) {
                    makeToast("inserisci nome utente");
                    return;
                }

                if (Functions.hasConnection(getApplicationContext())) {
                    String[] ris = hand.Login(nome, pass).split("<->");

                    if (ris.equals("FAILED")) {

                        makeToast("LOGIN " + ris);

                    } else {

                        editor.putString("nome", nome);
                        editor.putString("pass", pass);
                        editor.apply();


                        ProgressDialog progressd = ProgressDialog.show(StartActivity.this, "Attendere", "Accesso in corso...", true);
                        try {
                            final Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                            Player.nome = ris[1];
                            Player.id = Integer.valueOf(ris[0]);
                            progressd.dismiss();
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                            progressd.dismiss();
                            makeToast("C'e' qualcosa che non va");
                        }

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
                    return;
                }

                if (pass.length() < 0) {
                    makeToast("inserisci password");
                    return;
                }
                if (nome.length() < 0) {
                    makeToast("inserisci nome utente");
                    return;
                }


                if (Functions.hasConnection(getApplicationContext())) {

                    String ris = hand.Registrazione(nome, pass);

                    if (ris.equals("FAILED") || ris.equals("USER ALREADY EXISTS")) {
                        makeToast(ris);
                    }

                    if (ris.equals("SUCCESS")) {
                        editor.putString("nome", nome);
                        editor.putString("pass", pass);
                        editor.apply();
                        ProgressDialog progress = ProgressDialog.show(StartActivity.this, "Attendere", "Registrazione in corso...", true);
                        String[] log = hand.Login(nome, pass).split("<->");
                        Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                        Player.nome = log[1];
                        Player.id = Integer.valueOf(log[0]);
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

