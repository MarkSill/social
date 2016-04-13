package com.marksill.social;

import com.marksill.social.networking.NetworkServer;
import com.marksill.social.state.NotGameState;
import com.marksill.social.state.NotState;

public class SocialServer {
	
	public static final double SLEEP_TIME = 1 / 60; //1/60th of a second (~17 milliseconds per update).
	
	public static NotState state;
	
	private static long lastTime;
	private static int framesSinceNetwork = 0;
	
	public static void main(String[] args) {
		Social social = new Social();
		social.setNetworkInterface(new NetworkServer(55555, 55556));
		try {
			social.start(false, false, args);
		} catch (SocialException e) {
			e.printStackTrace();
		}
		state = new NotGameState();
		lastTime = System.nanoTime();
		state.init(Social.getInstance());
		loop();
	}
	
	public static void loop() {
		while (true) {
			double time = 1000000000 / (System.nanoTime() - lastTime);
			lastTime = System.nanoTime();
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int delta = (int) (1000 / time);
			state.update(Social.getInstance(), delta);
			if (framesSinceNetwork++ >= 6) {
				((NetworkServer) Social.getInstance().getNetworkInterface()).sendUpdate();
				framesSinceNetwork = 0;
			}
		}
	}

}
