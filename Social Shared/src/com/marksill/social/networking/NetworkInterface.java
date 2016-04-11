package com.marksill.social.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.marksill.social.instance.Instance;
import com.marksill.social.instance.InstanceBlock;
import com.marksill.social.instance.InstanceCircle;
import com.marksill.social.instance.InstanceClientScript;
import com.marksill.social.instance.InstanceContainer;
import com.marksill.social.instance.InstanceGame;
import com.marksill.social.instance.InstancePlayer;
import com.marksill.social.instance.InstancePlayers;
import com.marksill.social.instance.InstanceRectangle;
import com.marksill.social.instance.InstanceScript;
import com.marksill.social.instance.InstanceWorld;

public abstract class NetworkInterface {
	
	public static final Class<?>[] CLASSES = {
			Instance.class,
			InstanceBlock.class,
			InstanceCircle.class,
			InstanceClientScript.class,
			InstanceContainer.class,
			InstanceGame.class,
			InstancePlayer.class,
			InstancePlayers.class,
			InstanceRectangle.class,
			InstanceScript.class,
			InstanceWorld.class,
			Request.class,
			RequestConnect.class,
			RequestReadyForUsername.class
	};
	
	public void init(Kryo kryo) {
		for (Class<?> clazz : CLASSES) {
			kryo.register(clazz);
		}
	}
	
	public abstract void sendTCP(Object data);
	public abstract void sendUDP(Object data);
	public abstract void receive(Connection connection, Object data);

}
