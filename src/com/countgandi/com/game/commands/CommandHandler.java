package com.countgandi.com.game.commands;

import java.util.ArrayList;

import com.countgandi.com.net.server.Server;

public class CommandHandler {

	public static ArrayList<Command> commands = new ArrayList<Command>();
	private static Server server;

	// Add commands
	// let me please do this in alphabetical order? (if i can count letters 2+14=76)
	public static void init() {
		//ignore alphabetical for this help command for memes
		commands.add(new CommandHelp());

		commands.add(new CommandSay());
	}
	
	public static void init(Server server) {
		CommandHandler.server = server;
		init();
	}

	public static void doCommand(String cmd) {
		boolean flag = false;
		cmd = cmd.trim();
		String name = cmd.split(" ")[0].trim();
		String[] args = cmd.substring(name.length()).trim().split(" ");
		for (int i = 0; i < commands.size(); i++) {
			if (commands.get(i).toString().trim().equals(name)) {
				flag = true;
				if (!commands.get(i).performCommand(args, server)) {
					commands.get(i).error();
				}
			}
		}
		if (!flag) {
			System.out.println("Command does not exist: " + cmd);
		}
	}

}
