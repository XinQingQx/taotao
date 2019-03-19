package com.itheima.httpclient;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class APIService {
	// 1.不带参数的GET请求
	public HttpResult doget(String url) throws Exception {
		return doGet(url, null);
	}

	// 2.带参数的GET请求
	// 要返回http状态码以及响应的内容
	public HttpResult doGet(String url, Map<String, Object> map) throws Exception {
		// 1.创建httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 2.创建httget请求
		// 创建uribuilder 构建Url 及设置参数
		URIBuilder uriBuilder = new URIBuilder(url);
		// 循环遍历参数集合 设置参数
		// 判断如果map不为空
		if (map != null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
			}
		}
		HttpGet httpGet = new HttpGet(uriBuilder.build());
		// 3.执行请求（发送请求）
		CloseableHttpResponse response = httpClient.execute(httpGet);
		// 4.接收响应内容，进行解析 封装到httpresult
		// 内容有的时候
		Integer code = response.getStatusLine().getStatusCode();// 响应的状态码
		if (response.getEntity() != null) {
			String body = EntityUtils.toString(response.getEntity(), "utf-8");// 获取响应的内容，转换成字符串
			HttpResult result = new HttpResult(code, body);
			return result;
		} else {
			HttpResult result = new HttpResult(code, null);
			return result;
		}
	}

	// 3.不带参数的POST请求
	public HttpResult doPost(String url) throws Exception{
		return doPost(url,null);
	}
	
	// 4.带参数的POST请求
	public HttpResult doPost(String url, Map<String, Object> map) throws Exception {

		// 1.创建httpclient
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 2.创建httppost请求
		HttpPost httpPost = new HttpPost(url);
		// 3.构建参数的列表
		//判断参数不为空的情况
		if (map != null) {
			List<NameValuePair> params = new ArrayList<>();
			// 遍历集合 构建参数列表
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
			// 4.创建form表单传递参数的实体对象
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");

			// 设置httpost请求的实体对象
			httpPost.setEntity(entity);
		}
		// 5.执行请求
		CloseableHttpResponse response = httpClient.execute(httpPost);
		// 6.接收响应 封装到httpresult中
		// 内容有的时候
		Integer code = response.getStatusLine().getStatusCode();// 响应的状态码
		if (response.getEntity() != null) {
			String body = EntityUtils.toString(response.getEntity(), "utf-8");// 获取响应的内容，转换成字符串
			HttpResult result = new HttpResult(code, body);
			return result;
		} else {
			HttpResult result = new HttpResult(code, null);
			return result;
		}
	}

}
