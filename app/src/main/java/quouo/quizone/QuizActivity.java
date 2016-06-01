package quouo.quizone;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity implements ITimer {

    Domanda[] domande = new Domanda[5];
    Button[] buttons = new Button[4];
    TextView domanda;
    int punteggio = 0;
    TextView numeroDomanda;
    TextView nome1;
    TextView nome2;
    TextView timerText;
    boolean canPlay;
    int numdomanda = 0;

    Handler handler = new Handler();

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_quiz);

        ConnectionHandler connectionHandler = new ConnectionHandler();
        System.out.println("Id partita: " + getIntent().getStringExtra("idPartita"));
        domande = connectionHandler.domande(getIntent().getStringExtra("idPartita"));
        nome1 = (TextView)findViewById(R.id.nome1);
        nome2 = (TextView)findViewById(R.id.nome2);
        timerText = (TextView)findViewById(R.id.timerQuiz);

        numeroDomanda = (TextView)findViewById(R.id.numeroDomanda);
        domanda = (TextView)findViewById(R.id.Domanda);
        buttons[0] = (Button)findViewById(R.id.risposta1);
        buttons[1] = (Button)findViewById(R.id.risposta2);
        buttons[2] = (Button)findViewById(R.id.risposta3);
        buttons[3] = (Button)findViewById(R.id.risposta4);

        nome1.setText(Player.nome);
        nome2.setText(getIntent().getStringExtra("avversario"));
        ImpostaDomanda(numdomanda);
    }

    private void ImpostaDomanda(final int index){
        if(index > 4){
            if(!Functions.hasConnection(getApplicationContext())){
                NotConnectionDialog();
            } else {
                LoadFine();
            }
            return;
        }

        StartTimer(8);

        canPlay = true;
        domanda.setText(domande[index].getTesto());
        timerText.setTextColor(Color.BLACK);

        for (int i = 0; i < 4; i++){
            buttons[i].setText(domande[index].getRisposta(i).getTesto());

            ///
            ///
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
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                numdomanda++;
                                ImpostaDomanda(numdomanda);
                            }
                        }, 500);
                    }
                }
            });
        }

        numeroDomanda.setText((numdomanda + 1) + "/5");
    }

    private void StartTimer(int sec){
        if(timer != null)
            timer.stop = true;
        timer = new Timer(this, sec);
        timer.start();
    }

    private void NotConnectionDialog(){
        final Dialog d = new Dialog(this);
        d.setTitle("Non c'e' internet");
        d.setCancelable(false);
        d.setContentView(R.layout.dialog);
        d.show();

        Button b = (Button) d.findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Functions.hasConnection(getApplicationContext())){
                    LoadFine();
                } else {
                    Toast.makeText(getApplicationContext(), "Non c'e' internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void OnTimeChange(final int seconds) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(seconds == 3){
                    timerText.setTextColor(Color.RED);
                }
                timerText.setText(String.valueOf(seconds));
            }
        });
    }

    @Override
    public void OnTimeOver(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                numdomanda++;
                ImpostaDomanda(numdomanda);
            }
        });
    }

    public void onBackPressed() {
        Toast.makeText(QuizActivity.this,"Non puoi uscire", Toast.LENGTH_LONG);
        /*final Dialog d = new Dialog(this);
        d.setCancelable(true);
        d.setContentView(R.layout.esci);
        d.show();

        Button esci = (Button) d.findViewById(R.id.esci1);
        esci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                d.dismiss();
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
        return;*/
    }

    private void LoadFine(){
        ConnectionHandler con = new ConnectionHandler();
        String ret = con.InviaPunteggio(Player.id, getIntent().getStringExtra("idPartita"), punteggio);
        if(ret == "FAILED"){
            Toast.makeText(getApplicationContext(), "FAILED INVIA PUNTEGGIO", Toast.LENGTH_SHORT);
        }
        Intent intent = new Intent(getApplicationContext(), gameOver.class);
        System.out.println("Punteggio da passare" + punteggio);
        intent.putExtra("Punteggio", punteggio);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(timer != null)
            timer.stop = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timer != null)
            timer.stop = true;
    }
}
