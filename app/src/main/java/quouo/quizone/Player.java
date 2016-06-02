package quouo.quizone;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by bassomatteo on 26/05/2016.
 */
public class Player {
    public static int id;
    public static String nome;
    public static int partite;
    public static int pareggio;
    public static int vinte;
    public static int pareggiate;
    public static int perse;


    public void setPar(int v, int p, int l){
        vinte = v;
        pareggiate = p;
        perse = l;
    }
}
