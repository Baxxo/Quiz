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
    TextView numeroDomanda;
    TextView nome1;
    TextView nome2;
    int numdomanda = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        ConnectionHandler connectionHandler = new ConnectionHandler();
        domande = connectionHandler.domande(3);
        nome1 = (TextView)findViewById(R.id.nome1);
        nome2 = (TextView)findViewById(R.id.nome2);

        numeroDomanda = (TextView)findViewById(R.id.numeroDomanda);
        domanda = (TextView)findViewById(R.id.Domanda);
        buttons[0] = (Button)findViewById(R.id.risposta1);
        buttons[1] = (Button)findViewById(R.id.risposta2);
        buttons[2] = (Button)findViewById(R.id.risposta3);
        buttons[3] = (Button)findViewById(R.id.risposta4);
        String nome = Player.nome;
        nome1.setText(nome);
        nome2.setText("avversario");
        ImpostaDomanda(0);
    }

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
		if(domande[index].getRisposta(0).isGiusto()){
		    punteggio ++;
                ImpostaDomanda(index + 1);
                buttons[0].setBackgroundColor(0x0000FF00);
                buttons[0].invalidate();
        }else{

        }
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
		if(domande[index].getRisposta(1).isGiusto()){
		    punteggio ++;
                ImpostaDomanda(index + 1);
                buttons[1].setBackgroundColor(0x0000FF00);
                buttons[1].invalidate();
        }else{

        }
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
		if(domande[index].getRisposta(2).isGiusto()){
		    punteggio ++;
                ImpostaDomanda(index + 1);
                buttons[2].setBackgroundColor(0x0000FF00);
                buttons[2].invalidate();
        }else{

        }
            }
        });

        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
		if(domande[index].getRisposta(3).isGiusto()){
		    punteggio ++;
                ImpostaDomanda(index + 1);
                buttons[3].setBackgroundColor(0x0000FF00);
                buttons[3].invalidate();
        }else{

        }
            }
        });

        numeroDomanda.setText(numdomanda + "/5");
        numdomanda++;
    }


    private void makeToast(String text) {
        Toast.makeText(QuizActivity.this, text, Toast.LENGTH_SHORT).show();
    }

}
