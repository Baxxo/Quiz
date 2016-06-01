package quouo.quizone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

/**
 * Created by Matteo on 30/05/2016.
 */

public class SplashScreen extends Activity {
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setMax(1500);

        Thread timerThread = new Thread(){
            public void run(){
                for(i=0;i<1500;i+=10){
                    try{
                        progressBar.setProgress(i);
                        sleep(20);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
                finish();
            }
        };
        timerThread.start();
    }

}