package com.itheima.httpclient;

public class HttpResult {
	private Integer code;//响应状态码
	
	private String body;//响应的内容
	
	

	public HttpResult(Integer code, String body) {
		super();
		this.code = code;
		this.body = body;
	}

	public HttpResult() {
		super();
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	
}
