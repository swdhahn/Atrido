package com.countgandi.com.net.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;

/**
 * 
 * @author Count
 *
 * Pretty much the {@link com.countgandi.com.net.client.Client Client} class, 
 * but it's for the server.
 *
 */
public class ClientConnection {
	
	private Socket socket;
	@SuppressWarnings("unused")
	private Server server;
	
	public ClientConnection(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
		
		new Thread() {
			@Override
			public void run() {
				while(Server.running) {
					tick();
				}
			}
		}.start();
	}
	
	public void tick() {
		
	}
	
	public void sendDataUdp(String data) {
		try {
			Server.udpSocket.send(new DatagramPacket(data.getBytes(), data.getBytes().length, socket.getInetAddress(), Server.Port));
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
		}
	}
	
	public void sendDataTcp(String data) {
		try {
			socket.getOutputStream().write(data.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
		}
	}
	
	public void disconnect() {
		
	}

}
