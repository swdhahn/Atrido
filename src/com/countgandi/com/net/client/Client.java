package com.countgandi.com.net.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.countgandi.com.game.commands.CommandHandler;
import com.countgandi.com.net.Proxy;
import com.countgandi.com.net.server.Server;

public class Client implements Proxy {

	public static boolean running = false;
	public static int Port;
	public static String ipAddress;
	private static Socket tcpSocket;
	private static DatagramSocket udpSocket;
	private static String username;

	public Client(int port, String ipAddress, String username) {
		Client.Port = port;
		Client.ipAddress = ipAddress;
		Client.username = username;
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

		} catch (UnknownHostException e) {
			e.printStackTrace();
			stop();
		} catch (IOException e) {
			e.printStackTrace();
			stop();
		}
		tick();
	}

	public void recieveTcp() throws IOException {
		byte[] bytes = new byte[128];
		tcpSocket.getInputStream().read(bytes);
		String tt = new String(bytes).trim();
		
		if(tt.startsWith("Command:")) {
			CommandHandler.doCommand(tt.substring(8));
		} else if(tt.startsWith("Disconnect:")) {
			System.out.println(tt.substring(11) + " has disconnected.");
		} else if(tt.startsWith("Connect:")) {
			
		}
		
	}

	public void recieveUdp() throws IOException {

	}

	@Override
	public void stop() {
		running = false;
		try {
			tcpSocket.shutdownInput();
			tcpSocket.shutdownOutput();
			tcpSocket.close();
			tcpSocket = null;
			udpSocket.disconnect();
			udpSocket.close();
			udpSocket = null;
		} catch (Exception e) {
			e.printStackTrace();
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
	}

	public static void main(String[] args) {
		CommandHandler.init();
		new Client(Server.Port, "localhost", "hello");
	}

}
