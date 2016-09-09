package com.sds.movie.admin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MemberManager extends JPanel {

	JPanel p_center;
	MemberTableModel model;
	JTable table;
	JScrollPane scroll;

	public MemberManager() {
		// TODO Auto-generated constructor stub

		p_center = new JPanel();
		//p_center.setPreferredSize(new Dimension(400, 600));
		model = new MemberTableModel();
		table = new JTable(model);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(table.getValueAt(table.getSelectedRow(), 0));
				String member_id = (String)table.getValueAt(table.getSelectedRow(), 0);
				String id=(String)table.getValueAt(table.getSelectedRow(), 1);
				String pwd=(String)table.getValueAt(table.getSelectedRow(), 2);
				String name=(String)table.getValueAt(table.getSelectedRow(), 3);
				String addr_id=(String)table.getValueAt(table.getSelectedRow(), 4);
				String question=(String)table.getValueAt(table.getSelectedRow(), 5);
				String answer= (String)table.getValueAt(table.getSelectedRow(), 6);
				String member_img=(String)table.getValueAt(table.getSelectedRow(), 7);
				String regdate=(String)table.getValueAt(table.getSelectedRow(), 8);
				MemberDetailInfo memberDetailInfo= new MemberDetailInfo(member_id, id, pwd, name, addr_id, question, answer, member_img, regdate);
			}
			
		});
		table.setPreferredScrollableViewportSize(new Dimension(400, 600));
		scroll = new JScrollPane(table);

		p_center.add(scroll);
		add(p_center,BorderLayout.CENTER);
		getTableModel();
	}
	
	
	
	public void getTableModel(){
		model.getMemberInfo();
		model.fireTableDataChanged();
		table.updateUI();
	}

/*	public static void main(String[] args) {
		JFrame frame = new JFrame();
		MemberManager mm = new MemberManager();
		frame.add(mm);
		frame.setVisible(true);
		frame.setSize(400, 600);

	}*/

}
