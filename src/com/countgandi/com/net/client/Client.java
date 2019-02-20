package com.countgandi.com.net.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.countgandi.com.net.Proxy;

public class Client implements Proxy {
	
	public static int Port;
	public static String ipAddress;
	private Socket tcpSocket;
	private DatagramSocket udpSocket;
	
	public Client(int port, String ipAddress) {
		Client.Port = port;
		Client.ipAddress = ipAddress;
	}

	@Override
	public void load() {
		try {
			tcpSocket = new Socket(ipAddress, Port);
			udpSocket = new DatagramSocket(Port);
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			disconnect();
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
		}
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void stop() {
		
	}

	@Override
	public void sendDataUdp(String data) {
		try {
			udpSocket.send(new DatagramPacket(data.getBytes(), data.getBytes().length, tcpSocket.getInetAddress(), Port));
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
		}
	}

	@Override
	public void sendDataTcp(String data) {
		try {
			tcpSocket.getOutputStream().write(data.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
		}
	}
	
	public void disconnect() {
		
	}

}
