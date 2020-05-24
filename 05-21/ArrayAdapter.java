package kr.ac.sejong.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity { //상속한다.

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Data
        String[] data = {"A", "B", "C"};

        //UI
        listView = findViewById(R.id.listView);

        //Data(Array) <--- ArrayAdapter ---> UI
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        //(context로써 mainActivity, 어떠한 형식으로 담을것인가(템플릿같이),data)
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * @param parent listview
             * @param view AppCompatTextView ---> item
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //view를 이용
                AppCompatTextView tv = (AppCompatTextView)view;
                Toast.makeText(MainActivity.this, tv.getText().toString(), Toast.LENGTH_SHORT).show();

                //Listview ---> 직접 그 안의 요소를 가지고 오는 것
                //Object object = parent.getItemAtPosition(position);
                //System.out.printIn(object);
            }
        });
    }
}
