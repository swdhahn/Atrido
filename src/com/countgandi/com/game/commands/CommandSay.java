package com.countgandi.com.game.commands;

public class CommandSay extends Command {

	public CommandSay() {
		super("/say");
	}

	@Override
	public boolean performCommand(String[] arguments) {
		String ss = "";
		for (int i = 0; i < arguments.length; i++) {
			ss += arguments[i] + " ";
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
