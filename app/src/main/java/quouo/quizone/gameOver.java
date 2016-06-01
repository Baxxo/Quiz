package quouo.quizone;

import android.app.Dialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class gameOver extends AppCompatActivity {

    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        int punteggio = getIntent().getExtras().getInt("Punteggio");

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(punteggio);
        ratingBar.setIsIndicator(true);

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
