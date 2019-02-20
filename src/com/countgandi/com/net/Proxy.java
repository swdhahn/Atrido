package com.countgandi.com.net;

public interface Proxy {
	
	public void load();
	public void tick();
	public void stop();
	public void sendDataUdp(String data);
	public void sendDataTcp(String data);

}
