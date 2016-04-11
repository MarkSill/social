package com.marksill.social.networking;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class NetworkClient extends NetworkInterface {
	
	public Client client;
	
	public NetworkClient(String host, int tcp, int udp) {
		client = new Client();
		init(client.getKryo());
		client.start();
		try {
			client.connect(5000, host, tcp, udp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.addListener(new Listener() {
			@Override
			public void received(Connection connection, Object data) {
				receive(connection, data);
			}
		});
	}

	@Override
	public void sendTCP(Object data) {
		client.sendTCP(data);
	}

	@Override
	public void sendUDP(Object data) {
		client.sendUDP(data);
	}

	@Override
	public void receive(Connection connection, Object data) {
		if (data instanceof Request) {
			Request r = (Request) data;
			if (r instanceof RequestReadyForUsername) {
				connection.sendTCP(new RequestConnect("MarkSill"));
			}
		}
	}

}
