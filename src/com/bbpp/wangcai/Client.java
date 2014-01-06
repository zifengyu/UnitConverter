package com.bbpp.wangcai;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Client extends WebViewClient {

	@Override  
    public void onPageFinished(WebView view, String url)  
    {  
        if (view != null && view.getTitle() != null && view.getTitle().contains("ad"))
        	view.loadUrl("javascript:window.SDKUtils.showHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");  
    }
	
}
