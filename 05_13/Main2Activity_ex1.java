package kr.ac.sejong.uiapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private TextView id2;
    private  TextView name2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        id2 = findViewById(R.id.id2);
        name2 = findViewById(R.id.name2);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        String name = intent.getStringExtra("name");

        id2.setText(String.valueOf(id));
        name2.setText(name);
    }
}
