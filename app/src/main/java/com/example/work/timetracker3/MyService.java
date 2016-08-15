package com.example.work.timetracker3;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by wilki on 7/28/2016.
 */
public class MyService extends Service {

    private final IBinder mbinder = new LocalBinder();



    /*this is the only method required, but we dont need it because we are not binding anything*/
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }


    public class LocalBinder extends Binder{
        public MyService getService(){
            return MyService.this;
        }

    }

    public int getTime(){
        Log.i("HERE", "We are in the getime method");
        return 12;
    }


}
