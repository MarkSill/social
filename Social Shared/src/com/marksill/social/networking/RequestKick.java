package com.marksill.social.networking;

public class RequestKick extends Request {
	
	public RequestKick() {
		this("No reason given.");
	}
	
	public RequestKick(String reason) {
		super("kick", reason);
	}

}
