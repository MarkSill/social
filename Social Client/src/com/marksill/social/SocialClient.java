package com.marksill.social;

import com.marksill.social.networking.NetworkClient;

public class SocialClient {
	
	public static void main(String[] args) {
		Social social = new Social();
		social.setNetworkInterface(new NetworkClient("127.0.0.1", 55555, 55556));
		try {
			social.start(true, false, args);
		} catch (SocialException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

}
