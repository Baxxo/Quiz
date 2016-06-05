package quouo.quizone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class gameOver extends AppCompatActivity {
    TextView vintoperso;
    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        int punteggio = getIntent().getExtras().getInt("Punteggio");
        int nemicoPunt = getIntent().getExtras().getInt("avversarioPunt");
        vintoperso = (TextView) findViewById(R.id.textView16);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(punteggio);
        ratingBar.setIsIndicator(true);

        if(nemicoPunt == -1) {
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

        Button button = (Button)findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TornaNelMenuPrincipale();
            }
        });

    }

    private void TornaNelMenuPrincipale(){
        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        TornaNelMenuPrincipale();
    }
}
