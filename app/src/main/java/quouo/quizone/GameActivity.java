package quouo.quizone;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    //TableLayout richiestesfida;
    Button gioca;
    int id;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        gioca = (Button) findViewById(R.id.start);

        /*Bundle usernamepassato = getIntent().getExtras();
        String username = usernamepassato.getString("User");*/
        String username = Player.nome;

        TextView usern = (TextView) findViewById(R.id.username);
        usern.setText(username);

        Richieste();

        gioca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
                startActivity(intent);

            }
        });
    }

    public void Richieste() {
        String nome = "1-Ciao-2-bello-3-Gian-4-Fantastico-5-Favoloso-";
        String copia[] = nome.split("-");

        for (int i = 0; i < copia.length; i += 2) {
            AggiungiSfida(copia[i + 1], copia[i]);
        }
    }

    private void AggiungiSfida(String useramico, String id) {
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);

        TableRow row = new TableRow(getApplicationContext());
        row.setLayoutParams(new ActionBar.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));

        TextView nome = new TextView(getApplicationContext());
        nome.setText(useramico);
        nome.setTextColor(Color.BLACK);

        Button accetta = new Button(getApplicationContext());
        accetta.setText("Accetta");

        Button rifiuta = new Button(getApplicationContext());
        rifiuta.setText("Rifiuta");

        row.addView(nome);
        row.addView(accetta);
        row.addView(rifiuta);

        linearLayout.addView(row);
    }

    private void makeToast(String text) {
        Toast.makeText(GameActivity.this, text, Toast.LENGTH_SHORT).show();
    }

}
