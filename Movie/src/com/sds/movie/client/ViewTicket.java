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
	JPanel p_top; //젤 위에 붙을 패널
	JPanel p_top_center;
	JLabel la_logo;
	JLabel la_ticket; // 티켓 번호
	JLabel la_reserveNum; // 예매번호
	JLabel la_movie; // 영화이름
	JLabel la_time; //영화 시간
	JLabel la_theater;  // 관 번째
	JLabel la_seat; // 좌석 번호
	ImageIcon icon;
	JLabel la_barcord; // 바코드 이미지 붙을 라벨
	JButton bt;
	Dimension dim = new Dimension(280, 20);

	public ViewTicket(String fileName,String ticket_num,String reserve_num) {
		setLayout(new FlowLayout(FlowLayout.CENTER));

		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd"); //날자 포멧
		Calendar today = Calendar.getInstance(); // 오늘 날짜 가져올 객체
		
		createBarcord(ticket_num, "C:/movieproject_res/user_res/",fileName);
		p_top = new JPanel(new BorderLayout());
		p_top_center = new JPanel();
		la_logo = new JLabel("GGV");
		la_ticket = new JLabel("티켓번호 : "+ticket_num);
		la_reserveNum = new JLabel("예매번호 : "+reserve_num);
		la_movie = new JLabel("영  화 : "+MainFrame.getReserveInfo().get("movie_name"));
		la_time = new JLabel("상영시간 : "+dateFormat.format(today.getTime())+" "+MainFrame.getReserveInfo().get("time"));
		la_theater = new JLabel("상영관 : "+MainFrame.getReserveInfo().get("theater"));
		la_seat = new JLabel("좌석 번호 : "+MainFrame.getReserveInfo().get("seat"));
		
		icon = new ImageIcon("C:/movieproject_res/user_res/"+fileName);
		la_barcord = new JLabel(icon);
		bt = new JButton("확인");
		
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
