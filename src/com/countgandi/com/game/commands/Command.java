package com.countgandi.com.game.commands;

import com.countgandi.com.net.server.Server;

public abstract class Command {
	
	protected String command;
	
	public Command(String command) {
		this.command = command;
	}
	
	public abstract boolean performCommand(String[] arguments, Server server);
	public abstract String error();
	public abstract String[] info();
	
	@Override
	public String toString() {
		return command;
	}
}
