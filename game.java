package kr.ac.sejong.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private int axeLevel = 1;
    private int numOfWood = 0;
    private int woodSellerEfficiency = 0;
    private  int numOfMoney = 0;

    private TextView numOfWoodText;
    private TextView numOfMoneyText;
    private ImageView axeImage;
    private Button cutDownButton;
    private Button upgradeAxe;
    private Button woodSeller;

    private Drawable drawable;

  @Override
  protected void onCreate(Bundle savedInstanceState){
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      numOfWoodText = findViewById(R.id.numOfWood);
      numOfMoneyText = findViewById(R.id.numOfMoney);
      axeImage = findViewById(R.id.axeImage);
      cutDownButton = findViewById(R.id.cutDownButton);
      upgradeAxe = findViewById(R.id.upgradeAxe);
      woodSeller = findViewById(R.id.woodSeller);



      cutDownButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              numOfWood += axeLevel;
              numOfWoodText.setText("#Wood: "+ numOfWood);
          }
      });
      upgradeAxe.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (50*axeLevel <= numOfWood){
                  numOfWood -= 50*axeLevel;
                  axeLevel++;
                  numOfWoodText.setText("#Wood: "+ numOfWood);

                  Resources res = getResources();
                  if (axeLevel == 2) {
                      drawable = ResourcesCompat.getDrawable(res, R.drawable.axe2, null);
                  }
                  else if (axeLevel == 3) {
                      drawable = ResourcesCompat.getDrawable(res, R.drawable.axe3, null);
                  }
                  else if (axeLevel == 4) {
                      drawable = ResourcesCompat.getDrawable(res, R.drawable.axe4, null);
                  }
                  else if (axeLevel == 5) {
                      drawable = ResourcesCompat.getDrawable(res, R.drawable.axe5, null);
                  }
                  else if (axeLevel == 6) {
                      drawable = ResourcesCompat.getDrawable(res, R.drawable.axe6, null);
                  }

                  axeImage.setImageDrawable(drawable);
              }else{
                  Toast.makeText(MainActivity.this, "Not Enough Wood", Toast.LENGTH_SHORT).show();
              }
          }
      });

      woodSeller.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(numOfWood >= 100){
                  numOfWood -= 100;
                  woodSellerEfficiency++;

                  // Timer가 시작된다.
                  Timer timer = new Timer();
                  timer.schedule(new TimerTask() {
                      @Override
                      public void run() {
                          if(numOfWood > 0){
                              numOfWood--;
                              numOfMoney += woodSellerEfficiency;

                              MainActivity.this.runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      numOfWoodText.setText("#Wood: " + numOfWood);
                                      numOfMoneyText.setText("#Money: " + numOfMoney);
                                  }
                              });
                          }
                      }
                  }, 0, 1000);
                  numOfWoodText.setText("#Wood: " + numOfWood);
                  woodSeller.setText("UPGRADE SELLER");
              }else{
                  Toast.makeText(MainActivity.this, "Not Enough Wood", Toast.LENGTH_SHORT).show();
              }
          }
      });
  }
}
