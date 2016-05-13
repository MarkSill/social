package com.marksill.social.networking;

public class RequestPlayer extends Request {

	public RequestPlayer() {
		this(-1);
	}
	
	public RequestPlayer(long data) {
		this("player", data);
	}

	public RequestPlayer(String name, Object data) {
		super(name, data);
	}

}
