package com.marksill.social.networking;

import java.io.IOException;
import java.util.Map;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.marksill.social.instance.Instance;
import com.marksill.social.instance.InstancePlayer;
import com.marksill.social.instance.InstancePlayers;

public class NetworkServer extends NetworkInterface {
	
	public Server server;
	
	public NetworkServer(int tcp, int udp) {
		server = new Server();
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
	
	@Override
	public void receive(Connection connection, Object data) {
		System.out.println("Received data: " + data);
		if (data instanceof Request) {
			Request r = (Request) data;
			if (data instanceof RequestConnect) {
				RequestConnect connect = (RequestConnect) r;
				System.out.println("Player " + connect.data + " joining...");
				InstancePlayer player = new InstancePlayer((String) connect.data);
				player.cid = connection.getID();
				((InstancePlayers) Instance.game.findChild("Players")).addPlayer(player);
			}
		}
	}
	
	public void sendUpdate() {
		Map<String, Object> map = Instance.game.createMap();
		//System.out.println(map);
		sendTCP(new RequestUpdate(map));
	}

}
