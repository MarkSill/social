package com.marksill.social.networking;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.marksill.social.Social;
import com.marksill.social.instance.Instance;
import com.marksill.social.instance.InstancePlayer;
import com.marksill.social.instance.InstancePlayers;

public class NetworkServer extends NetworkInterface {
	
	public Server server;
	public Map<Long, Map<String, Object>> lastMap = null;
	
	public NetworkServer(int tcp, int udp) {
		server = new Server(BUFFER_SIZE, BUFFER_SIZE);
		init(server.getKryo());
		server.start();
		try {
			server.bind(tcp, udp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.addListener(new Listener() {
			@Override
			public void received(Connection connection, Object data) {
				receive(connection, data);
			}
			
			@Override
			public void connected(Connection connection) {
				System.out.println("Client connecting...");
				connection.sendTCP(new RequestReadyForUsername());
			}
			
			@Override
			public void disconnected(Connection connection) {
				System.out.println("Client disconnecting...");
				for (InstancePlayer pl : ((InstancePlayers) Instance.game.findChild("Players")).getPlayersAsList()) {
					if (pl.cid == connection.getID()) {
						System.out.println("Player " + pl.name + " disconnected.");
						pl.delete();
						break;
					}
				}
			}
		});
	}

	@Override
	public void sendTCP(Object data) {
		server.sendToAllTCP(data);
	}

	@Override
	public void sendUDP(Object data) {
		server.sendToAllUDP(data);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void receive(Connection connection, Object data) {
		System.out.println("Received data: " + data);
		if (data instanceof Request) {
			Request r = (Request) data;
			if (data instanceof RequestConnect) {
				RequestConnect connect = (RequestConnect) r;
				Map<String, Object> userinfo = (HashMap<String, Object>) connect.data;
				System.out.println("Player " + userinfo.get("name") + " joining...");
				InstancePlayer player = new InstancePlayer((String) userinfo.get("name"));
				player.cid = connection.getID();
				((InstancePlayers) Instance.game.findChild("Players")).addPlayer(player);
				connection.sendUDP(new RequestUpdate(lastMap));
				System.out.println("Sent instance information to player \"" + userinfo.get("name") + "\".");
			}
		}
	}
	
	public void sendUpdate() {
		if (!Social.getInstance().isRunning()) {
			lastMap = null;
			return;
		}
		Map<Long, Map<String, Object>> map = Instance.toMap();
		Map<Long, Map<String, Object>> newMap = new HashMap<>();
		if (lastMap != null) {
			for (Long id : map.keySet()) {
				Map<String, Object> obj = map.get(id);
				Map<String, Object> lastObj = lastMap.get(id);
				Map<String, Object> newObj = new HashMap<>();
				if (obj != null && lastObj != null) {
					for (String key : obj.keySet()) {
						Object last = lastObj.get(key);
						Object current = obj.get(key);
						if (key.equals("cname")) {
							newObj.put(key, obj.get(key));
						} else if (last != null && current != null && !last.equals(current)) {
							newObj.put(key, obj.get(key));
						} else {
							if ((last == null || current == null) && !(last == null && current == null)) {
								newObj.put(key, obj.get(key));
							}
						}
					}
				} else if (obj != null && lastObj == null) {
					newObj = obj;
				} else if (obj == null && lastObj != null) {
					newObj = null;
				}
				newMap.put(id, newObj);
			}
		} else {
			newMap = map;
		}
		lastMap = map;
		sendUDP(new RequestUpdate(newMap));
	}

}
