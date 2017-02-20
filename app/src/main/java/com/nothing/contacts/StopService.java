package com.nothing.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StopService extends AppCompatActivity {
    Button stopServiceButton;
    TextView tvStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_service);

        tvStop = (TextView) findViewById(R.id.tvStop);
        tvStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unsetFlag();
                Toast.makeText(StopService.this,"Alert stopped", Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void StopServiceMethod(View view){
        /*stopServiceButton = (Button)findViewById(R.id.stopServiceButton);
        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(StopService.this, shake_check.class);
                //stopService(intent);
                unsetFlag();
            }
        });*/
        unsetFlag();
        Toast.makeText(this,"Alert stopped", Toast.LENGTH_SHORT).show();
    }

    public static void unsetFlag() {
        shake_check.flag = 0;
    }

}

