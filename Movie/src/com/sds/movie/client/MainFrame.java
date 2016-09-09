package com.sds.movie.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.sds.movie.admin.Management;
import com.sds.movie.admin.MemberManager;
import com.sds.movie.admin.MovieRegist;
import com.sds.movie.server.ServerMain;

public class MainFrame extends JFrame{
	public static final int WIDTH=450;
	public static final int HEIGHT=770;
	static String imgPath="//M120226/movieproject_res/movie_img/";
//	static String imgPath="c:/movieproject_res/movie_img/";
	static private JPanel p_north;
	static private JPanel p_center;
	//TopPlanel 관련
	static private MainTop mainTop;
	//Client 관련
	static MainPanel mainPanel;
	static RegistForm registForm;
	static MovieInfo movieInfo;
	static SelectMovie selectMovie;
	static CinemaSelect cinemaSelect;
	static BenchSelect benchSelect;
	static CardForm cardForm;
	static String[] movie=new String[4];//선택 영화 정보를 가지고 있는 배열 0.영화 1. 영화관 2. 상영관 3.시간
	static JPanel[] clientArr = new JPanel[6];
	static JPanel[] adminArr = new JPanel[3];
	static JScrollPane scroll;
	//Admin 관련
	static Management management;
	static MovieRegist movieRegist;
	static MemberManager memberManager;
	//서버 관련
	String ip="70.12.112.116";
//	String ip="192.168.0.3";
	int port=8888;
	public static Socket client;
	public static BufferedReader buffr; //입력용
	public static BufferedWriter buffw; //출력용

