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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    Button gioca;
    Dialog d;
    Dialog u;
    String ret;
    float x;
    TableLayout linearLayout;
    Richieste[] richieste;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        gioca = (Button) findViewById(R.id.start);

        TextView usern = (TextView) findViewById(R.id.username);

        if (preferences.getString("toast", "false").equals("false") == true) {
            editor.putString("toast", "true");
            editor.apply();
            Toast.makeText(GameActivity.this, "Prova a fare uno swipe a destra o a sinistra", Toast.LENGTH_LONG).show();
        }

        usern.setText(Player.nome);
        usern.setClickable(true);
        usern.setTextColor(Color.BLACK);
        usern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u = new Dialog(GameActivity.this);
                u.requestWindowFeature(Window.FEATURE_NO_TITLE);
                u.setContentView(R.layout.profile);

                u.show();

                TextView user = (TextView) u.findViewById(R.id.textView9);
                TextView t1 = (TextView) u.findViewById(R.id.textView10);
                TextView t2 = (TextView) u.findViewById(R.id.textView11);
                TextView t3 = (TextView) u.findViewById(R.id.textView12);
                TextView vinte = (TextView) u.findViewById(R.id.textView13);
                TextView par = (TextView) u.findViewById(R.id.textView14);
                TextView per = (TextView) u.findViewById(R.id.textView15);
                Button esci = (Button) u.findViewById(R.id.logOut);

                esci.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogOut();
                    }
                });

                user.setText(String.valueOf(Player.nome));
                vinte.setText(String.valueOf(Player.vinte));
                par.setText(String.valueOf(Player.pareggiate));
                per.setText(String.valueOf(Player.perse));
                user.setTextColor(Color.WHITE);

                vinte.setTextColor(Color.GREEN);
                par.setTextColor(Color.CYAN);
                per.setTextColor(Color.RED);
                t1.setTextColor(Color.GREEN);
                t2.setTextColor(Color.CYAN);
                t3.setTextColor(Color.RED);

                TextView tot = (TextView) u.findViewById(R.id.textTot);
                tot.setTextColor(Color.WHITE);
                int totale = Player.pareggiate + Player.perse + Player.vinte;
                TextView totalepartite = (TextView) u.findViewById(R.id.textView16);
                totalepartite.setText(String.valueOf(totale));

            }
        });

        if (!Functions.hasConnection(getApplicationContext())) {
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

        gioca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animazione(v);
                if (Functions.hasConnection(getApplicationContext())) {
                    ConnectionHandler con = new ConnectionHandler();
                    String[] crea = con.CreaPartita(Player.id).split("<->");
                    Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                    intent.putExtra("idPartita", crea[0]);
                    intent.putExtra("avversario", crea[1]);
                    intent.putExtra("avversarioPunt", -1);
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

        int[] stats = new int[3];

        for (int i = richieste.length - 1; i >= 0; i--) {

            switch (richieste[i].stato) {
                case DAFARE:
                    AggiungiRichiestaDaFare(richieste[i]);
                    break;
                case ASPETTA:
                    AggiungiRichiestaAspetta(richieste[i], "Turno dell'avversario");
                    break;
                case FINITA:
                    AggiungiRichiestaAspetta(richieste[i], richieste[i].risultato());
                    switch (richieste[i].risultato()) {
                        case "Hai vinto":
                            stats[0]++;
                            break;
                        case "Hai perso":
                            stats[2]++;
                            break;
                        case "Parita":
                            stats[1]++;
                            break;
                    }
                    break;
                case RIFIUTATA:
                    AggiungiRichiestaAspetta(richieste[i], "Rifiutata");
                    break;
                case NONVALIDA:
                    makeToast("Partita non valida");
                    break;
            }
        }
        TextView partite = (TextView) findViewById(R.id.partite);
        Player.setPar(stats[0], stats[1], stats[2]);
        partite.setText("Vin: " + stats[0] + " Par: " + stats[1] + " Per: " + stats[2]);
        partite.setTextColor(Color.BLACK);
    }

    private void AggiungiRichiestaAspetta(final Richieste richiesta, final String testo) {
        linearLayout = (TableLayout) findViewById(R.id.tableLayout);
        System.out.println("Carico partita id: " + richiesta.getIdRichiesta());
        final TableRow row = new TableRow(getApplicationContext());
        row.setLayoutParams(new ActionBar.LayoutParams(TableRow.LayoutParams.MATCH_PARENT));
        row.setMinimumHeight(getApplicationContext().getResources().getDisplayMetrics().densityDpi / 3);
        row.setGravity(Gravity.CENTER_VERTICAL);

        TextView nome = new TextView(getApplicationContext());
        nome.setText("      " + richiesta.getNemico());
        nome.setTextColor(Color.BLACK);

        TextView nullo = new TextView(getApplicationContext());
        nullo.setText("     ");

        TextView txt = new TextView(getApplicationContext());

        if (richiesta.stato == StatoRichiesta.FINITA) {
            if (testo.equals("Hai vinto")) {
                txt.setTextColor(Color.GREEN);
            }
            if (testo.equals("Parita")) {
                txt.setTextColor(Color.GRAY);
            }
            if (testo.equals("Hai perso")) {
                txt.setTextColor(Color.RED);
            }
            txt.setText(testo + " (" + richiesta.getMyPunt() + " : " + richiesta.getNemicoPunt() + ")");
        } else {
            txt.setText(testo);
            txt.setTextColor(Color.BLUE);
        }

        row.addView(nome);
        row.addView(nullo);
        row.addView(txt);

        linearLayout.addView(row);
    }

    private void AggiungiRichiestaDaFare(final Richieste richiesta) {
        linearLayout = (TableLayout) findViewById(R.id.tableLayout);
        System.out.println("Carico partita id: " + richiesta.getIdRichiesta());
        final TableRow row = new TableRow(getApplicationContext());
        row.setLayoutParams(new ActionBar.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
        row.setMinimumHeight(getApplicationContext().getResources().getDisplayMetrics().densityDpi / 3);
        row.setGravity(Gravity.CENTER_VERTICAL);

        TextView nome = new TextView(getApplicationContext());
        nome.setText("      " + richiesta.getNemico());
        nome.setTextColor(Color.BLACK);

        TextView nullo = new TextView(getApplicationContext());
        nullo.setText("     ");

        Button accetta = new Button(getApplicationContext());
        accetta.setText("Accetta");
        accetta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Functions.hasConnection(getApplicationContext())) {
                    Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                    intent.putExtra("idPartita", richiesta.getIdRichiesta());
                    intent.putExtra("avversario", richiesta.getNemico());
                    intent.putExtra("avversarioPunt", richiesta.getNemicoPunt());
                    startActivity(intent);
                    finish();
                } else {
                    makeToast("Non c'e' internet");
                }
            }
        });

        Button rifiuta = new Button(getApplicationContext());
        rifiuta.setText("Arrenditi");
        rifiuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Functions.hasConnection(getApplicationContext())) {
                    linearLayout.removeView(row);
                    ConnectionHandler con = new ConnectionHandler();
                    ret = con.InviaPunteggio(Player.id, richiesta.getIdRichiesta(), -2);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float delta = event.getX() - x;
                if (delta < -250) {
                    CercaIntent();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void CercaIntent() {
        Intent i = new Intent(getApplicationContext(), Cerca.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater m = getMenuInflater();
        m.inflate(R.menu.but, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.agg:
                linearLayout.removeAllViews();
                makeToast("Sto aggiornando");
                Richieste();
                break;
            case R.id.cerca:
                CercaIntent();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void LogOut() {
        d = new Dialog(GameActivity.this);
        d.setContentView(R.layout.esci);
        d.show();

        TextView tv = (TextView) d.findViewById(R.id.textView2);
        tv.setText("Sei sicuro di voler fare il LogOut?");

        Button log = (Button) d.findViewById(R.id.esci1);
        log.setText("LogOut");
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("nome");
                editor.remove("pass");
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button torna = (Button) d.findViewById(R.id.torna);
        torna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }

    private void makeToast(String text) {
        Toast.makeText(GameActivity.this, text, Toast.LENGTH_SHORT).show();
    }
    public void animazione(View v) {
        gioca.setBackgroundColor(Color.parseColor("#FFFF64E0"));
    }

}
