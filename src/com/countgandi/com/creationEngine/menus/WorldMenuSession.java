package com.countgandi.com.creationEngine.menus;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.countgandi.com.creationEngine.Main;
import com.countgandi.com.game.Handler;

public class WorldMenuSession extends MenuSession {
	private static final long serialVersionUID = 1L;

	public WorldMenuSession(Handler handler) {
		super("World", handler);

		JPanel topPanel = new JPanel();
		JPanel bottomPanel = new JPanel();

		topPanel.setLayout(new GridLayout(1, 6, 0, 0));
		bottomPanel.setLayout(new GridLayout(1, 6, 0, 0));

		JList<SessionObject> list = new JList<SessionObject>(Main.worldList);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.setFixedCellWidth(800);
		list.setVisibleRowCount(10);

		JScrollPane listScroller = new JScrollPane(list);

		list.setForeground(Color.WHITE);
		list.setBackground(Color.DARK_GRAY);
		this.setBackground(Color.DARK_GRAY);
		this.setForeground(Color.WHITE);
		topPanel.setBackground(Color.DARK_GRAY);
		topPanel.setForeground(Color.WHITE);
		bottomPanel.setBackground(Color.DARK_GRAY);
		bottomPanel.setForeground(Color.WHITE);

		topPanel.add(listScroller);

		final JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.setForeground(Color.WHITE);
		popupMenu.setBackground(Color.DARK_GRAY);
		JMenuItem showInWorldItem = new JMenuItem("Show in World");
		JMenuItem editItem = new JMenuItem("Edit");
		JMenuItem deleteItem = new JMenuItem("Delete");
		
		popupMenu.add(showInWorldItem);
		popupMenu.add(editItem);
		popupMenu.add(new JPopupMenu.Separator());
		popupMenu.add(deleteItem);

		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e) && !list.isSelectionEmpty() && list.locationToIndex(e.getPoint()) == list.getSelectedIndex()) {
					popupMenu.show(list, e.getX(), e.getY());
				}
			}
		});

		JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
		
		showInWorldItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}});
		editItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bottomPanel.removeAll();
				bottomPanel.add(list.getSelectedValue().getBottomPanel());
				pane.updateUI();
			}});
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}});
		
		this.add(pane);
	}

}
