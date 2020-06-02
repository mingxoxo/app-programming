package kr.ac.sejong.myapplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MyService extends Service {

    private MediaPlayer player;

    public MyService() {
    }

    @Override
    public void onCreate(){
        //서비스가 생성이 될 때 호출
        player = MediaPlayer.create(this, R.raw.nanana);
        player.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int started){
        //서비스가 시작될 때 호출
        player.start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
