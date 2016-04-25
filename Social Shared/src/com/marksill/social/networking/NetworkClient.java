package com.marksill.social.networking;

import java.io.IOException;
import java.util.Map;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.marksill.social.instance.Instance;

public class NetworkClient extends NetworkInterface {
	
	public Client client;
	
	public NetworkClient(String host, int tcp, int udp) {
		client = new Client(BUFFER_SIZE, BUFFER_SIZE);
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

	@SuppressWarnings("unchecked")
	@Override
	public void receive(Connection connection, Object data) {
		if (data instanceof Request) {
			Request r = (Request) data;
			if (r instanceof RequestReadyForUsername) {
				connection.sendTCP(new RequestConnect("MarkSill"));
			} else if (r instanceof RequestUpdate) {
				Map<Long, Map<String, Object>> map = (Map<Long, Map<String, Object>>) r.data;
				Instance.fromMap(map);
			}
		}
	}

}
