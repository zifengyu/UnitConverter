package com.bbpp.unitconverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cn.waps.AppConnect;
import cn.waps.UpdatePointsNotifier;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements UpdatePointsNotifier {

	private float[] currencyRate = {6.1335f, 0.643f, 0.7567f, 7.7621f, 7.9883f, 29.7350f, 97.55f, 1.0185f, 1.0515f, 1.271f, 0.9355f, 1.2480f};
	long lastInternet = 0;

	private String[] mPlanetTitles;
	private ListView mDrawerList;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	private int currentPosition;

	private boolean showAd = false;
	public static int score = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AppConnect.getInstance(this);

		/* Load currency */
		SharedPreferences pref = getPreferences(MODE_PRIVATE);
		for (int i = 0; i < currencyRate.length; ++i)
			currencyRate[i] = pref.getFloat("com.bbpp.unitconverter.currencyrate" + i, currencyRate[i]);
		UnitList.setCurrencyRate(currencyRate);
		lastInternet = pref.getLong("com.bbpp.unitconverter.lastinternet", lastInternet);
		score = pref.getInt("com.bbpp.unitconverter.score", 0);
				
		if (++score > 10) {
			showAd = true;
		} else {
			AppConnect appConn = AppConnect.getInstance(this);
			if (appConn != null)
				appConn.awardPoints(1, this);
		}

		if (System.currentTimeMillis() - lastInternet > 86400000L) {
			/* Download currency */
			ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				new DownloadCurrencyTask().execute();
			}			
		}

		setContentView(R.layout.activity_main);
		
		mTitle = getTitle();		
		mDrawerTitle = getResources().getString(R.string.drawer_title);		
		mPlanetTitles = getResources().getStringArray(R.array.planets_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// Set the adapter for the list view
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));
		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description */
				R.string.drawer_close  /* "close drawer" description */
				) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {			
			selectItem(pref.getInt("com.bbpp.unitconverter.pos", 1));
		}
	}

	@Override
	protected void onPause() {
		Editor edit = getPreferences(MODE_PRIVATE).edit();
		for (int i = 0; i < currencyRate.length; ++i)
			edit.putFloat("com.bbpp.unitconverter.currencyrate" + i, currencyRate[i]);
		edit.putInt("com.bbpp.unitconverter.pos", currentPosition);
		edit.putLong("com.bbpp.unitconverter.lastinternet", lastInternet);
		edit.putInt("com.bbpp.unitconverter.score", score);
		edit.commit();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		AppConnect appConn = AppConnect.getInstance(this);
		if (appConn != null)
			appConn.finalize();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {

		currentPosition = position;

		Fragment fragment = new UnitFragment();
		Bundle args = new Bundle();
		args.putInt(UnitFragment.ARG_UNIT_LIST, position);
		args.putBoolean(UnitFragment.ARG_SHOW_AD, showAd);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);		
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_settings:
			AppConnect.getInstance(this).showFeedback();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private class DownloadCurrencyTask extends AsyncTask<Void, Void, ArrayList<Float>> {

		@Override
		protected void onPostExecute(ArrayList<Float> result) {
			for (int i = 0; i < result.size(); ++i)
				currencyRate[i] = result.get(i).floatValue();
			UnitList.setCurrencyRate(currencyRate);
			if (currentPosition == 0) {
				//((FrameLayout)MainActivity.this.findViewById(R.id.content_frame)).invalidate();
				MainActivity.this.selectItem(currentPosition);
			}
			lastInternet = System.currentTimeMillis(); 
			Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.update_message), Toast.LENGTH_SHORT);
			toast.show();
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
						float rate = currencyRate[i];
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

	@Override
	public void getUpdatePoints(String arg0, int point) {
		
		if (point > 10) {
			score = point;
		}

	}

	@Override
	public void getUpdatePointsFailed(String arg0) {
		

	}


} 