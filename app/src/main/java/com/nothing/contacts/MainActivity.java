package com.nothing.contacts;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static int flag=0;
    int plus;
    private Toolbar toolbar;
    private Button messageButton, buttooon, shakeButton;
    private TextView shakeClick, messageClick, contactsClick;
    private List<ContactDetails> conts;
    public static Intent intent;
    shake_check ob = new shake_check();
    private LocationListener locListener = ob.new MyLocationListener();
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;
    Boolean getLoc = false;
    LocationManager locManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        intent = new Intent(MainActivity.this, shake_check.class);
        shakeButton = (Button) findViewById(R.id.shakeButton);
        shakeClick = (TextView) findViewById(R.id.tv1);
        messageClick = (TextView) findViewById(R.id.tv2);
        contactsClick = (TextView) findViewById(R.id.tv3);
        messageButton = (Button) findViewById(R.id.messageButton);
        buttooon = (Button) findViewById(R.id.buttooon);

        check();
        if (isMyServiceRunning(shake_check.class)) {
            stop();
            shakeButton.setBackgroundResource(R.drawable.shaker);
        }

        conts = ContactsLab.get(getApplicationContext()).getContacts();

        messageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(shake_check.class)) {
                    sendSM();
                } else {
                    sendSMS();
                }
            }
        });
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(shake_check.class)) {
                    sendSM();
                } else {
                    sendSMS();
                }
            }
        });


        contactsClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(MainActivity.this, ContactsActivity.class);
                startActivity(inte);
            }
        });
        buttooon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(MainActivity.this, ContactsActivity.class);
                startActivity(inte);
            }
        });

        shakeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
                if(flag==0){
                    startService(intent);
                    stop();
                    shakeButton.setBackgroundResource(R.drawable.shaker);
                    Toast.makeText(getApplicationContext(),"Shake mode is ON",Toast.LENGTH_SHORT).show();
                } else {
                    stopService(intent);
                    start();
                    Toast.makeText(getApplicationContext(),"Shake mode is OFF",Toast.LENGTH_SHORT).show();
                    shakeButton.setBackgroundResource(R.drawable.shakeg);
                }
            }
        });
        shakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
                if(flag==0){
                    startService(intent);
                    stop();
                    shakeButton.setBackgroundResource(R.drawable.shaker);
                    Toast.makeText(getApplicationContext(),"Shake mode is ON",Toast.LENGTH_SHORT).show();
                } else {
                    stopService(intent);
                    start();
                    Toast.makeText(getApplicationContext(),"Shake mode is OFF",Toast.LENGTH_SHORT).show();
                    shakeButton.setBackgroundResource(R.drawable.shakeg);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
            }
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }
        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        showLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about) {
            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
            startActivity(intent);
            save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void save() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("key", plus = 0);
        editor.commit();
    }

    public void start() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("flag", flag = 0);
        editor.commit();
    }

    public void stop() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("flag", flag = 1);
        editor.commit();
    }

    public static int getFlag(){
        //new MainActivity().check();
        return flag;
    }

    public void check() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        flag = sharedPreferences.getInt("flag", flag);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo servic : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(servic.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        conts = ContactsLab.get(getApplicationContext()).getContacts();
    }

    public void sendSMS() {
        String number;
        String message = "I'm in danger, HELP!!";
        SmsManager smsManager = SmsManager.getDefault();
        int cou = 0;

        for (ContactDetails cont : conts) {
            cou = 1;
            number = cont.getNumber();
            try {
                smsManager.sendTextMessage(number, null, message, null, null);
                Toast.makeText(getApplicationContext(), "Message Sent!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Message sending failed!", Toast.LENGTH_SHORT).show();
            }
        }
        if(cou == 0){
            Toast.makeText(getApplicationContext(), "No added contact!!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void sendSM() {
        shake_check.sendSMS();
    }


    //Loaction Stuff
    public void showLocation() {

        try {
            getLoc = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            throw e;
        }

        if (getLoc) {
            if (Build.VERSION.SDK_INT >= 23) {
                if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

                } else if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) == PackageManager.PERMISSION_GRANTED)
                    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, locListener);

            } else
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, locListener);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0 && ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) == PackageManager.PERMISSION_GRANTED))
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, locListener);

        if (requestCode == 1 && (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED))
            ;


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}
