package quouo.quizone;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class BackgroundTask extends AsyncTask<Request, Void, String> {
    URL url;
    String[] parametres;

    public void SetParams(String[] parametres){
        this.parametres = parametres;
    }

    @Override
    protected String doInBackground(Request... params) {
        System.out.println("Inizio connessione");
        String result = "FAILED";
        Uri.Builder builder = new Uri.Builder();
        switch (params[0]){
            case LOGIN:
                System.out.println( parametres[0] + " " + parametres[1]);
                builder.scheme("http").authority("quiz1.altervista.org")
                        .appendPath("database.php")
                        .appendQueryParameter("richiesta", "login")
                        .appendQueryParameter("username", parametres[0])
                        .appendQueryParameter("password", parametres[1]);
                break;
            case REGISTER:
                builder.scheme("http").authority("quiz1.altervista.org")
                        .appendPath("database.php")
                        .appendQueryParameter("richiesta", "registrazione")
                        .appendQueryParameter("username", parametres[0])
                        .appendQueryParameter("password", parametres[1]);
                break;
            case CARICADOMANDA:
                builder.scheme("http").authority("quiz1.altervista.org")
                        .appendPath("database.php")
                        .appendQueryParameter("richiesta", "caricaDomanda")
                        .appendQueryParameter("idDomanda", parametres[0]);
                break;
            case CARICADOMANDE:
                builder.scheme("http").authority("quiz1.altervista.org")
                        .appendPath("database.php")
                        .appendQueryParameter("richiesta", "caricaDomandePartita")
                        .appendQueryParameter("id", parametres[0]);
                break;
            case CREAPARTITA:
                builder.scheme("http").authority("quiz1.altervista.org")
                        .appendPath("database.php")
                        .appendQueryParameter("richiesta", "creaPartita")
                        .appendQueryParameter("id1", parametres[0]);
                break;
            case CARICAPARTITE:
                builder.scheme("http").authority("quiz1.altervista.org")
                        .appendPath("database.php")
                        .appendQueryParameter("richiesta", "caricaPartite")
                        .appendQueryParameter("idPartita", parametres[0]);
                break;
            case ANNULLAPARTITA:
                builder.scheme("http").authority("quiz1.altervista.org")
                        .appendPath("database.php")
                        .appendQueryParameter("richiesta", "terminaPartita")
                        .appendQueryParameter("idP", parametres[0])
                        .appendQueryParameter("nomeUser", parametres[1]);
                break;
        }

        try {
            url = new URL(builder.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                Log.e("Errore", "Database Vuoto");
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            buffer.append(reader.readLine());
            result = buffer.toString();
            System.out.println("Buffer: " + buffer.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Ritorno: " + result);
        return result;
    }

    @Override
    protected void onPostExecute(String retValue) {
        super.onPostExecute(retValue);
    }
}
