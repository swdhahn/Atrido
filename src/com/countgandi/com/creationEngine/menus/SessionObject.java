package com.countgandi.com.creationEngine.menus;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.countgandi.com.engine.renderEngine.terrain.Terrain;
import com.countgandi.com.engine.renderEngine.water.WaterTile;
import com.countgandi.com.game.entities.Light;

public class SessionObject extends JLabel {
	private static final long serialVersionUID = 1L;

	private Object object;
	private String title;
	private JPanel panel = new JPanel();

	public SessionObject(Object object) {
		this.object = object;
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(Color.DARK_GRAY);
		panel.setForeground(Color.WHITE);
		if (this.object instanceof Terrain) {
			title = "Terrain - x: " + ((Terrain) object).getX() + "; z:  " + ((Terrain) object).getZ() + ";";
			terrainPanel();
		} else if (this.object instanceof WaterTile) {
			title = "Water - x: " + ((WaterTile) object).getX() + "; z:  " + ((WaterTile) object).getZ() + ";";
			waterTilePanel();
		} else if (this.object instanceof Light) {
			title = "Light - " + ((Light) object).getLightType();
			lightPanel();
		}
	}

	public JPanel getBottomPanel() {
		return panel;
	}

	private JPanel terrainPanel() {
		panel.add(new JLabel("Terrain"));
		JTextField xpos = new JTextField(10);
		JTextField ypos = new JTextField(10);
		JPanel positionsPanel = new JPanel();
		positionsPanel.add(new JLabel("x: "));
		positionsPanel.add(xpos);
		positionsPanel.add(new JLabel("y: "));
		positionsPanel.add(ypos);
		
		panel.add(positionsPanel);
		return panel;
	}

	private JPanel waterTilePanel() {
		panel.add(new JLabel("Water Tile Stuffs"));
		return panel;
	}

	private JPanel lightPanel() {
		panel.add(new JLabel("Light Stuffs"));
		return panel;
	}

	@Override
	public String toString() {
		return title;
	}

}
