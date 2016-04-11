package com.marksill.social.networking;

public class RequestConnect extends Request {
	
	public RequestConnect() {
		this("");
	}
	
	public RequestConnect(String username) {
		super("connect", username);
	}

}
