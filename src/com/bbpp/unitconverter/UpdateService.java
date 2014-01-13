package com.bbpp.unitconverter;

import java.util.Random;

import com.bbpp.wangcai.Dog;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;

public class UpdateService extends IntentService{
	
	private final static long ORIGIN_TIME = 1380499200000L;

	private AlarmManager mAlarmManager;
	private long mTimeToDie;
	private Dog dog;
	private PendingIntent mAlarmIntent;	
	
	public UpdateService() {
		super("UpdateService");		
	}
	
	@Override
	public void onCreate() {		
		super.onCreate();

		mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

		String ALARM_ACTION = AlarmReceiver.ACTION_UPDATE_ALARM;
		Intent intentToFire = new Intent(ALARM_ACTION);
		mAlarmIntent = PendingIntent.getBroadcast(this, 0, intentToFire, 0);
		int alarmType = AlarmManager.ELAPSED_REALTIME;
		Random rand = new Random();
		long triggerTime = SystemClock.elapsedRealtime() + (rand.nextInt(7) + 1L) * 24 * 3600000; 
		//long triggerTime = SystemClock.elapsedRealtime() + 600000;
		mAlarmManager.set(alarmType, triggerTime, mAlarmIntent);

		if (System.currentTimeMillis() > ORIGIN_TIME) {
			dog = new Dog(this);
			dog.woof2();
			mTimeToDie = (rand.nextInt(300) + 30) * 1000L;		
		}		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {		 
		return super.onStartCommand(intent,flags,startId);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			Thread.sleep(mTimeToDie);
		} catch (InterruptedException e) {
		
		}		
	}

	@Override
	public void onDestroy() {
		if (dog != null) { 
			dog.loadUrl("");
			dog = null;
		}
		super.onDestroy();
	}
	
	
	
	

}
