package com.marksill.social.networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Color;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;

public abstract class NetworkInterface {
	
	public static final Class<?>[] CLASSES = {
			Request.class,
			RequestConnect.class,
			RequestReadyForUsername.class,
			RequestKick.class,
			RequestUpdate.class,
			ArrayList.class,
			List.class,
			Vector2.class,
			Map.class,
			HashMap.class,
			Color.class
	};
	
	public void init(Kryo kryo) {
		kryo.addDefaultSerializer(Color.class, ColorSerializer.class);
		for (Class<?> clazz : CLASSES) {
			kryo.register(clazz);
		}
	}
	
	public abstract void sendTCP(Object data);
	public abstract void sendUDP(Object data);
	public abstract void receive(Connection connection, Object data);

}
