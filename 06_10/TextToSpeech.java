package kr.ac.sejong.speechapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText ttsEditText;
    private Button ttsButton;
    private TextToSpeech ttsEngine;
    private TextView srText;
    private TextView srSysMsg;
    private Button srButton;
    private SpeechRecognizer myRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ttsEditText = findViewById(R.id.ttseditText);
        ttsButton = findViewById(R.id.ttsbutton);
        srButton = findViewById(R.id.srButton);
        srText = findViewById(R.id.srText);
        srSysMsg = findViewById(R.id.srSysMsg);

        //1. TextToSpeech class를 인스턴스화 한다.
        ttsEngine = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    ttsEngine.setLanguage(Locale.KOREAN);
                }
            }
        });
        ttsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ttsEngine.setPitch(1.0f);
                ttsEngine.setSpeechRate(1.0f);
                Editable editable = ttsEditText.getText();
                ttsEngine.speak(editable,TextToSpeech.QUEUE_ADD,null,"1");
            }
        });
