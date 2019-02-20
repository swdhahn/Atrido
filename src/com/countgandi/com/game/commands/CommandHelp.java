package com.countgandi.com.game.commands;

public class CommandHelp extends Command {

	public CommandHelp() {
		super("/help");
	}

	@Override
	public boolean performCommand(String[] arguments) {
		System.out.println("Help has been given: \n");
		for(int i = 0; i < CommandHandler.commands.size(); i++) {
			System.out.println("Command " + i + ": ");
			for(int j = 0; j < CommandHandler.commands.get(i).info().length; j++) {
				System.out.println("  " + CommandHandler.commands.get(i).info()[j]);
			}
		}
		return true;
	}

	@Override
	public String error() {
		return "This is very much impossible and no one will ever see this. Error just in case though.";
	}

	@Override
	public String[] info() {
		return new String[] {"/help", "to give halp"};
	}

}
