package com.countgandi.com.game.commands;

import com.countgandi.com.net.server.Server;

public class CommandSay extends Command {

	public CommandSay() {
		super("/say");
	}

	@Override
	public boolean performCommand(String[] arguments, Server server) {
		String ss = "";
		for (int i = 0; i < arguments.length; i++) {
			ss += arguments[i] + " ";
		}
		if(server != null) {
			server.sendDataTcp("Command:" + command + " " + ss);
		}
		System.out.println(ss);
		return true;
	}

	@Override
	public String[] info() {
		return new String[] { "/say text...", "prints text into chat" };
	}

	@Override
	public String error() {
		return "This is literally impossible... Error just in case though";
	}

}
