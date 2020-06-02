package kr.ac.sejong.myapplication;

import android.os.Message;
import android.widget.TextView;
import java.util.TimerTask;
import android.os.Handler;

public class MyTimerTask extends TimerTask {

    private TextView tv;
    private Handler handler;

    public MyTimerTask(TextView tv, Handler handler){
        this.tv = tv;
        this.handler = handler;
    }

    @Override
    public void run() {
        //TextView를 가져와서 그 안의 값을 1 증가시키고
        //TextView에 증가된 값을 갱신
        //Handler를 통해서 그 값을 전달함으로써 갱신
        //현재의 TextView의 값을 정수형으로 갖고 온다.
        int curVal = Integer.parseInt(tv.getText().toString());
        curVal++;
        //갱신된 값을 handler를 통해 전달해 본다.
        Message message = handler.obtainMessage();
        message.arg1 = curVal;
        handler.sendMessage(message);
    }
}
