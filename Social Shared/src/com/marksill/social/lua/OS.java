package com.marksill.social.lua;

/**
 * Class meant to replace Lua's os class/table thing to reduce security risks.
 */
public class OS {

	/**
	 * Creates a new instance of OS.
	 */
	public OS() {
		
	}
	
	/**
	 * Gets the time in seconds since the Unix epoch.
	 * @return The current time since the Unix epoch.
	 */
	public long time() {
		return System.currentTimeMillis() / 1000;
	}
	
	/**
	 * Gets the time using Java's System.nanoTime(). Preferred of time().
	 * @return The current time in nanoseconds.
	 */
	public long nanoTime() {
		return System.nanoTime();
	}

}
