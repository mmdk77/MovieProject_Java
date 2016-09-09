package com.sds.movie.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sds.json.JsonProtocol;

public class MainTop extends JPanel implements ActionListener {
	JPanel p_center;
	JLabel lb_name;
	JLabel lb_user;
	JLabel lb_id;
	JLabel lb_pwd;
	JButton bt_item;
	JButton bt_login;
	JButton bt_regist;
	JButton bt_logout;
	JButton bt_profile;
	JTextField tf_id;
	JPasswordField tf_pwd;

	// menubar관련
	MyMenuBar bar;

	String[] menuClientList = { "무비차트", "아트하우스", "개봉예정" };
	// String[] menuAdminList;
	ArrayList<String[]> menuItemList = new ArrayList<String[]>();
	String[] move = { "예매율", "평점순"};
	String[] art = { };
	String[] open = {};
	String[] management = {"영화등록","영화관리","회원관리"};
	
	
	public MainTop(int width, int height) {
		setLayout(new BorderLayout());

		p_center = new JPanel();

		p_center.setLayout(null);
		p_center.setPreferredSize(new Dimension(width, height));
		// p_center.setBackground(Color.CYAN);

		drawIcon();

		add(p_center);
		// menuBar붙이기
		createMenubar(menuClientList,"Client");
	}

