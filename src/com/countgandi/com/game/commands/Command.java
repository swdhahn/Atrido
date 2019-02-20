package com.countgandi.com.game.commands;

public abstract class Command {
	
	protected String command;
	
	public Command(String command) {
		this.command = command;
	}
	
	public abstract boolean performCommand(String[] arguments);
	public abstract String error();
	public abstract String[] info();
	
	@Override
	public String toString() {
		return command;
	}
}
