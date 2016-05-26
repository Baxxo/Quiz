package quouo.quizone;

import android.content.DialogInterface;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        ConnectionHandler connectionHandler = new ConnectionHandler();
        domande = connectionHandler.domande(3);

        domanda = (TextView)findViewById(R.id.Domanda);
        buttons[0] = (Button)findViewById(R.id.risposta1);
        buttons[1] = (Button)findViewById(R.id.risposta2);
        buttons[2] = (Button)findViewById(R.id.risposta3);
        buttons[3] = (Button)findViewById(R.id.risposta4);

        ImpostaDomanda(0);
    }

    //https://github.com/Baxxo/Quiz1.git

    private void ImpostaDomanda(final int index){
	if(index > 4){
        makeToast("Il tuo punteggio:  " + punteggio);
	    return;
	}
        domanda.setText(domande[index].getTesto());
        buttons[0].setText(domande[index].getRisposta(0).getTesto());
        buttons[1].setText(domande[index].getRisposta(1).getTesto());
        buttons[2].setText(domande[index].getRisposta(2).getTesto());
        buttons[3].setText(domande[index].getRisposta(3).getTesto());

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
		if(domande[index].getRisposta(0).isGiusto())
		    punteggio ++;
                ImpostaDomanda(index + 1);
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
		if(domande[index].getRisposta(1).isGiusto())
		    punteggio ++;
                ImpostaDomanda(index + 1);
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
		if(domande[index].getRisposta(2).isGiusto())
		    punteggio ++;
                ImpostaDomanda(index + 1);
            }
        });

        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
		if(domande[index].getRisposta(3).isGiusto())
		    punteggio ++;
                ImpostaDomanda(index + 1);
            }
        });
    }


    private void makeToast(String text) {
        Toast.makeText(QuizActivity.this, text, Toast.LENGTH_SHORT).show();
    }

}
