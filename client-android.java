package kr.ac.sejong.serverapplication;

import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


//https://copycoding.tistory.com/240

public class MainActivity extends AppCompatActivity {

    private Handler mHandler;
    Socket socket;

    private String ip = "172.16.54.1"; // IP
    private int port = 9999; // PORT번호

    EditText et;
    TextView msgTV;

    @Override
    protected void onStop() {
        super.onStop();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();

        et = (EditText)findViewById(R.id.EditText01);
        Button btn = (Button)findViewById(R.id.Button01);
        Button btnCon = (Button)findViewById(R.id.Button02);
        final TextView tv = (TextView)findViewById(R.id.TextView01);
        msgTV = (TextView)findViewById(R.id.chatTV);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (et.getText().toString() != null || !et.getText().toString().equals("")) {
                    ConnectThread th = new ConnectThread();
                    th.start();
                }
            }
        });
    }

    class ConnectThread extends Thread {
        public void run() {
            try {
                //소켓 생성
                InetAddress serverAddr = InetAddress.getByName(ip);
                socket = new Socket(serverAddr, port);

                //입력 메세지
                String sndMsg = et.getText().toString();
                Log.d("=======", sndMsg);

                //데이터 전송
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out.println(sndMsg);

                //데이터 수신
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String read = input.readLine();

                //화면 출력
                mHandler.post(new msgUpdate(read));
                Log.d("=======", read);
                socket.close();

            } catch (UnknownHostException e) {
                    e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //받은 메세지 출력
    class msgUpdate implements  Runnable{
        private String msg;
        public msgUpdate(String str){
            this.msg = str;
        }
        @Override
        public void run() {
            msgTV.setText(msgTV.getText().toString() + msg + "\n");
        }
    };
}


