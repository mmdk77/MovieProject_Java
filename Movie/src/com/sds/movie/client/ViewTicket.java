package com.sds.movie.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ViewTicket extends JFrame implements ActionListener{
	JPanel p_top; //�� ���� ���� �г�
	JPanel p_top_center;
	JLabel la_logo;
	JLabel la_ticket; // Ƽ�� ��ȣ
	JLabel la_reserveNum; // ���Ź�ȣ
	JLabel la_movie; // ��ȭ�̸�
	JLabel la_time; //��ȭ �ð�
	JLabel la_theater;  // �� ��°
	JLabel la_seat; // �¼� ��ȣ
	ImageIcon icon;
	JLabel la_barcord; // ���ڵ� �̹��� ���� ��
	JButton bt;
	Dimension dim = new Dimension(280, 20);

	public ViewTicket(String fileName,String ticket_num,String reserve_num) {
		setLayout(new FlowLayout(FlowLayout.CENTER));

		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd"); //���� ����
		Calendar today = Calendar.getInstance(); // ���� ��¥ ������ ��ü
		
		createBarcord(ticket_num, "C:/movieproject_res/user_res/",fileName);
		p_top = new JPanel(new BorderLayout());
		p_top_center = new JPanel();
		la_logo = new JLabel("GGV");
		la_ticket = new JLabel("Ƽ�Ϲ�ȣ : "+ticket_num);
		la_reserveNum = new JLabel("���Ź�ȣ : "+reserve_num);
		la_movie = new JLabel("��  ȭ : "+MainFrame.getReserveInfo().get("movie_name"));
		la_time = new JLabel("�󿵽ð� : "+dateFormat.format(today.getTime())+" "+MainFrame.getReserveInfo().get("time"));
		la_theater = new JLabel("�󿵰� : "+MainFrame.getReserveInfo().get("theater"));
		la_seat = new JLabel("�¼� ��ȣ : "+MainFrame.getReserveInfo().get("seat"));
		
		icon = new ImageIcon("C:/movieproject_res/user_res/"+fileName);
		la_barcord = new JLabel(icon);
		bt = new JButton("Ȯ��");
		
		bt.addActionListener(this);
		
		p_top.setPreferredSize(new Dimension(280, 40));
		la_movie.setPreferredSize(dim);
		la_time.setPreferredSize(dim);
		la_theater.setPreferredSize(dim);
		la_movie.setPreferredSize(dim);
		la_seat.setPreferredSize(dim);
		
		la_ticket.setPreferredSize(new Dimension(200, 15));
		la_reserveNum.setPreferredSize(new Dimension(200, 15));
		p_top_center.add(la_ticket);
		p_top_center.add(la_reserveNum);
		
		p_top.add(la_logo,BorderLayout.WEST);
		p_top.add(p_top_center);

		add(p_top);
		add(la_movie);
		add(la_time);
		add(la_theater);
		add(la_seat);
		add(la_barcord);
		add(bt);
		
		setBounds(100, 100, 300, 450);
		setVisible(true);
	}
	
	public void createBarcord(String cord,String filePath,String fileName){
		Barcord barcord = new Barcord(cord, filePath,fileName);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
		MainFrame.setClientVisble(0);
	}
}
