package kr.ac.sejong.myapplication;

import android.app.Activity;
import android.widget.TextView;
import java.util.TimerTask;

public class MyTimerTask extends TimerTask {

    private TextView tv;
    private Activity mainActivity;

    public MyTimerTask(TextView tv, Activity mainActivity){
        this.tv = tv;
        this.mainActivity = mainActivity;
    }
    @Override
    public void run() {
//        mainActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                //1초씩 쉬면서 tv 위 값을 가져오고
//                //그것에 1을 추가에서 tv에 반영한다.
//                String cur = tv.getText().toString();
//                int curVal = Integer.parseInt(cur);
//                curVal++;
//                tv.setText(String.valueOf(curVal));
//            }
//        });
        tv.post(new Runnable() {
            @Override
            public void run() {
                //1초씩 쉬면서 tv 위 값을 가져오고
                //그것에 1을 추가에서 tv에 반영한다.
                String cur = tv.getText().toString();
                int curVal = Integer.parseInt(cur);
                curVal++;
                tv.setText(String.valueOf(curVal));
            }
        });
    }
}
