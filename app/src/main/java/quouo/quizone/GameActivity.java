package quouo.quizone;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    Button gioca;
    int id;
    Dialog d;
    ConnectionHandler hand = new ConnectionHandler();
    BackgroundTask backgroundTask = new BackgroundTask();
    TableLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        gioca = (Button) findViewById(R.id.start);

        TextView usern = (TextView) findViewById(R.id.username);
        usern.setText(Player.nome);

        if(!Functions.hasConnection(getApplicationContext())){
            d = new Dialog(this);
            d.setTitle("Non c'e' internet");
            d.setCancelable(false);
            d.setContentView(R.layout.dialog);
            d.show();

            Button b = (Button) d.findViewById(R.id.button);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                    Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                    finish();
                    startActivity(intent);
                }
            });
        }

        TextView logOut = (TextView)findViewById(R.id.textView3);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                p.edit().clear().commit();
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        gioca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Functions.hasConnection(getApplicationContext())) {
                    ConnectionHandler con = new ConnectionHandler();
                    String id = con.CreaPartita(Player.id);
                    Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                    intent.putExtra("idPartita", id);
                    startActivity(intent);
                    finish();
                } else {
                    makeToast("Non c'e' internet");
                }
            }
        });

        Richieste();
    }

    public void Richieste() {
        ConnectionHandler con = new ConnectionHandler();
        String[] carica = con.CaricaPartite(Player.id).split("<->");

        for (int i = 0; i < carica.length - 1; i += 2) {
            AggiungiSfida(carica[i + 1], carica[i]);
        }
    }

    private void AggiungiSfida(final String useramico, final String id) {
        linearLayout = (TableLayout) findViewById(R.id.tableLayout);
        System.out.println("Carico partita id: " + id);
        final TableRow row = new TableRow(getApplicationContext());
        row.setLayoutParams(new ActionBar.LayoutParams(TableRow.LayoutParams.MATCH_PARENT));

        TextView nome = new TextView(getApplicationContext());
        nome.setText(useramico);
        nome.setTextColor(Color.BLACK);

        TextView nullo = new TextView(getApplicationContext());
        nullo.setText("          ");

        Button accetta = new Button(getApplicationContext());
        accetta.setText("Accetta");
        accetta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Functions.hasConnection(getApplicationContext())) {
                    linearLayout.removeView(row);
                    Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                    intent.putExtra("idPartita", id);
                    intent.putExtra("avversario", useramico);
                    startActivity(intent);
                    finish();
                } else {
                    makeToast("Non c'e' internet");
                }
            }
        });

        Button rifiuta = new Button(getApplicationContext());
        rifiuta.setText("Rifiuta");
        rifiuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Functions.hasConnection(getApplicationContext())) {
                    linearLayout.removeView(row);
                    ConnectionHandler con = new ConnectionHandler();
                    String ret = con.TerminaPartita(id, Player.id);
                    System.out.println("Cancellazione della partita: " + ret);
                } else {
                    makeToast("Non c'e' internet");
                }
            }
        });

        row.addView(nome);
        row.addView(nullo);
        row.addView(accetta);
        row.addView(rifiuta);

        linearLayout.addView(row);
    }

    @Override
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
        Toast.makeText(GameActivity.this, text, Toast.LENGTH_SHORT).show();
    }

}
