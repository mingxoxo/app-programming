package kr.ac.sejong.uiapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import bsh.Interpreter;

public class MainActivity extends AppCompatActivity {

    private  Button button;
    private  EditText Editid;
    private  EditText Editname;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Activity Main 이라는 Layout을 화면에 배치한다.
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        Editid = findViewById(R.id.id);
        Editname = findViewById(R.id.name);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //명시적인 intent를 사용해서 Main2Activity를 호출
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                int id = Integer.parseInt(Editid.getText().toString());
                String name = Editname.getText().toString();
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }
}
