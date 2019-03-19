package com.itheima.httpclient;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

public class TestAPIServcie {
	@Test
	public void testdogetParam() throws Exception{
			APIService service = new APIService();
			Map<String , Object> map =new  HashMap<String, Object>();
			map.put("page", "1");
			map.put("rows", "20");
			
			HttpResult doGet = service.doGet("http://localhost:8081/item/list", map);
			System.out.println(doGet.getCode());
			System.out.println(doGet.getBody());
	}
	
	@Test
	public void testdoPostParam() throws Exception{
		APIService service = new APIService();
		Map<String , Object> map =new  HashMap<String, Object>();
		
		/**
		 *  parameters.add(new BasicNameValuePair("cid", "560"));
        parameters.add(new BasicNameValuePair("title", "httpclient"));
        parameters.add(new BasicNameValuePair("sellPoint", "asfdafaf"));
        parameters.add(new BasicNameValuePair("price", "1200"));
        parameters.add(new BasicNameValuePair("num", "213"));
		 */
		
		map.put("cid", "560");
		map.put("title", "apiservicehttpclienttest");
		map.put("sellPoint", "asfdafaf");
		map.put("price", "1200");
		map.put("num", "213");
		
		HttpResult doGet = service.doGet("http://localhost:8081/item/save", map);
		System.out.println(doGet.getCode());
		System.out.println(doGet.getBody());
	}
}
