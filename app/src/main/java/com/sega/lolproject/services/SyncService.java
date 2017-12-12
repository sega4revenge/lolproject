package com.sega.lolproject.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by sega4 on 11/12/2017.
 */

public class SyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();

    private static SyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("khoi tao dau tien");
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {

                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
