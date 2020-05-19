package kr.ac.sejong.uiapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import bsh.Interpreter;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button mapButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Activity Main 이라는 Layout을 화면에 배치한다.
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        mapButton = findViewById(R.id.mapButton);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //암시적으로 
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:35.2570,129.2185?z=10")); //action , uri
                startActivity(intent);
            }
        });
    }
}
