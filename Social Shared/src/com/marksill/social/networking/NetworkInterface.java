package com.marksill.social.networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;
import org.luaj.vm2.LuaValue;
import org.newdawn.slick.Color;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.marksill.social.instance.TempJoint;

public abstract class NetworkInterface {
	
	public static final int BUFFER_SIZE = 65536;
	
	public static final Class<?>[] CLASSES = {
			Request.class,
			RequestConnect.class,
			RequestReadyForUsername.class,
			RequestKick.class,
			RequestUpdate.class,
			RequestClient.class,
			RequestPlayer.class,
			ArrayList.class,
			List.class,
			Vector2.class,
			Map.class,
			HashMap.class,
			Color.class,
			Transform.class,
			LuaValue.class,
			Object.class,
			TempJoint.class
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
