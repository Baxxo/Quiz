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
    Richieste[] richieste;

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
                    String[] crea = con.CreaPartita(Player.id).split("<->");
                    Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                    intent.putExtra("idPartita", crea[0]);
                    intent.putExtra("avversario", crea[1]);
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
        richieste = con.CaricaRichieste(Player.id);

        for(int i = 0; i < richieste.length; i++){
            Functions.Debug("Aggiungo richieste: " + i);

            switch (richieste[i].stato){
                case DAFARE:
                    AggiungiRichiestaDaFare(richieste[i]);
                    break;
                case ASPETTA:
                    AggiungiRichiestaAspetta(richieste[i], "Turno dell'avversario");
                    break;
                case FINITA:
                    AggiungiRichiestaAspetta(richieste[i], richieste[i].risultato());
                    break;
                case RIFIUTATA:
                    AggiungiRichiestaAspetta(richieste[i], "La partita e' stata rifiutata");
                    break;
                case NONVALIDA:
                    makeToast("Partita non valida");
                    break;
            }
        }
    }

    private void AggiungiRichiestaAspetta(final Richieste richiesta, final String testo) {
        linearLayout = (TableLayout) findViewById(R.id.tableLayout);
        System.out.println("Carico partita id: " + richiesta.getIdRichiesta());
        final TableRow row = new TableRow(getApplicationContext());
        row.setLayoutParams(new ActionBar.LayoutParams(TableRow.LayoutParams.MATCH_PARENT));

        TextView nome = new TextView(getApplicationContext());
        nome.setText(richiesta.getNemico());
        nome.setTextColor(Color.BLACK);

        TextView nullo = new TextView(getApplicationContext());
        nullo.setText("        ");

        TextView txt = new TextView(getApplicationContext());
        txt.setText(testo);
        txt.setTextColor(Color.BLACK);

        row.addView(nome);
        row.addView(nullo);
        row.addView(txt);

        linearLayout.addView(row);
    }

    private void AggiungiRichiestaDaFare(final Richieste richiesta) {
        linearLayout = (TableLayout) findViewById(R.id.tableLayout);
        System.out.println("Carico partita id: " + richiesta.getIdRichiesta());
        final TableRow row = new TableRow(getApplicationContext());
        row.setLayoutParams(new ActionBar.LayoutParams(TableRow.LayoutParams.MATCH_PARENT));

        TextView nome = new TextView(getApplicationContext());
        nome.setText(richiesta.getNemico());
        nome.setTextColor(Color.BLACK);

        TextView nullo = new TextView(getApplicationContext());
        nullo.setText("        ");

        Button accetta = new Button(getApplicationContext());
        accetta.setText("Accetta");
        accetta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Functions.hasConnection(getApplicationContext())) {
                    linearLayout.removeView(row);
                    Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                    intent.putExtra("idPartita", richiesta.getIdRichiesta());
                    intent.putExtra("avversario", richiesta.getNemico());
                    startActivity(intent);
                    finish();
                } else {
                    makeToast("Non c'e' internet");
                }
            }
        });

        Button rifiuta = new Button(getApplicationContext());
        rifiuta.setText("Arrenderti");
        rifiuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Functions.hasConnection(getApplicationContext())) {
                    linearLayout.removeView(row);
                    ConnectionHandler con = new ConnectionHandler();
                    String ret = con.InviaPunteggio(Player.id, richiesta.getIdRichiesta(), -2);
                    AggiungiRichiestaAspetta(richiesta, "Ti sei arresso");
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
