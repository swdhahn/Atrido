package com.countgandi.com.net.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;

/**
 * 
 * @author Count
 *
 *         Pretty much the {@link com.countgandi.com.net.client.Client Client}
 *         class, but it's for the server.
 *
 */
public class ClientConnection {

	private Socket socket;
	private String username = "";

	public ClientConnection(Socket socket) {
		this.socket = socket;

		byte[] bytes = new byte[128];
		try {
			socket.getInputStream().read(bytes);
			username = (new String(bytes).trim()).split(":")[1];
			System.out.println(username + " has connected.");
		} catch (IOException e) {
			disconnect();
			e.printStackTrace();
		}

		new Thread() {
			@Override
			public void run() {
				while (!socket.isClosed()) {
					try {
						recieveTcp();
					} catch (IOException e) {
						disconnect();
					}
				}
			}
		}.start();
		new Thread() {
			@Override
			public void run() {
				while (!socket.isClosed()) {
					try {
						recieveUdp();
					} catch (IOException e) {
						disconnect();
					}
				}
			}
		}.start();
	}

	public void recieveTcp() throws IOException {
		byte[] bytes = new byte[128];
		socket.getInputStream().read(bytes);
		String tt = new String(bytes).trim();
		System.out.println(tt);
	}

	public void recieveUdp() throws IOException {

	}

	public void sendDataUdp(String data) {
		try {
			Server.udpSocket.send(new DatagramPacket(data.getBytes(), data.getBytes().length, socket.getInetAddress(), Server.Port));
		} catch (IOException e) {
			disconnect();
		}
	}

	public void sendDataTcp(String data) {
		try {
			socket.getOutputStream().write(data.getBytes());
		} catch (IOException e) {
			disconnect();
		}
	}

	public void disconnect() {
		try {
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(username + " has disconnected.");
		sendTcpDataToAll("Disconnect:" + username);
		Server.clients.remove(this);
	}
	
	public void sendTcpDataToAll(String data) {
		for (int i = 0; i < Server.clients.size(); i++) {
			if(!Server.clients.get(i).equals(this)) {
				Server.clients.get(i).sendDataTcp(data);
			}
		}
	}
	public void sendUdpDataToAll(String data) {
		for (int i = 0; i < Server.clients.size(); i++) {
			if(!Server.clients.get(i).equals(this)) {
				Server.clients.get(i).sendDataUdp(data);
			}
		}
	}
}
