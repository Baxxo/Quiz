package quouo.quizone;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Cerca extends AppCompatActivity {
    Richieste [] richieste;

    LinearLayout linearLayout;
    private float x1, x2;
    static final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca);

        final TextView cerca = (TextView)findViewById(R.id.cercaText);

        ConnectionHandler con = new ConnectionHandler();
        richieste = con.CaricaRichieste(Player.id);

        Button b = (Button)findViewById(R.id.cercaGiocatore);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Functions.hasConnection(getApplicationContext())) {
                    ConnectionHandler con = new ConnectionHandler();
                    String[] enemy = con.CercaGiocatore(cerca.getText().toString()).split("<->");
                    if (!enemy.equals("FAILED")) {
                        linearLayout = (TableLayout) findViewById(R.id.tbl);
                        linearLayout.removeAllViews();
                        try {
                            AggiungiGiocattore(enemy[0]);
                            CaricaDomande(enemy[1]);
                        } catch (Exception e){
                            TextView t = new TextView(getApplicationContext());
                            t.setText("Niente");
                            t.setTextColor(Color.BLACK);
                            linearLayout.addView(t);
                        }
                    }
                } else {
                    makeToast("Non c'e' internet");
                }
            }
        });
    }

    private void AggiungiGiocattore(final String id){
        linearLayout = (TableLayout) findViewById(R.id.tbl);
        Button b = new Button(getApplicationContext());
        b.setText("Sfida");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Functions.hasConnection(getApplicationContext())) {
                    ConnectionHandler con = new ConnectionHandler();
                    String[] crea = con.CreaPartitaVs(Player.id, id).split("<->");
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

        linearLayout.addView(b);
    }


    //////////////////////////////////////////


    private void CaricaDomande(String nome){
        for(int i = richieste.length - 1; i >= 0; i--){
            if(richieste[i].getNemico().equals(nome)){
                switch (richieste[i].stato) {
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
                        AggiungiRichiestaAspetta(richieste[i], "Rifiutata");
                        break;
                    case NONVALIDA:
                        makeToast("Partita non valida");
                        break;
                }
            }
        }
    }


    private void AggiungiRichiestaAspetta(final Richieste richiesta, final String testo) {
        linearLayout = (TableLayout) findViewById(R.id.tbl);
        System.out.println("Carico partita id: " + richiesta.getIdRichiesta());
        final TableRow row = new TableRow(getApplicationContext());
        row.setLayoutParams(new ActionBar.LayoutParams(TableRow.LayoutParams.MATCH_PARENT));
        row.setMinimumHeight(getApplicationContext().getResources().getDisplayMetrics().densityDpi / 3);
        row.setGravity(Gravity.CENTER_VERTICAL);

        TextView nome = new TextView(getApplicationContext());
        nome.setText(richiesta.getNemico());
        nome.setTextColor(Color.BLACK);

        nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        TextView nullo = new TextView(getApplicationContext());
        nullo.setText("     ");

        TextView txt = new TextView(getApplicationContext());

        if (richiesta.stato == StatoRichiesta.FINITA) {
            if (testo.equals("Hai vinto")) {
                txt.setTextColor(Color.GREEN);
            }
            if (testo.equals("Parita")) {
                txt.setTextColor(Color.YELLOW);
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
        linearLayout = (TableLayout) findViewById(R.id.tbl);
        System.out.println("Carico partita id: " + richiesta.getIdRichiesta());
        final TableRow row = new TableRow(getApplicationContext());
        row.setLayoutParams(new ActionBar.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
        row.setMinimumHeight(getApplicationContext().getResources().getDisplayMetrics().densityDpi / 3);
        row.setGravity(Gravity.CENTER_VERTICAL);

        TextView nome = new TextView(getApplicationContext());
        nome.setText(richiesta.getNemico());
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

    private void makeToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), GameActivity.class);
        startActivity(i);
        finish();
        return;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                float deltaY = x2 + x1;
                makeToast("wait");
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    Intent i = new Intent(getApplicationContext(), GameActivity.class);
                    startActivity(i);
                    finish();
                }
                if (Math.abs(deltaY) > MIN_DISTANCE) {
                    Intent i = new Intent(getApplicationContext(), GameActivity.class);
                    startActivity(i);
                    finish();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
