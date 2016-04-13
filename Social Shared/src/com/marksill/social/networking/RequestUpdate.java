package com.marksill.social.networking;

import java.util.Map;

public class RequestUpdate extends Request {
	
	public RequestUpdate() {
		this(null);
	}
	
	public RequestUpdate(Map<String, Object> map) {
		super("update", map);
	}

}
