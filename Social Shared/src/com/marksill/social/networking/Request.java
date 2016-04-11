package com.marksill.social.networking;

public abstract class Request {
	
	public String name;
	public Object data;
	
	public Request() {
		this("", null);
	}
	
	public Request(String name, Object data) {
		this.name = name;
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "Request " + name + ": " + data.toString();
	}

}