	//회원 정보 담을 컬렉션 프레임웍
	static private HashMap<String,String> myInfo = new HashMap<String,String>();
	//예매 정보 담을 컬렉션 프레임웍
	static private HashMap<String,HashMap> cineInfo = new HashMap<String,HashMap>();
	//예매정보 담을 컬렉션 프레임웍
	static private HashMap<String,String>reserveInfo = new HashMap<String,String>();
	static String title,cinema,theater,time;
	//이벤트 관련
	WindowAdapter windowAdapter = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			disConnect();
		}
	};
	
	public MainFrame() {
		connect();
		p_north = new JPanel();
		p_center = new JPanel();
		scroll = new JScrollPane(p_center,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		mainTop = new MainTop(430, 70);
		p_north.add(mainTop);
		
		mainPanel = new MainPanel(400, 500);
		registForm = new RegistForm();
		movieInfo = new MovieInfo("");
		selectMovie = new SelectMovie();
		cinemaSelect = new CinemaSelect(MainFrame.title);
		benchSelect = new BenchSelect(title+cinema,theater, time);
		
		//
		management = new Management();
		movieRegist = new MovieRegist();
		memberManager = new MemberManager();
		
		clientArr[0]=mainPanel;
		clientArr[1]=registForm;
		clientArr[2]=movieInfo;
		clientArr[3]=selectMovie;
		clientArr[4]=cinemaSelect;
		clientArr[5]=benchSelect;
		
		adminArr[0]=management;
		adminArr[1]=movieRegist;
		adminArr[2]=memberManager;
		
		//고객 관련 panel Disable
		for(int i=0; i < clientArr.length; i++){
			clientArr[i].setPreferredSize(new Dimension(400, 600));
			p_center.add(clientArr[i]);
			clientArr[i].setVisible(false);
		}
		//관리자 관련 panel Disable
		for(int i=0; i < adminArr.length; i++){
			adminArr[i].setPreferredSize(new Dimension(400, 600));
			p_center.add(adminArr[i]);
			adminArr[i].setVisible(false);
		}
		
	//	p_center.setLayout(new FlowLayout(FlowLayout.CENTER));
	//	p_center.setPreferredSize(new Dimension(400, 1000));
	
		clientArr[0].setVisible(true);
		add(p_north,BorderLayout.NORTH);
		add(scroll);
		addWindowListener(windowAdapter);
		
		setSize(WIDTH, HEIGHT);
		setVisible(true);
		
		//setManagement(400, 770);
	}
	
	public static void setClientVisble(int index){
		setAdminVisble();
		for(int i=0; i < clientArr.length; i++){
			p_center.add(clientArr[i]);
			clientArr[i].setVisible(false);
		}
		clientArr[index].setVisible(true);
		clientArr[index].updateUI();
	}
	
	public static void setClientVisble(){
		for(int i=0; i < clientArr.length; i++){
			p_center.add(clientArr[i]);
			clientArr[i].setVisible(false);
		}
	}
	
	public static void setAdminVisble(){
		for(int i=0; i < adminArr.length; i++){
			p_center.add(adminArr[i]);
			adminArr[i].setVisible(false);
		}
	}
	
	public static void setAdminVisble(int index){
		setClientVisble();
		management.getTableModel();
		memberManager.getTableModel();
		for(int i=0; i < adminArr.length; i++){
			p_center.add(adminArr[i]);
			adminArr[i].setVisible(false);
		}
		adminArr[index].setVisible(true);
		adminArr[index].updateUI();
	}
	
	
	
	public static void setMovieInfoPanel(String img){
		movieInfo.setInfo(img);
		movieInfo.viewComment(img);
		setClientVisble(2);
	}
	
	public static void setRegistPanel(){
		setClientVisble(1);
	}

	public static void setMainPanel(){
		mainPanel.getMovieInfo();
		setClientVisble(0);
	}
	
	public static void setMainSelectMovie(){
		selectMovie.removeAll();
		selectMovie.drawMovieList("JLabel");
		setClientVisble(3);
	}
	
	public static void setCinemaSelect(String title){
		System.out.println(title);
		MainFrame.title = title;
		System.out.println(MainFrame.title);
		cinemaSelect.ch_city.select(0);
		cinemaSelect.ch_cinema.select(0);
		cinemaSelect.lb_mTitle.setText(title);
		cinemaSelect.p_center.removeAll();
		cinemaSelect.p_center.updateUI();
		setClientVisble(4);
	}
	
	public static void setBenchSelect(String cinema, String theater, String time){
		MainFrame.cinema = cinema;
		MainFrame.theater = theater;
		MainFrame.time = time;
		benchSelect.getBenchInfo(theater);
		benchSelect.movieInfo.setText(title);
		benchSelect.movieInfo2.setText(cinema+theater);
		benchSelect.time.setText("영화 시간 : "+time);
		benchSelect.benchClear();//초기화
		setClientVisble(5);
		p_center.setPreferredSize(new Dimension(WIDTH, benchSelect.getHeight()));
	}

	/*
	public void setManagement(int width,int height){
		setAdminVisble(0);
		
		setSize(width, height);
		p_north.setSize(width, p_north.getHeight());
		p_north.setLayout(new FlowLayout(FlowLayout.LEFT));
	}
	
	public void setMovieRegist(){
		setAdminVisble(1);
	}
			*/
	public static void setCardForm(String pri,HashMap seatInfo,String theater){
		cardForm = new CardForm(pri,seatInfo,theater);
	}
	
	
	// 서버 연결 메서드(Socket 연결)
	public void connect(){
		try {
			client = new Socket(ip, port);
			if(client!=null){
				//System.out.println("접속 성공");
				//입출력 라인 구축
				buffr = new BufferedReader(new InputStreamReader(client.getInputStream()));
				buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
				
			}
		} catch (UnknownHostException e) {
			createDialog("잘못된ip입니다.");
			e.printStackTrace();
		} catch (IOException e) {
			createDialog("접속중 오류발생하였습니다.");
			e.printStackTrace();
		}
		
	}
	//서버 연결 종료 메서드(Socket 연결종료)
	public void disConnect(){
		try {
			client.close();
		} catch (IOException e) {
			createDialog("연결해제중 오류 발생.");
			e.printStackTrace();
		} finally {
			System.exit(-1);//프로세스 종료
		}
	}
	
	//JOptionPane.Dialog 메서드
	public static void createDialog(String msg){
		JOptionPane.showMessageDialog(p_center, msg);
	}
	
	//getter
	public static HashMap getCineInfo(){
		return cineInfo;
	}
	public static HashMap getMyInfo(){
		return myInfo;
	}
	public static HashMap getReserveInfo(){
		return reserveInfo;
	}	
	//setter
	public static void setCineInfo(String key,HashMap value){
		cineInfo.replace(key, value);
	}
	public static void setMyInfo(String key,String value){
		myInfo.replace(key,value);
	}
	public static void setReserveInfo(String key,String value){
		if(reserveInfo.get(key) != null){
			reserveInfo.replace(key,value);
		}else{
			reserveInfo.put(key, value);
		}
	}
	
	
	public static void main(String[] args){
	//	new ServerMain();
		new MainFrame();
	}
}
