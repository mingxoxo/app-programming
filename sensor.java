package kr.ac.sejong.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
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
    private static final String FILE_HEADER = "USERID, TYPE, PARTITIONTIME, VALS";
    SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");

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

                Log.d(TAG, "Writing to " + getStorageDir()); //파일 경로 출력
                try {
                    writer = new FileWriter(new File(getStorageDir(), "sensors_" + format1.format(System.currentTimeMillis()) + ".csv")); //파일 저장하기 위해
                    writer.append(FILE_HEADER.toString());
                    writer.append("\n");
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
        if(isRunning) {
            try {
                switch(evt.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        writer.write("user-a");
                        writer.append(",");
                        writer.append("ACC");
                        writer.append(",");
                        writer.append(format1.format(System.currentTimeMillis()));
                        writer.append(",");
                        writer.append(String.format("%f, %f, %f\n", evt.values[0], evt.values[1], evt.values[2]));
                        Log.d(TAG, "Writing to " + format1.format(System.currentTimeMillis()) + String.format(" ACC; %f; %f; %f; %f; %f; %f\n", evt.values[0], evt.values[1], evt.values[2], 0.f, 0.f, 0.f)); //파일 경로 출력
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
