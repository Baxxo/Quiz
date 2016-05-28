package quouo.quizone;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    Button gioca;
    int id;
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

        Richieste();

        gioca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionHandler con = new ConnectionHandler();
                String id = con.CreaPartita(Player.id);
                Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                intent.putExtra("idPartita", id);
                startActivity(intent);
            }
        });
    }

    public void Richieste() {
        ConnectionHandler con = new ConnectionHandler();
        String[] carica = con.CaricaPartite(Player.id).split("<->");

        for (int i = 0; i < carica.length - 1; i += 2) {
            AggiungiSfida(carica[i + 1], carica[i]);
        }
    }

    private void AggiungiSfida(String useramico, final String id) {
        linearLayout = (TableLayout)findViewById(R.id.linearLayout);
        System.out.println("Carico partita id: " + id);
        TableRow row = new TableRow(getApplicationContext());
        row.setLayoutParams(new ActionBar.LayoutParams(TableRow.LayoutParams.MATCH_PARENT));

        TextView nome = new TextView(getApplicationContext());
        nome.setText(useramico);
        nome.setTextColor(Color.BLACK);

        TextView nullo = new TextView(getApplicationContext());
        nullo.setText("                    ");

        Button accetta = new Button(getApplicationContext());
        accetta.setText("Accetta");
        accetta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                intent.putExtra("idPartita", id);
                startActivity(intent);
                finish();
            }
        });

        Button rifiuta = new Button(getApplicationContext());
        rifiuta.setText("Rifiuta");

        row.addView(nome);
        row.addView(nullo);
        row.addView(accetta);
        row.addView(rifiuta);

        linearLayout.addView(row);
    }

    private void makeToast(String text) {
        Toast.makeText(GameActivity.this, text, Toast.LENGTH_SHORT).show();
    }

}
