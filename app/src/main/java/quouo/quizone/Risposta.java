package quouo.quizone;

/**
 * Created by Oleksandr on 24/05/2016.
 */
public class Risposta {

    private String testo;
    private boolean giusto;

    public Risposta(String testo, boolean giusto) {
        this.testo = testo;
        this.giusto = giusto;
    }

    public String getTesto() {
        return testo;
    }

    public boolean isGiusto() {
        return giusto;
    }
}
