package kr.ac.sejong.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity { //상속한다.

    private TextView tv;
    private  Button btn;
    private  Button DateBtn;

    //@RequiresApi(api = Build.VERSION_CODES.M)
    //자기가 원하는 행동을 Override(재정의)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //화면에 helloworld 세팅부분 -> activity_main을 화면에 배치한다.
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        btn = findViewById(R.id.button);
        DateBtn = findViewById(R.id.button2);

        DateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog picker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tv.setText(year + "-" + month + "-" + dayOfMonth);
                    }
                }, 2000, 10, 13);
                picker.show();
            }
        });

}
