package kr.ac.sejong.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.Date;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState){
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      //서비스 시작시킨다.
      Intent intent = new Intent(MainActivity.this, MyService.class);
      startService(intent);

      //1. Broadcast Receiver를 만든다.
      BroadcastReceiver br = new BroadcastReceiver() {
          @Override
          public void onReceive(Context context, Intent intent) {
              //방송을 받았을 때 호출 됨
              TextView tv = findViewById(R.id.textview);
              tv.setText(new Date().toString());
          }
      };

      //2. Broadcast Receiver를 등록한다.
      //br은 ACTION_TIME_TICK 때마다 호출할 것이다.
      registerReceiver(br, new IntentFilter("second_tick"));
  }
}
