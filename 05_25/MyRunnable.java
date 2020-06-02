package kr.ac.sejong.myapplication;

import android.app.Activity;
import android.widget.TextView;

public class MyRunnable implements Runnable {

    private TextView tv;
    private Activity mainActivity;

    public MyRunnable(TextView tv, Activity mainActivity){
        this.tv = tv;
        this.mainActivity = mainActivity;
    }

    @Override
    public void run(){
        while(true) {
            try{
                Thread.sleep(1000);
            }
            catch (Exception e) {

            }
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //1초씩 쉬면서 tv 위 값을 가져오고
                    String cur = tv.getText().toString();
                    int curVal = Integer.parseInt(cur);
                    curVal++;
                    tv.setText(String.valueOf(curVal));
                }
            });

        }
    }
}
