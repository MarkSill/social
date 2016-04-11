package com.marksill.social.networking;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class NetworkClient extends NetworkInterface {
	
	public Client client;
	
	public NetworkClient(String host, int tcp, int udp) {
		client = new Client();
		client.start();
		try {
			client.connect(5000, host, tcp, udp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.addListener(new Listener() {
			public void received(Connection connection, Object data) {
				receive(connection, data);
			}
		});
		init(client.getKryo());
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
