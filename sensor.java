package kr.ac.sejong.myapplication;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)

public class MainActivity extends AppCompatActivity implements SensorEventListener2 {

    SensorManager manager;
    Button buttonStart;
    Button buttonStop;
    boolean isRunning;
    final String TAG = "SensorLog";
    FileWriter writer;
    FileWriter writer_on_off;

    private static final String FILE_HEADER = "USERID, TYPE, PARTITIONTIME, VALS";
    private static final String FILE_HEADER_ONOFF = "USERID, PARTITIONTIME, ISON";
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //출처: https://indra17.tistory.com/entry/Screen-OnOff-이벤트-받기 [피로에 살고 피로에 죽자]
    //https://brunch.co.kr/@mystoryg/48

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("SmartPortal", "ScreenOnReceiver, onReceive:" + action);
            if (action.equals(Intent.ACTION_SCREEN_ON)) {
                try {
                    writer_on_off.write("user-a");
                    writer_on_off.append(",");
                    writer_on_off.append(format1.format(System.currentTimeMillis()));
                    writer_on_off.append(",");
                    writer_on_off.append("1");
                    Log.d(TAG, "Writing to " + format1.format(System.currentTimeMillis()) + "1");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                try {
                    writer_on_off.write("user-a");
                    writer_on_off.append(",");
                    writer_on_off.append(format1.format(System.currentTimeMillis()));
                    writer_on_off.append(",");
                    writer_on_off.append("0");
                    Log.d(TAG, "Writing to " + format1.format(System.currentTimeMillis()) + "0");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isRunning = false;
        
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStart.setEnabled(false); //버튼 못눌리게
                buttonStop.setEnabled(true); //버튼 눌려지게

                Intent intent = new Intent();
                intent.setAction("example.test.broadcast");
                sendBroadcast(intent);

                Log.d(TAG, "Writing to " + getStorageDir()); //파일 경로 출력
                try {
                    writer = new FileWriter(new File(getStorageDir(), "sensor_" + format1.format(System.currentTimeMillis()) + ".csv")); //파일 저장하기 위해
                    writer.append(FILE_HEADER.toString());
                    writer.append("\n");

                    writer_on_off = new FileWriter(new File(getStorageDir(), "screen_OnOff_" + format1.format(System.currentTimeMillis()) + ".csv")); //파일 저장하기 위해
                    writer_on_off.append(FILE_HEADER_ONOFF.toString());
                    writer_on_off.append("\n");

                } catch (IOException e) {
                    e.printStackTrace();
                }

                manager.registerListener(MainActivity.this, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 0); //가속도
                //manager.registerListener(MainActivity.this, manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), 0);
                //manager.registerListener(MainActivity.this, manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED), 0);
                manager.registerListener(MainActivity.this, manager.getDefaultSensor(Sensor.TYPE_LIGHT), 0);


                isRunning = true;
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);
                isRunning = false;
                manager.flush(MainActivity.this);
                manager.unregisterListener(MainActivity.this);
                try {
                    writer.close();
                    writer_on_off.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(br, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }


    private String getStorageDir() { //파일 경로 받아오는 코드
        return Objects.requireNonNull(this.getExternalFilesDir(null)).getAbsolutePath();
        //  return "/storage/emulated/0/Android/data/com.iam360.sensorlog/";
    }

    @Override
    public void onFlushCompleted(Sensor sensor) {

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onSensorChanged(SensorEvent evt) {
        if (isRunning) {
            try {
                switch (evt.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        writer.write("user-a");
                        writer.append(",");
                        writer.append("ACC");
                        writer.append(",");
                        writer.append(format1.format(System.currentTimeMillis()));
                        writer.append(",");
                        writer.append(String.format("%f, %f, %f\n", evt.values[0], evt.values[1], evt.values[2]));
                        //Log.d(TAG, "Writing to " + format1.format(System.currentTimeMillis()) + String.format(" ACC; %f; %f; %f; %f; %f; %f\n", evt.values[0], evt.values[1], evt.values[2], 0.f, 0.f, 0.f)); //파일 경로 출력
                        break;
                    case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                        writer.write("user-a");
                        writer.append(",");
                        writer.append("GYRO_UN");
                        writer.append(",");
                        writer.append(format1.format(System.currentTimeMillis()));
                        writer.append(",");
                        writer.append(String.format("%f, %f, %f, %f, %f, %f\n", evt.values[0], evt.values[1], evt.values[2], evt.values[3], evt.values[4], evt.values[5]));
                        break;
                    case Sensor.TYPE_GYROSCOPE:
                        writer.write("user-a");
                        writer.append(",");
                        writer.append("GYRO");
                        writer.append(",");
                        writer.append(format1.format(System.currentTimeMillis()));
                        writer.append(",");
                        writer.append(String.format("%f, %f, %f, %f, %f, %f\n", evt.values[0], evt.values[1], evt.values[2], 0.f, 0.f, 0.f));
                        break;
                    case Sensor.TYPE_LIGHT:
                        writer.write("user-a");
                        writer.append(",");
                        writer.append("LIGHT");
                        writer.append(",");
                        writer.append(format1.format(System.currentTimeMillis()));
                        writer.append(",");
                        writer.append(String.format("%f\n", evt.values[0]));
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
