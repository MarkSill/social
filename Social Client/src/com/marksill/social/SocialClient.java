package com.marksill.social;

public class SocialClient {
	
	public static void main(String[] args) {
		Social social = new Social();
		try {
			social.start(true, false, args);
		} catch (SocialException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

}
