package quouo.quizone;

/**
 * Created by Oleksandr on 29/05/2016.
 */
public class Timer extends Thread {

    private ITimer activity;
    private int seconds;
    public boolean stop;

    public Timer(ITimer activity, int sec) {
        this.activity = activity;
        seconds = sec;
        stop = false;
    }

    public void run() {
        Functions.Debug("Timer seconds: " + seconds);
        activity.OnTimeChange(seconds);
        if (stop) return;
        while (seconds > 0) {
            try {
                if (stop) return;
                Thread.sleep(1000);
                if (stop) return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (stop) return;
            seconds--;
            Functions.Debug("Timer: " + seconds);
            if (stop) return;
            activity.OnTimeChange(seconds);
        }
        activity.OnTimeOver();
        return;

    }

}
