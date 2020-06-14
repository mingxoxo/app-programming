package kr.ac.sejong.sensorapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.hardware.Sensor.TYPE_ALL;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private SensorManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        //sensor의 list를 가지고 올 수 있다.
        final List<Sensor> sensorList= sm.getSensorList(TYPE_ALL);

        ArrayList<String> sensorStringList = new ArrayList<String>();

        for(Sensor sensor: sensorList){
            String sensorName = sensor.getName();
            sensorStringList.add(sensorName);
        }

        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,sensorStringList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sensor clickedSensor = sensorList.get(position);
                sm.registerListener(new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        //센서의 값을 Toast로 출력한다.
                        //"val1, val2, val3..."
                        float[] sensorValueArray = event.values;
                        String toastmessage = "";
                        for(float sensorvalue: sensorValueArray){
                            toastmessage += sensorvalue + ",";
                        }
                        Toast.makeText(MainActivity.this, toastmessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {

                    }
                }, clickedSensor, 3000); //3초 주기마다
            }
        });
    }
}
