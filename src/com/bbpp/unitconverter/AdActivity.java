package com.bbpp.unitconverter;

import cn.waps.AppConnect;
import android.app.Activity;
import android.os.Bundle;

public class AdActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AppConnect.getInstance(this);
		AppConnect.getInstance(this).showPopAd(this);
		
	}
	
	

}
