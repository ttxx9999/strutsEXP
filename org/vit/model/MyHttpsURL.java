package org.vit.model;

public class MyHttpsURL extends MyURL{
	public MyHttpsURL(String url){
		super.setUrl(url);
		super.setMode("https");
	}
}
