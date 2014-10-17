package org.vit.model;

public class MyHttpURL extends MyURL{
	public MyHttpURL(String url){
		super.setUrl(url);
		super.setMode("http");
	}
}
