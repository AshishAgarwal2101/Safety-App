package com.nothing.contacts;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.List;

public class shake_check extends Service implements SensorEventListener {
    private SensorManager sm;
    private Sensor acc;
    static Context context;
    static String locationAddress="Unknown";
    Boolean getLoc;
    public static int count = 0, flag=0;
    static float x = 0.0f, y = 0.0f, z = 0.0f;
    private static long time1, time2, time3, tim1, tim2;
    LocationManager locManager;
    LocationListener locListener= new MyLocationListener();
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;

    private static List<ContactDetails> conts;

    public shake_check() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Intent en= getIntent();
        //Toast.makeText(this, "In onCreate shake_check", Toast.LENGTH_SHORT).show();
        //startService(i);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        setContext();


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Toast.makeText(this,"In onstart command",Toast.LENGTH_SHORT).show();
        sm.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        try {
            getLoc = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            throw e;
        }

        if (getLoc) {
            if (Build.VERSION.SDK_INT >= 23) {
                if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) == PackageManager.PERMISSION_GRANTED)
                    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, locListener);

            }
            else
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, locListener);

        }
        return START_STICKY;   //super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        conts = ContactsLab.get(getApplicationContext()).getContacts();

        float x1, y1, z1, lax, lay, laz, lax1, lay1, laz1, max1, max;
        //low pass filter using alpha=0.8 0.2=1-0.8
        x1 = (float) (0.8 * x + 0.2 * event.values[0]);
        y1 = (float) (0.8 * y + 0.2 * event.values[1]);
        z1 = (float) (0.8 * z + 0.2 * event.values[2]);
        //high pass filter

        lax1 = x1 - event.values[0];
        lay1 = y1 - event.values[1];
        laz1 = z1 - event.values[2];
        lax = x - event.values[0];
        lay = y - event.values[1];
        laz = z - event.values[2];
        float lx1 = Math.abs(lax1);
        float ly1 = Math.abs(lay1);
        float lz1 = Math.abs(laz1);
        float lz = Math.abs(laz);
        float ly = Math.abs(lay);
        float lx = Math.abs(lax);
        max1 = (lx1 > ly1) ? ((lx1 > lz1) ? lx1 : lz1) : (ly1 > lz1 ? ly1 : lz1);
        max = (lx > ly) ? ((lx > lz) ? lx : lz) : (ly > lz ? ly : lz);
        x = x1;
        y = y1;
        z = z1;


        if ((Math.abs(max1 - max)) >= 4.5) {
            ++count;
            if (count == 1)
                time1 = System.currentTimeMillis();
            if (count == 2)
                time2 = System.currentTimeMillis();
            if (count == 3)
                time3 = System.currentTimeMillis();
        }

        if ((count == 3) && ((time3 - time1) <= 6000)) {
            count = 0;
            x = 0.0f;
            y = 0.0f;
            z = 0.0f;

            Toast.makeText(this, "Shook 3 times", Toast.LENGTH_LONG).show();
            //Generating a notification
            tim1 = System.currentTimeMillis();
            flag=1;
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent in = new Intent(this, StopService.class);
            PendingIntent pi = PendingIntent.getActivity(this, 0, in, 0);
            NotificationCompat.Builder b = new NotificationCompat.Builder(this);
            b.setSmallIcon(R.drawable.ic_launcher);
            b.setContentTitle("3 Shakes detected! Service started");
            b.setContentText("Click to go to the app and stop it");
            b.setAutoCancel(true);
            b.setOngoing(true);
            b.setContentIntent(pi);

            nm.notify(1243, b.build());

        }
        tim2 = System.currentTimeMillis();
        if((tim2-tim1)>=10000 && flag==1) {
            flag=0;
            sendSMS();
        }
    }

    public void setContext(){
        context=getApplicationContext();
    }
    public static void sendSMS(){
        String number;
        String message = "I'm in danger, HELP! Location: "+locationAddress;
        SmsManager smsManager = SmsManager.getDefault();
        int cou = 0;

        for(ContactDetails cont:conts){
            cou = 1;
            number = cont.getNumber();
            try{
                smsManager.sendTextMessage(number, null, message, null, null);

                Toast.makeText(context, "Message Sent!", Toast.LENGTH_SHORT).show();
            }
            catch(Exception e){
                Toast.makeText(context, "Message sending failed!", Toast.LENGTH_SHORT).show();
            }
        }

        if(cou == 0){
            Toast.makeText(context, "No added contact!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {

                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                LocationAddress.getAddressFromLocation(latitude, longitude, context, new GeocoderHandler());

            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }


        private class GeocoderHandler extends Handler {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        Bundle bundle = message.getData();
                        locationAddress = bundle.getString("address");
                        break;
                    default:
                        locationAddress = "Unable To Fetch Location";
                }
                //getActionBar().setTitle(locationAddress);

            }

        }

    }
}