package com.countgandi.com.net.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;

import org.lwjgl.util.vector.Vector3f;

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
	public int port;

	private Vector3f pos = new Vector3f(0, 0, 0), rot = new Vector3f(0, 0, 0);

	public ClientConnection(Socket socket) {
		this.socket = socket;
		
		ClientConnection c = this;
		new Thread() {
			@Override
			synchronized public void run() {
				byte[] bytes = new byte[128];
				try {
					socket.getInputStream().read(bytes);
					setUsername((new String(bytes).trim()).split(":")[1]);

					sendDataTcp("World:" + Server.SEED);
					sendTcpDataToAll("Connect:" + getUsername());
					
					try {
						this.wait(100);
					} catch (InterruptedException e) {
					}
					for (int i = 0; i < Server.clients.size(); i++) {
						if (!Server.clients.get(i).equals(c)) {
							sendDataTcp("Connect:" + Server.clients.get(i).username);
						}
					}

					System.out.println(getUsername() + " has connected.");
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
			}
		}.start();
	}

	public void recieveTcp() throws IOException {
		byte[] bytes = new byte[128];
		socket.getInputStream().read(bytes);
		String tt = new String(bytes).trim();
		System.out.println(tt);
	}

	public void recieveUdp(String string) throws IOException {
		if (string.startsWith("Update:")) {
			String[] ss = string.split(":");
			pos.x = Float.parseFloat(ss[1]);
			pos.y = Float.parseFloat(ss[2]);
			pos.z = Float.parseFloat(ss[3]);
			rot.x = Float.parseFloat(ss[4]);
			rot.y = Float.parseFloat(ss[5]);
			rot.z = Float.parseFloat(ss[6]);
			sendUdpDataToAll("Update:" + getUsername() + ":" + pos.x + ":" + pos.y + ":" + pos.z + ":" + rot.x + ":" + rot.y + ":" + rot.z);
		}
	}

	public void sendDataUdp(String data) {
		try {
			Server.udpSocket.send(new DatagramPacket(data.getBytes(), data.getBytes().length, socket.getInetAddress(), port));
		} catch (Exception e) {
			disconnect();
		}
	}

	public void sendDataTcp(String data) {
		try {
			socket.getOutputStream().write(data.getBytes());
		} catch (Exception e) {
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
		System.out.println(getUsername() + " has disconnected.");
		sendTcpDataToAll("Disconnect:" + getUsername());
		Server.clients.remove(this);
	}

	public void sendTcpDataToAll(String data) {
		for (int i = 0; i < Server.clients.size(); i++) {
			if (!Server.clients.get(i).equals(this)) {
				Server.clients.get(i).sendDataTcp(data);
			}
		}
	}

	public void sendUdpDataToAll(String data) {
		for (int i = 0; i < Server.clients.size(); i++) {
			if (!Server.clients.get(i).equals(this)) {
				Server.clients.get(i).sendDataUdp(data);
			}
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
