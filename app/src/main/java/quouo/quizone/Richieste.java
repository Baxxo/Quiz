package quouo.quizone;

import android.widget.TableRow;

/**
 * Created by Oleksandr on 30/05/2016.
 */
public class Richieste {

    StatoRichiesta stato;
    String idRichiesta;
    String nemicoNome;
    int myPunt;
    int nemicoPunt;

    public Richieste(String[] temp){
        idRichiesta = temp[0];
        String player1 = temp[1];
        String player2 = temp[3];
        int p1 = Integer.valueOf(temp[2]);
        int p2 = Integer.valueOf(temp[4]);

        if(Player.nome.equals(player1)){
            myPunt = p1;
            nemicoPunt = p2;
            nemicoNome = player2;
        }
        if(Player.nome.equals(player2)){
            myPunt = p2;
            nemicoPunt = p1;
            nemicoNome = player1;
        }

        if(myPunt == -1){
            stato = StatoRichiesta.DAFARE;
        } else if(myPunt > -1 && nemicoPunt == -1){
            stato = StatoRichiesta.ASPETTA;
        } else if(myPunt > -1 && nemicoPunt > -1){
            stato = StatoRichiesta.FINITA;
        } else if(myPunt == -2 || nemicoPunt == -2){
            stato = StatoRichiesta.RIFIUTATA;
        } else {
            stato = StatoRichiesta.NONVALIDA;
        }
    }

    public String getNemico(){
        return nemicoNome;
    }

    public String getIdRichiesta() {
        return idRichiesta;
    }

    public int getMyPunt() {
        return myPunt;
    }

    public int getNemicoPunt() {
        return nemicoPunt;
    }

    public String risultato(){
        if(myPunt > nemicoPunt)
            return "Vinto";
        if(myPunt < nemicoPunt)
            return "Perso";
        if(myPunt == nemicoPunt)
            return "Parita";
        else
            return "C'e' qualcosa che non va";
    }
}
