package quouo.quizone;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    Domanda[] domande = new Domanda[5];
    Button[] buttons = new Button[4];
    TextView domanda;
    int punteggio = 0;
    TextView numeroDomanda;
    TextView nome1;
    TextView nome2;
    boolean canPlay;
    int numdomanda = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        ConnectionHandler connectionHandler = new ConnectionHandler();
        System.out.println("Id partita: " + getIntent().getStringExtra("idPartita"));
        domande = connectionHandler.domande(getIntent().getStringExtra("idPartita"));
        nome1 = (TextView)findViewById(R.id.nome1);
        nome2 = (TextView)findViewById(R.id.nome2);

        numeroDomanda = (TextView)findViewById(R.id.numeroDomanda);
        domanda = (TextView)findViewById(R.id.Domanda);
        buttons[0] = (Button)findViewById(R.id.risposta1);
        buttons[1] = (Button)findViewById(R.id.risposta2);
        buttons[2] = (Button)findViewById(R.id.risposta3);
        buttons[3] = (Button)findViewById(R.id.risposta4);

        nome1.setText(Player.nome);
        nome2.setText("avversario");
        ImpostaDomanda(0);
    }

    private void ImpostaDomanda(final int index){
        if(index > 4){
            Intent intent = new Intent(getApplicationContext(), gameOver.class);
            intent.putExtra("Punteggio", punteggio);
            startActivity(intent);
            finish();
            return;
        }

        canPlay = true;
        domanda.setText(domande[index].getTesto());

        for (int i = 0; i < 4; i++){
            buttons[i].setText(domande[index].getRisposta(i).getTesto());
            buttons[i].setBackgroundColor(Color.WHITE);
            final int temp = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(canPlay) {
                        canPlay = false;
                        if (domande[index].getRisposta(temp).isGiusto()) {
                            punteggio++;
                            buttons[temp].setBackgroundColor(Color.GREEN);
                        } else {
                            buttons[temp].setBackgroundColor(Color.RED);
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ImpostaDomanda(index + 1);
                            }
                        }, 1000);
                    }
                }
            });
        }

        numeroDomanda.setText(numdomanda + "/5");
        numdomanda++;
    }


    private void makeToast(String text) {
        Toast.makeText(QuizActivity.this, text, Toast.LENGTH_SHORT).show();
    }

}
