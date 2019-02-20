package com.countgandi.com.net.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JPanel;

import com.countgandi.com.game.commands.CommandHandler;
import com.countgandi.com.net.Proxy;

public class Server extends JPanel implements Proxy {
	private static final long serialVersionUID = 1L;

	public static int Port = 37767;
	public static int maxClients = 10;
	public static boolean running = false;

	public static ServerSocket tcpSocket;
	public static DatagramSocket udpSocket;

	public static ArrayList<ClientConnection> clients = new ArrayList<ClientConnection>();

	public Server() {
		running = true;
		CommandHandler.init();
		load();
		waitForClients();
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
		Server server = this;
		new Thread() {
			@Override
			public void run() {
				try {
					while (Server.running) {
						while (clients.size() <= maxClients) {
							clients.add(new ClientConnection(tcpSocket.accept(), server));
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
					CommandHandler.doCommand(s.nextLine().trim());
				}
				s.close();
			}
		}.start();
	}

	public static void main(String[] args) {
		new Server();
	}

}
