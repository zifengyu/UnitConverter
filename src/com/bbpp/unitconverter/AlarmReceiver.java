package com.bbpp.unitconverter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
	
	 public static final String ACTION_UPDATE_ALARM = "com.bbpp.unitconverter.ACTION_UPDATE_ALARM";

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent startIntent = new Intent(context, UpdateService.class);
		context.startService(startIntent);		
	}

}
