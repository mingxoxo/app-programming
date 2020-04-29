package kr.ac.sejong.uiapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Activity Main 이라는 Layout을 화면에 배치한다.
        //setContentView(R.layout.activity_main);

        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setStretchAllColumns(true);

        EditText nameEditText = new EditText(this);
        nameEditText.setHint("Name");

        Button button1 = new Button(this);  button1.setText("1");
        Button button2 = new Button(this);  button2.setText("2");
        Button button3 = new Button(this);  button3.setText("3");
        Button button4 = new Button(this);  button4.setText("4");
        Button button5 = new Button(this);  button5.setText("5");
        Button button6 = new Button(this);  button6.setText("6");
        Button button7 = new Button(this);  button7.setText("7");
        Button button8 = new Button(this);  button8.setText("8");
        Button button9 = new Button(this);  button9.setText("9");
        Button button10 = new Button(this); button10.setText("0");
        Button button11 = new Button(this); button11.setText("CLEAR");
        Button button12 = new Button(this); button12.setText("+");
        Button button13 = new Button(this); button13.setText(".");
        Button button14 = new Button(this); button14.setText("*");
        Button button15 = new Button(this); button15.setText("/");
        Button button16 = new Button(this); button16.setText("COMPUTE");


        TableRow tableRow1 = new TableRow(this);
        tableRow1.addView(nameEditText);


        TableRow tableRow2 = new TableRow(this);
        tableRow2.addView(button1);
        tableRow2.addView(button2);
        tableRow2.addView(button3);

        TableRow tableRow3 = new TableRow(this);
        tableRow3.addView(button4);
        tableRow3.addView(button5);
        tableRow3.addView(button6);

        TableRow tableRow4 = new TableRow(this);
        tableRow4.addView(button7);
        tableRow4.addView(button8);
        tableRow4.addView(button9);

        TableRow tableRow5 = new TableRow(this);
        tableRow5.addView(button10);
        tableRow5.addView(button11);


        TableRow tableRow6 = new TableRow(this);
        tableRow6.addView(button12);
        tableRow6.addView(button13);

        TableRow tableRow7 = new TableRow(this);
        tableRow7.addView(button14);
        tableRow7.addView(button15);

        TableRow tableRow8 = new TableRow(this);
        tableRow8.addView(button16);

        tableLayout.addView(tableRow1);
        tableLayout.addView(tableRow2);
        tableLayout.addView(tableRow3);
        tableLayout.addView(tableRow4);
        tableLayout.addView(tableRow5);
        tableLayout.addView(tableRow6);
        tableLayout.addView(tableRow7);
        tableLayout.addView(tableRow8);

        setContentView(tableLayout);
    }
}
