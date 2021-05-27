package kr.ac.sejong.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.Semaphore;

import static android.content.ContentValues.TAG;

@RequiresApi(api = Build.VERSION_CODES.P)

public class MainActivity extends AppCompatActivity implements SensorEventListener2 {

    public static Context mContext;
    SensorManager manager;
    Button buttonStart;
    Button buttonStop;
    Button audioStart;
    Button audioStop;
    boolean isRunning;
    //final String TAG = "SensorLog";
    FileWriter writer;
    FileWriter writer_on_off;
    MediaRecorder recorder; //오디오 녹음 https://ju-hy.tistory.com/76
    String filename;
    public static Semaphore sem;

    private static final String FILE_HEADER = "USERID, TYPE, PARTITIONTIME, VALS";
    private static final String FILE_HEADER_ONOFF = "USERID, PARTITIONTIME, ISON";
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //출처: https://indra17.tistory.com/entry/Screen-OnOff-이벤트-받기 [피로에 살고 피로에 죽자]

    private String ip = "172.16.54.1"; // IP
    private int port = 9999; // PORT번호
    Button sendButton;
    Socket socket = null;

    //https://copycoding.tistory.com/240 socket programming

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        sendButton = findViewById(R.id.Button01);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;

        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //오디오 코드
        String[] permissions = {Manifest.permission.RECORD_AUDIO};

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "오디오 녹음 수신 권한 주어져 있음", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "오디오 녹음 수신 권한 없음", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "오디오 녹음 권한 설명 필요함", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }

        File file = new File(getStorageDir(), "recorded.mp4");
        filename = file.getAbsolutePath();
        //여기까지

        isRunning = false;

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);
        audioStart = (Button) findViewById(R.id.audioStart);
        audioStop = (Button) findViewById(R.id.audioStop);
        final ScreenOnReceiver[] tmp = {null};
        sem = new Semaphore(1);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // 서버 연결
                    File[] files = new File(getStorageDir()).listFiles();
                    try {
                        for (int i = 0; i < files.length; i++) {
                            socket = new Socket(ip, port);
                            //System.out.println("서버에 연결되었습니다.");

                            //파일전송
                            String filePath = getStorageDir();
                            String fileNm = files[i].getName();
                            FileSender fs = new FileSender(socket, filePath, fileNm);
                            fs.start();

                            //fileNm = "recorded.mp4";
                            //FileSender fs3 = new FileSender(socket, filePath, fileNm);
                            //fs3.start();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStart.setEnabled(false); //버튼 못눌리게
                buttonStop.setEnabled(true); //버튼 눌려지게

                Log.d(TAG, "Writing to " + getStorageDir()); //파일 경로 출력
                try {
                    //writer = new FileWriter(new File(getStorageDir(), "sensor_" + format1.format(System.currentTimeMillis()) + ".csv")); //파일 저장하기 위해
                    writer = new FileWriter(new File(getStorageDir(), "sensor.csv"));
                    writer.append(FILE_HEADER.toString());
                    writer.append("\n");

                    writer_on_off = new FileWriter(new File(getStorageDir(), "screen_OnOff.csv")); //파일 저장하기 위해
                    writer_on_off.append(FILE_HEADER_ONOFF.toString());
                    writer_on_off.append("\n");


                } catch (IOException e) {
                    e.printStackTrace();
                }
                ScreenOnReceiver screenOnReceiver = new ScreenOnReceiver(writer_on_off);
                IntentFilter filter = new IntentFilter();
                filter.addAction("android.intent.action.SCREEN_ON");
                filter.addAction("android.intent.action.SCREEN_OFF");
                registerReceiver(screenOnReceiver, filter);

                tmp[0] = screenOnReceiver;

                manager.registerListener(MainActivity.this, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 0); //가속도
                manager.registerListener(MainActivity.this, manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), 0);
                manager.registerListener(MainActivity.this, manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED), 0);
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
                unregisterReceiver(tmp[0]);
            }

        });

        audioStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioStop.setEnabled(true);
                audioStart.setEnabled(false);
                recordAudio();
            }
        });

        audioStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioStop.setEnabled(false);
                audioStart.setEnabled(true);
                stopRecording();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "오디오 녹음 권한 동의함", Toast.LENGTH_SHORT).show();
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this, "오디오 녹음 권한 거부함", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "오디오 녹음 권한 획득 실패", Toast.LENGTH_SHORT).show();
                }

        }
    }


    public String getStorageDir() { //파일 경로 받아오는 코드
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
                        writer.append(String.format("%f, %f, %f\n", evt.values[0], evt.values[1], evt.values[2]));
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

    public void recordAudio() {
        Toast.makeText(this, "오디오 녹음 시작", Toast.LENGTH_SHORT).show();
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(filename);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
        Toast.makeText(this, "오디오 녹음 마침", Toast.LENGTH_SHORT).show();
    }
}

//출처: https://kylblog.tistory.com/10?category=774696 [ylblog]

class ScreenOnReceiver extends BroadcastReceiver {
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    FileWriter writer_on_off;

    public ScreenOnReceiver(FileWriter writer_on_off) {
        this.writer_on_off = writer_on_off;
    }

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
                writer_on_off.append("1\n");
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
                writer_on_off.append("0\n");
                Log.d(TAG, "Writing to " + format1.format(System.currentTimeMillis()) + "0");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
};

//파일 전송용 클래스
class FileSender extends Thread {

    String filePath;
    String fileNm;
    Socket socket;
    DataOutputStream dos;
    FileInputStream fis;
    BufferedInputStream bis;


    @RequiresApi(api = Build.VERSION_CODES.P)
    public FileSender(Socket socket, String filePath, String fileNm) throws InterruptedException {

        this.socket = socket;
        this.fileNm = fileNm;
        this.filePath = filePath;

        try {
            // 데이터 전송용 스트림 생성
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // @Override
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void run() {

        try {
            //파일전송을 서버에 알린다.('file' 구분자 전송)
            dos.writeUTF("file");
            dos.flush();

            //전송할 파일을 읽어서 Socket Server에 전송
            String result = fileRead(dos);
            System.out.println("result : " + result);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private String fileRead(DataOutputStream dos) {

        String result;

        try {
            MainActivity.sem.acquire();
            System.out.println("파일 전송 작업을 시작합니다.");

            dos.writeUTF(fileNm);
            System.out.println("파일 이름(" + fileNm + ")을 전송하였습니다.");

            // 파일을 읽어서 서버에 전송
            File file = new File(filePath + "/" + fileNm);
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);

            int len;
            int size = 4096;
            byte[] data = new byte[size];
            while ((len = bis.read(data)) != -1) {
                dos.write(data, 0, len);
            }

            //서버에 전송
            dos.flush();

            result = "SUCCESS";
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            result = "ERROR";
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MainActivity.sem.release();

        return result;
    }
}

