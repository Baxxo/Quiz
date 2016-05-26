package quouo.quizone;

import java.util.concurrent.ExecutionException;

/**
 * Created by bassomatteo on 21/05/2016.
 */
public class ConnectionHandler {

    BackgroundTask backgroundTask;

    public ConnectionHandler(){

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

    public Domanda[] domande(int idPartita){
        backgroundTask = new BackgroundTask();
        Domanda[] domande = new Domanda[5];
        String[] ids = new String[5];

        backgroundTask.SetParams(new String[] {String.valueOf(idPartita)});

        try {
            ids = backgroundTask.execute(Request.CARICADOMANDE).get().split("<->");
            System.out.println();
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
