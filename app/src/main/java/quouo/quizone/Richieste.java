package quouo.quizone;

import android.widget.TableRow;

/**
 * Created by Oleksandr on 30/05/2016.
 */
public class Richieste {

    StatoRichiesta stato;
    String idRichiesta;
    String player1;
    String player2;
    int punt1;
    int punt2;

    public Richieste(String[] temp){
        idRichiesta = temp[0];
        player1 = temp[1];
        player2 = temp[3];
        punt1 = Integer.valueOf(temp[2]);
        punt2 = Integer.valueOf(temp[4]);

        if(punt1 == -1 && punt2 == -1){
            stato = StatoRichiesta.DAFARE;
        } else if(punt2 > -1 && punt1 > -1){
            stato = StatoRichiesta.FINITA;
        } else {
            if(Player.nome.equals(player1)){
                if(punt1 > -1)
                    stato = StatoRichiesta.ASPETTA;
                else
                    stato = StatoRichiesta.DAFARE;
            }
            if(Player.nome.equals(player2)){
                if(punt2 > -1)
                    stato = StatoRichiesta.ASPETTA;
                else
                    stato = StatoRichiesta.DAFARE;
            }
            else{
                stato = StatoRichiesta.NONVALIDA;
            }
        }
        Functions.Debug("Creo la richiesta: " + stato);
    }

    public String getNemico(){
        if(Player.nome.equals(player1))
            return player2;
        if(Player.nome.equals(player2))
            return player1;
        else
            return "Bah";
    }

    public String getIdRichiesta() {
        return idRichiesta;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public int getPunt1() {
        return punt1;
    }

    public int getPunt2() {
        return punt2;
    }

    @Override
    public String toString() {
        return "Richieste{" +
                "stato=" + stato +
                ", idRichiesta='" + idRichiesta + '\'' +
                ", player1='" + player1 + '\'' +
                ", player2='" + player2 + '\'' +
                ", punt1=" + punt1 +
                ", punt2=" + punt2 +
                '}';
    }
}