	// 메뉴 ICON만 나오게 하는 메서드
	public void drawIcon() {
		p_center.removeAll();
		lb_name = new JLabel("GGV");
		bt_item = new JButton("+");

		lb_name.setFont(new Font("Georgia", Font.BOLD, 20));
		bt_item.setFont(new Font("", Font.BOLD, 10));
		// 자리배치
		lb_name.setBounds(10, 10, 80, 30);
		bt_item.setBounds(330, 10, 40, 30);
		lb_name.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MainFrame.setMainPanel();
			}
		});
		// bt_item에 이벤트 처리하기
		bt_item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == "+") {
					drawLogin();
				} else if (e.getActionCommand() == "-") {
					drawIcon();
				}
			}
		});

		// 붙이기
		p_center.add(lb_name);
		p_center.add(bt_item);
		updateUI();
	}

	// 회원 가입 화면 나오게하는 메서드
	public void drawLogin() {
		bt_item.setText("-");
		bt_item.setBounds(100, 10, 40, 30);

		lb_id = new JLabel("ID");
		lb_pwd = new JLabel("password");
		tf_id = new JTextField(10);
		tf_pwd = new JPasswordField(10);
		bt_login = new JButton("로그인");
		bt_regist = new JButton("회원가입");
		bt_login.setFont(new Font("", Font.BOLD, 10));
		bt_regist.setFont(new Font("", Font.BOLD, 10));

		lb_id.setBounds(145, 10, 60, 30);
		lb_pwd.setBounds(145, 40, 60, 30);
		tf_id.setBounds(210, 10, 80, 30);
		tf_pwd.setBounds(210, 40, 80, 30);
		bt_login.setBounds(300, 10, 80, 30);
		bt_regist.setBounds(300, 40, 80, 30);

		bt_login.addActionListener(this);
		bt_regist.addActionListener(this);

		p_center.add(lb_id);
		p_center.add(lb_pwd);
		p_center.add(tf_id);
		p_center.add(tf_pwd);
		p_center.add(bt_login);
		p_center.add(bt_regist);

		updateUI();
	}

	// 로그인시 나올 화면 그리는 메서드
	public void drawUser(String name) {
		p_center.removeAll();
		lb_user = new JLabel(name + "님");
		bt_logout = new JButton("로그아웃");
		bt_profile = new JButton("프로필");
		bt_logout.setFont(new Font("", Font.BOLD, 10));
		bt_profile.setFont(new Font("", Font.BOLD, 10));
		lb_name.setBounds(10, 10, 80, 30);
		lb_user.setBounds(240, 10, 100, 30);
		bt_logout.setBounds(300, 15, 80, 20);
		bt_profile.setBounds(300, 35, 80, 20);

		p_center.add(lb_name);
		p_center.add(lb_user);
		p_center.add(bt_logout);
		p_center.add(bt_profile);

		bt_logout.addActionListener(this);
		bt_profile.addActionListener(this);

		updateUI();
	}

	public void createMenubar(String[] Arr, String who) {
		if (bar != null) {
			this.remove(bar);
		}
		menuItemList.removeAll(menuItemList);
		// menuBar관련
		menuItemList.add(move);
		menuItemList.add(art);
		menuItemList.add(open);
		
		if (who.equals("Client")) {
			
		} else if (who.equals("Admin")) {
			menuItemList.add(management);
		}
		bar = new MyMenuBar(Arr, menuItemList);
		bar.setPreferredSize(new Dimension(WIDTH, 30));
		add(bar, BorderLayout.SOUTH);
		bar.updateUI();
	}

	// login 연결 동작 메서드
	public void login() {
		String id = tf_id.getText();
		char[] password = tf_pwd.getPassword();
		String pwd = password.toString().valueOf(password);

		String[] request = { "request", "id", "password" };
		String[] msg = { "login", id, pwd };
		ArrayList<String[]> protocol = new ArrayList<String[]>();
		protocol.add(request);
		protocol.add(msg);

		String json = JsonProtocol.setJson(protocol);
		//MainFrame.createDialog(json);
		try {
			MainFrame.buffw.write(json + "\n");
			MainFrame.buffw.flush();

			String data = MainFrame.buffr.readLine();
			// JOptionPane.showMessageDialog(this,data);
			System.out.println(data);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(data);
			if (!jsonObject.get("responce").equals("login")) {
				data = MainFrame.buffr.readLine();
				jsonObject = (JSONObject) jsonParser.parse(data);
			}
			if (jsonObject.get("result").equals("ok")) {
				MainFrame.getMyInfo().clear();
				JSONObject obj = (JSONObject) jsonObject.get("data");
				String name = (String) obj.get("name");
				System.out.println(name);
				MainFrame.getMyInfo().clear();
				// 개인정보 ArrayList에 담아놓기
				MainFrame.getMyInfo().put("member_id", (String) obj.get("member_id"));
				MainFrame.getMyInfo().put("id", (String) obj.get("id"));
				MainFrame.getMyInfo().put("pwd", (String) obj.get("pwd"));
				MainFrame.getMyInfo().put("name", (String) obj.get("name"));
				MainFrame.getMyInfo().put("addr_id", (String) obj.get("addr_id"));
				MainFrame.getMyInfo().put("question", (String) obj.get("question"));
				MainFrame.getMyInfo().put("answer", (String) obj.get("answer"));
				MainFrame.getMyInfo().put("member_img", (String) obj.get("member_img"));
				MainFrame.getMyInfo().put("regdate", (String) obj.get("regdate"));
				
				//카드 정보 ArrayList에 담아놓기
				MainFrame.getMyInfo().put("card_id", (String) obj.get("card_id"));
				MainFrame.getMyInfo().put("card_num", (String) obj.get("card_num"));
				MainFrame.getMyInfo().put("card_pwd", (String) obj.get("card_pwd"));
				
				System.out.println(MainFrame.getMyInfo().toString());

				if (obj.get("id").equals("admin")) {
					String[] menuAdminList = {"무비차트", "아트하우스", "개봉예정" ,"관리" };
					createMenubar(menuAdminList,"Admin");
				} else {
					createMenubar(menuClientList,"Client");
				}
				drawUser(name);
			} else {
				MainFrame.createDialog("다시 입력해 주세요!!!");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void logout() {
		p_center.removeAll();
		MainFrame.getMyInfo().clear();
		createMenubar(menuClientList,"Client");
		MainFrame.setClientVisble(0);
		drawIcon();
		updateUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj.equals(bt_login)) {
			// JOptionPane.showMessageDialog(this, "로그인동작");
			System.out.println(tf_id.getText());
			// 빈값 예외처리!!
			if (!tf_id.getText().equals("") && tf_pwd.getPassword().length != 0) {
				login();
			} else {
				System.out.println("아이디와 패스워드를 입력해주세요!");
			}
		} else if (obj.equals(bt_regist)) {
			// JOptionPane.showMessageDialog(this, "회원가입동작");
			MainFrame.setRegistPanel();
		} else if (obj.equals(bt_logout)) {
			logout();
		} else if (obj.equals(bt_profile)) {
			Profile profile = new Profile();
			//MainFrame.createDialog("프로필 확인하러가기");
		}

	}
}
