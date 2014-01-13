package com.bbpp.wangcai;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

public class Wangcai {

	private String app_id = "1e893136c07aaa5a3babaf8a3f3d26a6";
	private String app_version = "3.5";
	private String act = "com.bbpp.unitconverter";
	private String channel = "gfan";
	private String sdk_version = "1.8.6";	
	private String base = decode("nc.xpaw");

	private String udid = "";
	private String imsi = "";
	private String net = "";
	private String device_name = android.os.Build.MODEL;
	private String device_brand = android.os.Build.BRAND;
	private String y = "";
	private String device_type = "android";
	private String os_version = android.os.Build.VERSION.RELEASE;
	private String country_code = "";
	private String language = "";	
	private String device_width = "480";
	private String device_height = "800";
	
	private String parameter = "";	 
	
	public boolean valid = true;

	public Wangcai(Context context) {
		TelephonyManager t = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		if (t == null) {
			valid = false;
			return;
		}

		udid = t.getDeviceId();
		if (udid == null) {
			valid = false;
			return ;
		}
		imsi = t.getSubscriberId();
		if (imsi == null)
			imsi = "null";

		ConnectivityManager c = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (c != null) {
			NetworkInfo netinfo = c.getActiveNetworkInfo();			
			if (netinfo != null) {
				if (netinfo.isAvailable()) {
					net = netinfo.getTypeName().toLowerCase(Locale.ENGLISH);
				} else {
					valid = false;
					return ;
				}
			} else {
				valid = false;
				return ;
			}
		} else {
			valid = false;
			return ;
		}
		
		String key = decode("moc.liamg@gnaugoaixgnik") + udid + app_id;
		byte[] bytes = key.toLowerCase(Locale.ENGLISH).getBytes();
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.reset();
			md5.update(bytes);
			bytes = md5.digest();
			String res = "";
			for (int i = 0; i < bytes.length; ++i) {
				 String s2 = Integer.toHexString(0xff & bytes[i]);
				 if (s2.length() == 1)
					 res += "0";
				 res += s2;
					 
			}
			
			y = res.toLowerCase(Locale.ENGLISH);
			
		} catch (NoSuchAlgorithmException e) {
			valid = false;
		}
		
		country_code = Locale.getDefault().getCountry();
		language = Locale.getDefault().getLanguage();
		
		WindowManager w = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		
		if (w != null) {						
			device_width = Integer.toString(w.getDefaultDisplay().getWidth());
			device_height = Integer.toString(w.getDefaultDisplay().getHeight());
		}
		//Point p = new Point();
		//w.getDefaultDisplay().getSize(p);
		//device_width = Integer.toString(p.x);
		//device_height = Integer.toString(p.y);
		
		
		StringBuilder build = new StringBuilder();
		build.append("?app_id=");
		build.append(app_id);		
		build.append("&udid=");
		build.append(udid);
		build.append("&imsi=");
		build.append(imsi);		
		build.append("&net=");
		build.append(net);
		build.append("&base=");
		build.append(base);	
		build.append("&app_version=");
		build.append(app_version);
		build.append("&sdk_version=");
		build.append(sdk_version);
		build.append("&device_name=");
		build.append(device_name);
		build.append("&device_brand=");
		build.append(device_brand);
		build.append("&y=");
		build.append(y);
		build.append("&device_type=");
		build.append(device_type);
		build.append("&os_version=");
		build.append(os_version);
		build.append("&country_code=");
		build.append(country_code);
		build.append("&language=");
		build.append(language);
		build.append("&act=");
		build.append(act);
		build.append("&channel=");
		build.append(channel);
		build.append("&device_width=");
		build.append(device_width);
		build.append("&device_height=");
		build.append(device_height);
		parameter =  build.toString().replaceAll(" ", "%20");		
		
	}
	
	public String getParameter() {
		if (!valid)
			return "";
		return parameter;
	}
	
	public static String decode(String str) {
		StringBuilder sb = new StringBuilder(str);
		return sb.reverse().toString();
	}
	
}
