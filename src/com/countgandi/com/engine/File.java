package com.countgandi.com.engine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class File {

	private String[] text;
	private String title;
	private String extention;
	private String path;

	public File(String path) {
		this(path, new String[] { "" });
	}

	public File(String path, String... text) {
		this.title = path.split(".")[0].split("/")[path.split(".")[0].split("/").length - 1];
		this.extention = path.split(".")[1];
		this.text = text;
		this.path = path;
	}

	public void read() {
		try {
			FileInputStream stream = new FileInputStream(path);
			Scanner s = new Scanner(stream);
			ArrayList<String> strings = new ArrayList<String>();
			while (s.hasNextLine()) {
				strings.add(s.nextLine());
			}
			text = (String[]) strings.toArray();
			s.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not find file: " + path);
			e.printStackTrace();
		}
	}

	public void write(String... strings) {
		try {
			new java.io.File(path).mkdirs();
			FileOutputStream stream = new FileOutputStream(path);
			for (int i = 0; i < strings.length; i++) {
				stream.write(strings[i].getBytes());
			}
			stream.close();
		} catch (FileNotFoundException e) {
			System.err.println("Could not create file as it is not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Could not write  to file: " + path);
			e.printStackTrace();
		}
	}

	public void append(String... strings) {
		read();
		ArrayList<String> ss = new ArrayList<String>();
		for (int i = 0; i < text.length; i++) {
			ss.add(text[i]);
		}
		for (int i = 0; i < strings.length; i++) {
			ss.add(strings[i]);
		}
		write((String[]) ss.toArray());
	}

	public String[] getText() {
		return text;
	}

	public String getTitle() {
		return title;
	}

	public String getExtention() {
		return this.extention;
	}

	public String getPath() {
		return path;
	}

}
