package kr.ac.sejong.speechapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
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

        //1. SpeechRecognizer를 준비한다.
       myRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
       myRecognizer.setRecognitionListener(new RecognitionListener() {
           @Override public void onReadyForSpeech(Bundle params) { srSysMsg.setText("[onReadyForSpeech]"); }
           @Override public void onBeginningOfSpeech() { srSysMsg.setText("[onBeginningOfSpeech]"); }
           @Override public void onRmsChanged(float rmsdB) { srSysMsg.setText("[onRmsChanged]"); }
           @Override public void onBufferReceived(byte[] buffer) { srSysMsg.setText("[onBufferReceived]"); }
           @Override public void onEndOfSpeech() { srSysMsg.setText("[onEndOfSpeech]"); }
           @Override public void onError(int error) { srSysMsg.setText("[onError]"); }

           @Override
           public void onResults(Bundle results) {
                //인식이 끝났을 때(가장 중요)

               //인식된 문자열의 arraylist를 줌
              ArrayList<String> mResult = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
              String str = "";
              for(String elem: mResult){
                  str += elem + " , ";
              }
              srText.setText(str);
              srSysMsg.setText("[onResults]");
           }
           @Override
           public void onPartialResults(Bundle partialResults) {
                //중간결과값
               srSysMsg.setText("[onPartialResults]");
           }
           @Override public void onEvent(int eventType, Bundle params) { srSysMsg.setText("[onEvent(]"); }
       });
        srButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //1. 권한 체크 INTERNET, RECORD AUDIO
                if (ContextCompat. checkSelfPermission (getApplicationContext(), Manifest.permission. RECORD_AUDIO ) != PackageManager. PERMISSION_GRANTED ||
                        ContextCompat. checkSelfPermission (getApplicationContext(), Manifest.permission. INTERNET ) != PackageManager. PERMISSION_GRANTED ) {
                    ActivityCompat. requestPermissions (MainActivity.this, new String[]{Manifest.permission. INTERNET , Manifest.permission. RECORD_AUDIO }, 0);
                }
                Intent srIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                srIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                srIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

                myRecognizer.startListening(srIntent);
            }
        });
    }
}
