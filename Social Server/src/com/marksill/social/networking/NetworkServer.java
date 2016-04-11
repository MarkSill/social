package com.marksill.social.networking;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class NetworkServer extends NetworkInterface {
	
	public Server server;
	
	public NetworkServer(int tcp, int udp) {
		server = new Server();
		server.start();
		try {
			server.bind(tcp, udp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.addListener(new Listener() {
			public void received(Connection connection, Object data) {
				receive(connection, data);
			}
		});
	}

	@Override
	public void sendTCP(Object data) {
		
	}

	@Override
	public void sendUDP(Object data) {
		
	}
	
	@Override
	public void receive(Connection connection, Object data) {
		
	}

}
