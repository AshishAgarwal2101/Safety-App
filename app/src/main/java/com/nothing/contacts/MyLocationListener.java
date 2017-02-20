package com.nothing.contacts;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;


public class MyLocationListener extends Service implements LocationListener {
    public String locationAddress;

    Context context = this;

    public String getLocAddress(){
        return locationAddress;
    }
    public MyLocationListener(Context ci){
        context=ci;
    }
    public MyLocationListener(){}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            // This needs to stop getting the location data and save the battery power.
               /* if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

                    }
                }
                locManager.removeUpdates(locListener);
                */


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

            //  Toast.makeText(getApplicationContext(),locationAddress,Toast.LENGTH_SHORT).show();
        }

    }
}


