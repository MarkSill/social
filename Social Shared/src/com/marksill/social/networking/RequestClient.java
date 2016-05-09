package com.marksill.social.networking;

public class RequestClient extends Request {

	public RequestClient() {
		this("client", null);
	}

	public RequestClient(String name, Object data) {
		super(name, data);
	}

}
