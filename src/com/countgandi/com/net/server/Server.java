package com.countgandi.com.net.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JPanel;

import com.countgandi.com.game.commands.CommandHandler;
import com.countgandi.com.net.Proxy;

public class Server extends JPanel implements Proxy {
	private static final long serialVersionUID = 1L;

	public static final int SEED = new Random().nextInt(1000000000);

	public static int Port = 37767;
	public static int maxClients = 10;
	public static boolean running = false;

	public static ServerSocket tcpSocket;
	public static DatagramSocket udpSocket;

	public static ArrayList<ClientConnection> clients = new ArrayList<ClientConnection>();

	public Server() {
		running = true;
		CommandHandler.init(this);
		load();
		acceptCommands();
	}

	@Override
	public void load() {
		try {
			tcpSocket = new ServerSocket(Port);
			udpSocket = new DatagramSocket(Port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		waitForClients();
		new Thread() {
			@Override
			public void run() {
				while(running) {
					try {
						recieveUdpPackets();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	@Override
	public void tick() {

	}

	@Override
	public void stop() {
		for (int i = 0; i < clients.size(); i++) {
			clients.get(i).disconnect();
		}
	}

	@Override
	public void sendDataUdp(String data) {
		for (int i = 0; i < clients.size(); i++) {
			clients.get(i).sendDataUdp(data);
		}
	}

	@Override
	public void sendDataTcp(String data) {
		for (int i = 0; i < clients.size(); i++) {
			clients.get(i).sendDataTcp(data);
		}
	}

	public void waitForClients() {
		new Thread() {
			@Override
			public void run() {
				try {
					while (Server.running) {
						while (clients.size() <= maxClients) {
							clients.add(new ClientConnection(tcpSocket.accept()));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void acceptCommands() {
		new Thread() {
			@Override
			public void run() {
				Scanner s = new Scanner(System.in);
				while (s.hasNextLine()) {
					String cmd = s.nextLine().trim();
					CommandHandler.doCommand(cmd);
				}
				s.close();
			}
		}.start();
	}
	
	public void recieveUdpPackets() throws IOException {
		DatagramPacket recievePacket = new DatagramPacket(new byte[128], 128);
		Server.udpSocket.receive(recievePacket);
		String s = new String(recievePacket.getData());
		for(int i = 0; i < clients.size(); i++) {
			ClientConnection client = clients.get(i);
			if(s.split("@")[0].equals(client.getUsername())) {
				client.port = recievePacket.getPort();
				client.recieveUdp(s.substring(client.getUsername().length() + 1));
				break;
			}
		}
	}

	public static void main(String[] args) {
		new Server();
	}

}
