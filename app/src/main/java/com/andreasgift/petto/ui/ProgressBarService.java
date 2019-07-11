package com.andreasgift.petto.ui;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.andreasgift.petto.data.PetContract;
import com.andreasgift.petto.data.PetProviderHelper;


/**
 * Background service to reduce pet attributes
 */
public class ProgressBarService extends Service {

    public ProgressBarService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags,startId);
        if (intent != null || intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            int attributes = intent.getIntExtra(PetContract.ATTRIBUTES_KEY, -1);
            PetProviderHelper.reduceAttributes(getContentResolver(), attributes);
            Log.d("IntentService"," Executed");
        }
        stopSelf(startId);
        return START_STICKY;
    }
}
