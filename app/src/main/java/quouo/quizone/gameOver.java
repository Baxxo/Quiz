package quouo.quizone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class gameOver extends AppCompatActivity {
    TextView vintoperso;
    RatingBar ratingBar;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_over);

        final int punteggio = getIntent().getExtras().getInt("Punteggio");
        int nemicoPunt = getIntent().getExtras().getInt("avversarioPunt");
        vintoperso = (TextView) findViewById(R.id.textView16);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(punteggio);
        ratingBar.setIsIndicator(true);

        if (nemicoPunt == -1) {
            vintoperso.setText("Attesa dell'aversario");
        } else {
            if (nemicoPunt > punteggio) {
                vintoperso.setText("Hai perso (" + nemicoPunt + " : " + punteggio + ")");
            } else if (nemicoPunt < punteggio) {
                vintoperso.setText("Hai vinto (" + nemicoPunt + " : " + punteggio + ")");
            } else if (nemicoPunt == punteggio) {
                vintoperso.setText("Pareggio (" + nemicoPunt + " : " + punteggio + ")");
            } else {
                vintoperso.setText("C'e' qualcosa che non va");
            }
        }

        Button button = (Button) findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TornaNelMenuPrincipale();
            }
        });

        Button share = (Button) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (punteggio == 1) {
                    message = "Giocando a QuizOne ho fatto solo un punto\n" + "Scaricalo anche tu così possiamo gicare\nhttps://drive.google.com/open?id=0B9uigSlRi5RXVkROMDFNbHgyMTA";
                }
                if (punteggio > 1) {
                    message = "Giocando a QuizOne ho fatto " + punteggio + " punti!!!\n" + "Scaricalo anche tu così possiamo gicare\nhttps://drive.google.com/open?id=0B9uigSlRi5RXVkROMDFNbHgyMTA";
                    ;
                }
                if (punteggio < 1) {
                    message = "Brutta partita a QuizOne... 0 punti\n" + "Scaricalo anche tu così possiamo gicare\nhttps://drive.google.com/open?id=0B9uigSlRi5RXVkROMDFNbHgyMTA";
                }
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(share, "QuizOne"));
            }
        });

    }

    private void TornaNelMenuPrincipale() {
        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        TornaNelMenuPrincipale();
    }
}
