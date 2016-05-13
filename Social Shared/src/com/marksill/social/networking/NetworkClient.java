package com.marksill.social.networking;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.marksill.social.instance.Instance;
import com.marksill.social.instance.InstancePlayer;

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
				Map<String, Object> userinfo = new HashMap<>();
				userinfo.put("name", "Player");
				userinfo.put("id", new Random().nextInt(1000000));
				connection.sendTCP(new RequestConnect(userinfo));
			} else if (r instanceof RequestUpdate) {
				Map<Long, Map<String, Object>> map = (Map<Long, Map<String, Object>>) r.data;
				Instance.fromMap(map);
			} else if (r instanceof RequestPlayer) {
				InstancePlayer.pid = (long) r.data;
			}
		}
	}

}
