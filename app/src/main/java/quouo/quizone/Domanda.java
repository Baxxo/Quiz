package quouo.quizone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Oleksandr on 24/05/2016.
 */
public class Domanda {

    String testo;
    Risposta[] risposte = new Risposta[4];

    public Domanda(String domNonDivisa) {
        String[] temp = domNonDivisa.split("<->");
        testo = temp[0];
        risposte[0] = new Risposta(temp[1], true);
        risposte[1] = new Risposta(temp[2], false);
        risposte[2] = new Risposta(temp[3], false);
        risposte[3] = new Risposta(temp[4], false);
        RisposteRand();

    }

    public Domanda() {

    }

    public String getTesto() {
        return testo;
    }

    public Risposta getRisposta(int index) {
        if (index > 4)
            index = 3;
        return risposte[index];
    }

    private void RisposteRand() {
        int[] randArr = new int[] { 5, 5, 5, 5 } ;
        for (int i = 0; i < 4; i++) {
            randArr[i] = RandNum(randArr);
        }
        for (int i = 0; i < 4; i++) {
            Risposta temp = risposte[i];
            risposte[i] = risposte[randArr[i]];
            risposte[randArr[i]] = temp;
        }
    }

    private int RandNum(int[] arr) {
        Random r = new Random();
        int rand = r.nextInt(4);
        for (int i = 0; i < 4; i++) {
            if (rand == arr[i]) {
                RandNum(arr);
            }
        }
        return rand;
    }
}
