package com.nothing.contacts;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class StartService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) && MainActivity.getFlag()==1)
        {
            Intent i= new Intent(context,shake_check.class);
            //Intent i = MainActivity.intent;
            context.startService(i);
        }
    }

}



