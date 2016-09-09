/*
 * menuItem�� ���õ� �̺�Ʈ�� actionPerformed���� �����ϱ�
 * */

package com.sds.movie.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

public class MyMenuBar extends JMenuBar implements ActionListener {
	JMenu[] menu;
	ArrayList<JMenuItem[]> menuItem;
	String[] menuList;
	ArrayList<String[]> menuItemList;

	public MyMenuBar(String[] menuList, ArrayList<String[]> menuItemList) {
		this.menuList = menuList;
		this.menuItemList = menuItemList;
		menu = new JMenu[menuList.length];
		menuItem = new ArrayList<JMenuItem[]>();
		for (int i = 0; i < menuList.length; i++) {
			// menu�� menuList�� text�� �־� �����Ѵ�.
			menu[i] = new JMenu(menuList[i]);
			// menu[i]��°�� menuItem[i]��° JmenuItem�� ���δ�.

			System.out.println(menuItemList.get(i));
			// JMenuItem[]�� menuItemList��[i]��° String[]�� �߰��Ѵ�.
			JMenuItem[] item = new JMenuItem[menuItemList.get(i).length];
			for (int j = 0; j < menuItemList.get(i).length; j++) {
				// System.out.println(menuItemList.get(i)[j]);
				System.out.println(menuItemList.get(i));
				item[j] = new JMenuItem(menuItemList.get(i)[j]);
				item[j].addActionListener(this);
			}

			menuItem.add(item);
			for (int j = 0; j < menuItem.get(i).length; j++) {
				menu[i].add(menuItem.get(i)[j]);
			}

			// menuBar�� menu���δ�.
			this.add(menu[i]);
		}

	}

	/*
	 * public MyMenuBar(String[] menuList){ this.menuList = menuList; menu = new
	 * JMenu[menuList.length]; for (int i = 0; i < menuList.length; i++) {
	 * menu[i] = new JMenu(menuList[i]); menu[i].addActionListener(this);
	 * this.add(menu[i]); } }
	 */
	// �޴��� ���� �̺�Ʈ ����
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj.equals(menuItem.get(0)[0])) {
			MainFrame.createDialog("��ȿ!");
		} else if (obj.equals(menuItem.get(3)[0])) {
			MainFrame.setAdminVisble(1);
			MainFrame.createDialog("��ȭ���ȭ��");
		} else if (obj.equals(menuItem.get(3)[1])) {
			MainFrame.setAdminVisble(0);
		} else if (obj.equals(menuItem.get(3)[2])) {
			MainFrame.setAdminVisble(2);
			MainFrame.createDialog("ȸ������ȭ��");
		}
	}

}
