package kr.ac.sejong.myapplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    private MediaPlayer player;

    public MyService() {
    }

    @Override
    public void onCreate(){

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int started){
        //1초마다 "second_tick"방송을 하겠다.
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //"second_tick"을 방송하는 방법
                Intent intent = new Intent("second_tick");
                sendBroadcast(intent);
            }
        }, 0, 1000); //즉시 1초마다

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
