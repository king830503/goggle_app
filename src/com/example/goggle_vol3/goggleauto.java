package com.example.goggle_vol3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class goggleauto extends BroadcastReceiver {

	

@Override
public void onReceive(Context context, Intent intent) {
	String action = intent.getAction();
	if (action.equals(Intent.ACTION_BOOT_COMPLETED)){
		Intent goggleIntent=new Intent(context,MainActivity.class);
		goggleIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(goggleIntent);
		}
	}
}