package kr.ac.sejong.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity { //상속한다.

    //@RequiresApi(api = Build.VERSION_CODES.M)
    //자기가 원하는 행동을 Override(재정의)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //화면에 helloworld 세팅부분 -> activity_main을 화면에 배치한다.
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //my_menu.xml
        //menu인자에 내가 만들어놓은 menu를 받아오게 됨.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Toast message로 화면에 title을 출력한다.
        Toast.makeText(this, item.getTitle()+String.valueOf(item.getItemId()), Toast.LENGTH_SHORT).show();
        

        return true;
    }
}
