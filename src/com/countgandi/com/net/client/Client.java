package com.countgandi.com.net.client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import com.countgandi.com.game.Handler;
import com.countgandi.com.game.commands.CommandHandler;
import com.countgandi.com.game.entities.MPlayer;
import com.countgandi.com.game.entities.Player;
import com.countgandi.com.game.worldGen.World;
import com.countgandi.com.net.Proxy;

public class Client implements Proxy {

	public static boolean running = false;
	public static int Port;
	public static String ipAddress;
	private static Socket tcpSocket;
	private static DatagramSocket udpSocket;
	private static String username;

	public static Player player;
	public static ArrayList<MPlayer> players;

	private Handler handler;

	public Client(int port, String ipAddress, String username, Handler handler) {
		Client.Port = port;
		Client.ipAddress = ipAddress;
		Client.username = username + new Random().nextLong();
		this.handler = handler;
		player = new Player(new Vector3f(0, 0, 0), handler);
		handler.entities.add(player);
		players = new ArrayList<MPlayer>();
		load();
	}

	@Override
	public void load() {
		try {
			tcpSocket = new Socket(ipAddress, Port);
			udpSocket = new DatagramSocket();
			udpSocket.connect(tcpSocket.getInetAddress(), Port);
			running = true;

			sendDataTcp("Connection:" + username);

			boolean flag = true;
			byte[] bytes = new byte[128];
			while (flag) {

				tcpSocket.getInputStream().read(bytes);
				String tt = new String(bytes).trim();
				if (tt.startsWith("World:")) {
					int l = Integer.parseInt(tt.substring(6).trim().split(";")[0].trim());
					World.SEED = l;
					flag = false;
				}
			}

		} catch (ConnectException e) {
			stop();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			stop();
		} catch (IOException e) {
			e.printStackTrace();
			stop();
		}
		tick();
	}

	public void sendToConnections() throws IOException {
		sendDataUdp(username + "@Update:" + handler.getCamera().getPosition().x + ":" + handler.getCamera().getPosition().y + ":" + handler.getCamera().getPosition().z + ":" + handler.getCamera().getRot().x + ":" + handler.getCamera().getRot().y + ":" + handler.getCamera().getRot().z);
	}

	public void recieveTcp() throws IOException {
		byte[] bytes = new byte[128];
		tcpSocket.getInputStream().read(bytes);
		String tt = new String(bytes).trim();

		if (tt.startsWith("Command:")) {
			CommandHandler.doCommand(tt.substring(8));
		} else if (tt.startsWith("Disconnect:")) {
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).username.equals(tt.substring(11).trim())) {
					players.remove(i);
				}
			}
		} else if (tt.startsWith("Connect:")) {
			boolean flag = true;
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).username.equals(tt.substring(8).trim())) {
					flag = false;
				}
			}
			if (flag) {
				players.add(new MPlayer(tt.substring(8).trim(), new Vector3f(0, 0, 0), handler));
			}
		}

	}

	public void recieveUdp() throws IOException {
		DatagramPacket packet = new DatagramPacket(new byte[128], 128);
		udpSocket.receive(packet);
		String tt = new String(packet.getData()).trim();

		if (tt.startsWith("Update:")) {
			String[] ss = tt.substring(7).split(":");
			
			for (int i = 0; i < players.size(); i++) {
				if (ss[0].trim().equals(players.get(i).username)) {
					players.get(i).setPosition(Float.parseFloat(ss[1]), Float.parseFloat(ss[2]), Float.parseFloat(ss[3]));
					players.get(i).setRotX(-Float.parseFloat(ss[4]));
					players.get(i).setRotY(Float.parseFloat(ss[5]));
					players.get(i).setRotZ(Float.parseFloat(ss[6]));
				}
			}
		}
	}

	@Override
	public void stop() {
		running = false;
		try {
			players = null;
			tcpSocket.shutdownInput();
			tcpSocket.shutdownOutput();
			tcpSocket.close();
			tcpSocket = null;
			udpSocket.disconnect();
			udpSocket.close();
			udpSocket = null;
		} catch (Exception e) {

		}
		System.out.println("Disconnected...");
	}

	@Override
	public void sendDataUdp(String data) {
		try {
			udpSocket.send(new DatagramPacket(data.getBytes(), data.getBytes().length, tcpSocket.getInetAddress(), Port));
		} catch (IOException e) {
			stop();
		}
	}

	@Override
	public void sendDataTcp(String data) {
		try {
			tcpSocket.getOutputStream().write(data.getBytes());
		} catch (IOException e) {
			stop();
		}
	}

	@Override
	public void tick() {
		Client c = this;
		new Thread() {
			@Override
			public void run() {
				while (running) {
					try {
						recieveTcp();
					} catch (IOException e) {
						c.stop();
					}
				}
			}
		}.start();
		new Thread() {
			@Override
			public void run() {
				while (running) {
					try {
						recieveUdp();
					} catch (IOException e) {
						c.stop();
					}
				}
			}
		}.start();
		new Thread() {
			@Override
			public void run() {
				long lastTime = System.nanoTime();
				double amountOfTicks = 60.0;
				double ns = 1000000000 / amountOfTicks;
				double delta = 0;
				while (running) {
					long now = System.nanoTime();
					delta += (now - lastTime) / ns;
					lastTime = now;
					while (delta >= 1) {
						try {
							sendToConnections();
						} catch (IOException e) {
							c.stop();
						}
						delta--;
					}

				}
			}
		}.start();
	}

}
