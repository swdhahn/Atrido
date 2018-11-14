package com.countgandi.com.game.entities;

import java.util.ArrayList;

public class GameRegistry {

	public static ArrayList<Class<? extends Entity>> entitiesRegistered = new ArrayList<Class<? extends Entity>>();
	
	public static void register() {
		registerEntity(TreeEntity.class);
	}

	public static void registerEntity(Class<? extends Entity> entity) {
		for(int i = 0; i < entitiesRegistered.size(); i++) {
			if(entity.getName() == entitiesRegistered.get(i).getName()) {
				System.err.println("Could not load entity with name: " + entity.getName() + "");
				System.err.println("Entity with the same name: " + entity.getName() + " exists\n");
				System.exit(1);
			}
		}
		try {
			entitiesRegistered.add(entity);
		} catch (Exception e) {
			System.err.println("Could not load entity with name: " + entity.getName() + "\n");
			e.printStackTrace();
		}

	}

}
