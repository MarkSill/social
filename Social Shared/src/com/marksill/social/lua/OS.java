package com.marksill.social.lua;

public class OS {

	public OS() {
		
	}
	
	public long time() {
		return System.currentTimeMillis();
	}
	
	public long nanoTime() {
		return System.nanoTime();
	}

}
