package com.marksill.social.networking;

import java.util.HashMap;
import java.util.Map;

public class RequestConnect extends Request {
	
	public RequestConnect() {
		this(new HashMap<String, Object>());
	}
	
	public RequestConnect(Map<String, Object> data) {
		super("connect", data);
	}

}
