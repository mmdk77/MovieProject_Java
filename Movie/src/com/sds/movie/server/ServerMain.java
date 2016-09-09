/*
서버 관리자가 쉽게 모니터링 하기위해 GUI 기반 서버로 ~~~
*/
package com.sds.movie.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerMain extends JFrame implements Runnable, ActionListener {
	JPanel p_north;
	JTextField t_port;
	JButton bt;
	JTextArea area;
	JScrollPane scroll;
	Thread acceptThread;
	ServerSocket server;
	int port = 8888;
	// 드라이버 경로
//	String driver = "oracle.jdbc.driver.OracleDriver";
	// 접속정보
//	String url = "jdbc:oracle:thin:@localhost:1521:XE";
//	String user = "javamovie";
//	String password = "javamovie";

	Connection con;

	public ServerMain() {
		p_north = new JPanel();
		t_port = new JTextField(Integer.toString(port), 5);
		bt = new JButton("실행");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		area.setBackground(Color.GREEN);

		p_north.add(t_port);
		p_north.add(bt);

		add(p_north, BorderLayout.NORTH);
		add(scroll);

		bt.addActionListener(this);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				disConnect();
			}

		});
		setBounds(800, 100, 400, 500);
		setVisible(true);
//테스팅 평하게 하기위해 잠시 옮김!!!! 자동 실행
		acceptThread = new Thread(this);
		acceptThread.start();
		bt.setEnabled(false);
		
	}

	// 접속자 감지!!!! (무한 루프)
	@Override
	public void run() {
		startServer();
	}

	// 서버 가동 메서드
	public void startServer() {
		try {
			server = new ServerSocket(port);
			area.append("서버생성완료\n");

			// 오라클 접속
			URL url = this.getClass().getClassLoader().getResource("com/sds/server/db/db.Property");
			FileInputStream fis=null;
			try {
				//propoties로 정의한 DB 정보 파일 위치알아오기
				fis = new FileInputStream(url.getPath());
				
				Properties properties = new Properties();
				properties.load(fis);
				String driver=properties.getProperty("driver");
				String host=properties.getProperty("url");
				String user=properties.getProperty("user");
				String password=properties.getProperty("password");
				
				
				System.out.println(host);
				Class.forName(driver); // 드라이버 로드
				con = DriverManager.getConnection(host, user, password);
				if (con != null) {
					setTitle("오라클 접속 성공");
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
			//	e.printStackTrace();
			}

			while (true) {
				Socket client = server.accept();
				String ip = client.getInetAddress().getHostAddress();
				ServerThread st = new ServerThread(this, client);
				st.start();
				area.append(ip + " 접속자 감지\n");
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}

	public void disConnect() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				//e.printStackTrace();
			}
			System.exit(-1);
		}
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		acceptThread = new Thread(this);
		acceptThread.start();
		bt.setEnabled(false);
	}

	public static void main(String[] args) {
		new ServerMain();
	}

}
