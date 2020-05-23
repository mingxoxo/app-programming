package kr.ac.sejong.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity { //상속한다.

    private TextView tv;

    //@RequiresApi(api = Build.VERSION_CODES.M)
    //자기가 원하는 행동을 Override(재정의)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //화면에 helloworld 세팅부분 -> activity_main을 화면에 배치한다.
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        registerForContextMenu(tv); //text를 long click했을때 menu가 뜨도록 등록

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);

        //방법 1
        menu.setHeaderTitle("context menu");
        menu.add(0, 1, 0, "배경색 : RED");
        menu.add(0, 2, 0, "배경색 : GREEN");
        menu.add(0, 3, 0, "배경색 : BLUE");

        //방법2
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.my_menu, menu);
    }
}
