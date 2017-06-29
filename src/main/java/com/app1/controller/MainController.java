package com.app1.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	@Autowired
	private com.app1.Config conf;
	
	CookieStore cookieStore = new BasicCookieStore();
	
	@RequestMapping(value="/test",method=RequestMethod.GET,produces="text/plain;charset=utf-8")
	@ResponseBody
	public String test() throws ClientProtocolException, IOException{
		String res=null;
		//CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpClient httpclient = HttpClients.custom()
		        .setDefaultCookieStore(cookieStore)
		        .build();
		
		
		HttpPost httpPost = new HttpPost("http://cas.xd.mtn/0.0/test");
		List<NameValuePair> nvps = new ArrayList <>();
		nvps.add(new BasicNameValuePair("xx", "yyyy"));
		Header h1=new BasicHeader("appID",conf.getUrl());
		httpPost.addHeader(h1);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse response2 = httpclient.execute(httpPost);
		
		
		try {
		    HttpEntity entity2 = response2.getEntity();
		    res=EntityUtils.toString(entity2,"utf-8");
		    EntityUtils.consume(entity2);
		} finally {
		    response2.close();
		}
		return res;
	}
	
	@RequestMapping(value="/cookie",method=RequestMethod.GET,produces="text/plain;charset=utf-8")
	@ResponseBody
	public String test1() throws ClientProtocolException, IOException{
		
		String res=null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://cas.xd.mtn/cookie");		
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		try {
			
			//String setCookie = response1.getFirstHeader("Set-Cookie").getValue();
			//String JSESSIONID = setCookie.substring("JSESSIONID=".length(),setCookie.indexOf(";"));
			
			BasicClientCookie cookie = new BasicClientCookie("JSESSIONID","hahahaha");
		    cookie.setDomain("cas.xd.mtn");
		    cookie.setPath("/0.0/test");
		    cookie.setAttribute("token","temptoken");
		    cookieStore.addCookie(cookie);
			
		    HttpEntity entity1 = response1.getEntity();
		    res=EntityUtils.toString(entity1,"utf-8");
		    EntityUtils.consume(entity1);
		} finally {
		    response1.close();
		}
		return res;
	}
}
