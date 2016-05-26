package quouo.quizone;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    //TableLayout richiestesfida;
    Button gioca;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        gioca = (Button) findViewById(R.id.start);

        Bundle usernamepassato = getIntent().getExtras();
        String username = usernamepassato.getString("User");

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
        //Richiedi Utenti nomi e id
        //utilizzo di string split per dividere la stringa

        String nome = "1-Ciao-2-bello-3-Gian-4-Fantastico-5-Favoloso-";
        String copia[] = nome.split("-");

        for (int i = 0; i < copia.length; i += 2) {
            //if(copia.length<4) {
            AggiungiSfida(copia[i + 1], copia[i], i);
            //}else{
            //  AggiungiSfidaScroll(copia[i + 1], copia[i], i);
            //}

        }
    }

   /*private void AggiungiSfidaScroll(String useramico,String id,int pos) {
        ScrollView scro = new ScrollView(this);
        AggiungiSfida(useramico,id,pos);
    }*/

    private void AggiungiSfida(String useramico, String id, int pos) {
        //passaggio di stringhe
        //crea textview e due button
        // http://stackoverflow.com/questions/31203009/how-to-create-an-android-table-dynamically-by-reusing-one-layout ---- Vedere come fare la table dinamica
        // http://stackoverflow.com/questions/18207470/adding-table-rows-dynamically-in-android
        ScrollView scro = new ScrollView(this);
        TableLayout ll = (TableLayout) findViewById(R.id.richieste);
        TableRow riga = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        riga.setLayoutParams(lp);

        TextView usertext = new TextView(this);
        usertext.setId(Integer.parseInt(id));
        riga.addView(usertext, 450, 100);
        usertext.setText(useramico);

        Button bAccetta = new Button(this);
        usertext.setId(Integer.parseInt(id));
        riga.addView(bAccetta);
        bAccetta.setText("Accetta");

        Button bRifiuta = new Button(this);
        usertext.setId(Integer.parseInt(id));
        riga.addView(bRifiuta);
        bRifiuta.setText("Rifiuta");

        ll.addView(riga);

        System.out.println(useramico);
    }


    private void makeToast(String text) {
        Toast.makeText(GameActivity.this, text, Toast.LENGTH_SHORT).show();
    }

}
