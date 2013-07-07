package com.bbpp.unitconverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cn.waps.AppConnect;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.widget.Toast;

public class MainActivity extends SlidingFragmentActivity {

	public static MainActivity mainActivity;

	private DownloadCurrencyTask downloadTask = null;

	private String[] mPlanetTitles;

	private float[] currencyRate = {6.1451f, 0.64737f, 0.76185f, 7.75799f, 7.9856f, 30.128f, 97.69f, 1.0504f, 1.082f, 1.291f, 0.933f, 1.2743f};
	long lastInternet = 0;

	private SlidingMenu menu;

	private int currentPosition;
	private ListFragment mFrag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	

		AppConnect.getInstance(this);
		
		getSupportActionBar().setIcon(R.drawable.drawer_icon);
		getSupportActionBar().setHomeButtonEnabled(true);

		mainActivity = this;

		mPlanetTitles = getResources().getStringArray(R.array.planets_array);

		/* Load currency */
		SharedPreferences pref = getPreferences(MODE_PRIVATE);
		for (int i = 0; i < currencyRate.length; ++i)
			currencyRate[i] = pref.getFloat("com.bbpp.unitconverter.currencyrate" + i, currencyRate[i]);
		UnitList.setCurrencyRate(currencyRate);
		lastInternet = pref.getLong("com.bbpp.unitconverter.lastinternet", lastInternet);
		currentPosition = pref.getInt("com.bbpp.unitconverter.pos", 1);

		/* Download currency */
		if (System.currentTimeMillis() - lastInternet > 86400000L) {			
			ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				if (downloadTask != null) {
					downloadTask.m_activity = this;					
				} else {
					downloadTask = new DownloadCurrencyTask();
					downloadTask.m_activity = this;
					downloadTask.execute();
				}
				//AppConnect appConn = AppConnect.getInstance(this);
				//appConn.checkUpdate(this);
			}			
		}

		setContentView(R.layout.activity_main);	

		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			mFrag = new DrawerListFrame();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (ListFragment)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		}

		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidth(16);
		menu.setShadowDrawable(R.drawable.drawer_shadow);
		menu.setBehindOffset(128);
		menu.setFadeDegree(0.35f);

		if (savedInstanceState == null) {			
			selectItem(currentPosition);			
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {		
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPause() {
		Editor edit = getPreferences(MODE_PRIVATE).edit();
		for (int i = 0; i < currencyRate.length; ++i)
			edit.putFloat("com.bbpp.unitconverter.currencyrate" + i, currencyRate[i]);
		edit.putInt("com.bbpp.unitconverter.pos", currentPosition);
		edit.putLong("com.bbpp.unitconverter.lastinternet", lastInternet);		
		edit.commit();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		
		if (downloadTask != null) {
			downloadTask.m_activity = null;
			downloadTask.cancel(false);
		}	
		
		mainActivity = null;

		super.onDestroy();
	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			break;
		case R.id.action_recommend:			
			break;
		case R.id.action_feedback:			
			break;		
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.showContent();
		} else {
			super.onBackPressed();
		}
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getSupportMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/** Swaps fragments in the main content view */
	public void selectItem(int position) {

		if (menu.isMenuShowing()) {
			menu.showContent();
		}

		currentPosition = position;		

		Fragment fragment = new UnitFragment();
		Bundle args = new Bundle();
		args.putInt(UnitFragment.ARG_UNIT_LIST, position);		
		fragment.setArguments(args);

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commitAllowingStateLoss();

		setTitle(mPlanetTitles[position]);
	}

	@Override
	public void setTitle(CharSequence title) {		
		getSupportActionBar().setTitle(title);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_MENU) {
	    	menu.toggle();
	        return true;
	    }
	    return super.onKeyUp(keyCode, event);
	}

	private static class DownloadCurrencyTask extends AsyncTask<Void, Void, ArrayList<Float>> {

		private MainActivity m_activity = null;

		@Override
		protected void onPostExecute(ArrayList<Float> result) {

			if (m_activity != null) {
				for (int i = 0; i < result.size(); ++i)
					m_activity.currencyRate[i] = result.get(i).floatValue();
				UnitList.setCurrencyRate(m_activity.currencyRate);
				if (m_activity.currentPosition == 0) {				
					m_activity.selectItem(m_activity.currentPosition);
				}
				m_activity.lastInternet = System.currentTimeMillis(); 
				Toast toast = Toast.makeText(m_activity, m_activity.getResources().getString(R.string.update_message), Toast.LENGTH_SHORT);
				toast.show();
			}
		}

		@Override
		protected ArrayList<Float> doInBackground(Void... params) {
			InputStream is = null;
			ArrayList<Float> resList = new ArrayList<Float>();

			try {
				URL url = new URL("http://hq.sinajs.cn/list=USDCNY,GBPUSD,EURUSD,USDHKD,USDMOP,USDTWD,USDJPY,USDCAD,AUDUSD,NZDUSD,USDCHF,USDSGD");
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setReadTimeout(10000 /* milliseconds */);
				conn.setConnectTimeout(15000 /* milliseconds */);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				conn.connect();
				int response = conn.getResponseCode();
				if (response == HttpURLConnection.HTTP_OK) {
					is = conn.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					String line;
					int i = 0;
					while ((line = reader.readLine()) != null) {						
						float rate = m_activity.currencyRate[i];
						String[] tokens = line.split(",");
						if (tokens.length >= 2) {
							try {
								float rate2 = Float.parseFloat(tokens[1]);
								if (rate2 > 0) {
									switch (i) {
									case 1:
									case 2:
									case 8:
									case 9:
										rate2 = 1/rate2;
									}
									rate = rate2;
								}
							} catch (NumberFormatException ex) {

							}
						}						
						resList.add(rate);
						++i;
					}
				}
			} catch (IOException e) {				
				//e.printStackTrace();
			} finally {
				if (is != null)
					try {
						is.close();
					} catch (IOException e) {

					}
			}
			return resList;
		}
	}

} 