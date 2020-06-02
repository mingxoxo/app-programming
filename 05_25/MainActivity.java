package kr.ac.sejong.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private MyRunnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textview);

        r = new MyRunnable(tv, this);
        //new Thread(r).start();

        Timer timer = new Timer();
        timer.schedule(new MyTimerTask(tv, this), 5000, 3000);
    }
}
