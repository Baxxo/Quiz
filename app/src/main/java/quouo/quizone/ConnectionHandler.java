package quouo.quizone;

import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

/**
 * Created by bassomatteo on 21/05/2016.
 */
public class ConnectionHandler {

    BackgroundTask backgroundTask;

    public ConnectionHandler(){

    }

    public String CercaGiocatore(String nome){
        String ret = "FAILED";

        backgroundTask = new BackgroundTask();
        backgroundTask.SetParams(new String[] {nome});
        try {
            ret = backgroundTask.execute(Request.CERCAGIOCATORE).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public String CreaPartitaVs(int id, String enemyId){
        String ret = "FAILED";

        backgroundTask = new BackgroundTask();
        backgroundTask.SetParams(new String[] {String.valueOf(id), enemyId});

        try {
            ret = backgroundTask.execute(Request.CREAPARTITAVS).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Functions.Debug("VS: " + ret);
        return ret;
    }

    public String Login(String nome, String password) {
        backgroundTask = new BackgroundTask();
        String id = "FAILED";
        backgroundTask.SetParams(new String[] {nome, password});
        try {
            id = backgroundTask.execute(Request.LOGIN).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return id;
    }

    public String Registrazione(String nome, String password) {
        backgroundTask = new BackgroundTask();
        String ret = "FAILED";
        backgroundTask.SetParams(new String[] {nome, password});
        try {
            ret = backgroundTask.execute(Request.REGISTER).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public Richieste[] CaricaRichieste(int id){
        Richieste[] temp = new Richieste[0];

        backgroundTask = new BackgroundTask();
        backgroundTask.SetParams(new String[] {String.valueOf(id)});

        String[] s = new String[0];
        try {
            s = backgroundTask.execute(Request.CARICAPARTITE).get().split("<->");
            System.out.println(temp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        temp = new Richieste[s.length / 5];
        Functions.Debug("Richieste: " + (s.length / 5));
        Functions.Debug("Stringe: " + s.length);
        int index = 0;
        for(int i = 0; i < temp.length; i++){
            temp[i] = new Richieste(new String[] { s[index], s[index+1], s[index+2], s[index+3], s[index+4] });
            Functions.Debug(temp[i].toString());
            index += 5;
        }
        Functions.Debug("Caricate richieste: " + temp.length);
        return temp;
    }

    public String CaricaPartite(int id){
        backgroundTask = new BackgroundTask();
        backgroundTask.SetParams(new String[] {String.valueOf(id)});

        String temp = "FAILED";

        try {
            temp = backgroundTask.execute(Request.CARICAPARTITE).get();
            System.out.println(temp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return temp;
    }

    public String CreaPartita(int id){
        backgroundTask = new BackgroundTask();
        backgroundTask.SetParams(new String[] {String.valueOf(id)});
        String ret = "FAILED";
        try {
            ret = backgroundTask.execute(Request.CREAPARTITA).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public String TerminaPartita(String idPartita, int idGiocatore){
        backgroundTask = new BackgroundTask();
        backgroundTask.SetParams(new String[]{ idPartita, String.valueOf(idGiocatore) });
        String ret = "FAILED";
        try {
            ret = backgroundTask.execute(Request.ANNULLAPARTITA).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public String InviaPunteggio(int idUser, String idPartita, int punteggio ){
        String ret = "FAILED";
        backgroundTask = new BackgroundTask();
        backgroundTask.SetParams(new String[]{ idPartita, String.valueOf(punteggio), String.valueOf(idUser) });

        try {
            ret = backgroundTask.execute(Request.INVIAPUNTEGGIO).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public Domanda[] domande(String idPartita){
        backgroundTask = new BackgroundTask();
        Domanda[] domande = new Domanda[5];
        String[] ids = new String[5];
        System.out.println("Carico le domande della partita " + idPartita);

        backgroundTask.SetParams(new String[] { idPartita });

        try {
            ids = backgroundTask.execute(Request.CARICADOMANDE).get().split("<->");
            for (int i = 0; i < 5; i++){
                System.out.println(ids[i]);
                backgroundTask = new BackgroundTask();
                backgroundTask.SetParams(new String[] { ids[i] });
                domande[i] = new Domanda(backgroundTask.execute(Request.CARICADOMANDA).get());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return domande;
    }
}
