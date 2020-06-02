package kr.ac.sejong.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textview);

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                //Background Task가 보낸 메세지를 처리하는 곳
                int updateVal = msg.arg1;
                tv.setText(String.valueOf(updateVal));
            }
        };

        //Timer -> Handler -> Main Thread
        Timer timer = new Timer();
        timer.schedule(new MyTimerTask(tv, handler), 3000, 2000);
    }
}
