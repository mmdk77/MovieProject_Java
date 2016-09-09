package com.sds.movie.client;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RegistForm extends JPanel implements ActionListener {
	JTextField tf_id, tf_name, tf_add, tf_cardnum, tf_qa, tf_img;
	JTextField ta_add1, ta_add2;
	JButton bt_overlap, bt_find, bt_img, bt_enroll, bt_cancel;
	JLabel lb_tel, lb_title, lb_id, lb_pwd, lb_pwd2, lb_name, lb_add, lb_cardnum, lb_cardpwd, lb_qachoice, lb_qa,
			lb_img, la_img;
	JPanel p_title, p_id, p_pwd, p_pwd2, p_name, p_add, p_cardnum, p_cardpwd, p_qa, p_qachoice, p_img, p_bt;
	JPanel profile, profile_center;
	Choice ch_qa;
	CheckboxGroup chg;
	Checkbox ch_male, ch_female;
	JPasswordField tf_pwd, tf_pwd2, tf_cardpwd;
	JFileChooser chooser;
	Image img; // profile 이미지
	// AddressFrame addr;
	FileInputStream fis;
	FileOutputStream fos;
	String savePath = "//M120226/movieproject_res/user_img/";
	File file = null;
	ImageIcon icon;

	public RegistForm() {

		// GridLayout gr = new GridLayout(11, 2, 5, 20);
		// setLayout(gr);
		Dimension dim = new Dimension(150, 30);

		// p_title = new JPanel();
		lb_title = new JLabel("회원가입");
		lb_title.setPreferredSize(dim);
		add(lb_title, CENTER_ALIGNMENT);

		// add(p_title);

		lb_tel = new JLabel("안내문의 1588-7800");
		lb_tel.setPreferredSize(dim);
		add(lb_tel);

		p_id = new JPanel();
		lb_id = new JLabel("아이디(ID)");
		lb_id.setPreferredSize(dim);
		tf_id = new JTextField(10);
		bt_overlap = new JButton("중복");
		p_id.add(tf_id);
		bt_overlap.setPreferredSize(new Dimension(60, 30));
		p_id.add(bt_overlap);
		add(lb_id);
		add(p_id);

		// p_pwd = new JPanel();
		lb_pwd = new JLabel("패스워드(pwd)");
		lb_pwd.setPreferredSize(dim);
		tf_pwd = new JPasswordField(10);
		add(lb_pwd);
		add(tf_pwd);

		// p_pwd2 = new JPanel();
		lb_pwd2 = new JLabel("패스워드 확인");
		lb_pwd2.setPreferredSize(dim);
		tf_pwd2 = new JPasswordField(10);
		add(lb_pwd2);
		add(tf_pwd2);
		// add(p_pwd2);
		lb_name = new JLabel("성함");
		lb_name.setPreferredSize(dim);
		tf_name = new JTextField(10);
		add(lb_name);
		add(tf_name);

		// p_qachoice = new JPanel();
		lb_qachoice = new JLabel("pwd 찾기 질문선택");
		lb_qachoice.setPreferredSize(dim);
		ch_qa = new Choice();
		ch_qa.add("선 택 ▼");
		ch_qa.add("아버지 성함은?");
		ch_qa.add("가장 좋아하는 동물은?");
		ch_qa.add("졸업한 초등학교는?");
		add(lb_qachoice);
		add(ch_qa);
		// add(p_qachoice);

		// p_qa = new JPanel();
		lb_qa = new JLabel("질문답");
		lb_qa.setPreferredSize(dim);
		tf_qa = new JTextField(10);
		add(lb_qa);
		add(tf_qa);
		// add(p_qa);

		p_add = new JPanel();
		lb_add = new JLabel("주소");
		lb_add.setPreferredSize(dim);
		ta_add1 = new JTextField(10);
		ta_add2 = new JTextField(30);
		bt_find = new JButton("검색");

		// /ta_add.setPreferredSize(new Dimension(100, 20));
		ta_add1.setEditable(false);
		//ta_add2.setEditable(false);
		
		add(lb_add);
		add(ta_add1);
		add(bt_find);
		add(ta_add2);
		// add(p_add);

		p_cardnum = new JPanel();
		lb_cardnum = new JLabel("사용카드 번호");
		lb_cardnum.setPreferredSize(dim);
		tf_cardnum = new JTextField(10);
		lb_cardpwd = new JLabel("사용카드 비밀번호");
		lb_cardpwd.setPreferredSize(dim);
		tf_cardpwd = new JPasswordField(10);
		chg = new CheckboxGroup();
		ch_male = new Checkbox("남자", true, chg);
		ch_female = new Checkbox("여자", false, chg);
		add(lb_cardnum);
		add(tf_cardnum);
		add(lb_cardpwd);
		add(tf_cardpwd);
		// add(p_cardnum, LEFT_ALIGNMENT);

		// p_name = new JPanel();

		// add(p_name);

		profile = new JPanel();
		profile.setLayout(new BorderLayout());
		lb_img = new JLabel("이미지선택");
		lb_img.setPreferredSize(new Dimension(300, 30));

		icon = new ImageIcon(this.getClass().getClassLoader().getResource("profile.png"));
		profile_center = new JPanel();
		tf_img = new JTextField(20);
		bt_img = new JButton("찾기");
		profile.add(lb_img, BorderLayout.NORTH);
		la_img = new JLabel(icon);
		/*
		 * img = getImage("profile.png"); p_img = new JPanel() {
		 * 
		 * @Override public void paint(Graphics g) { g.drawImage(img, 0, 0,
		 * null); } };
		 */
		la_img.setPreferredSize(new Dimension(60, 80));
		profile.add(la_img, BorderLayout.WEST);
		profile_center.add(tf_img);
		profile_center.add(bt_img);
		profile.add(profile_center);
		add(profile);

		p_bt = new JPanel();
		bt_enroll = new JButton("등록");
		bt_cancel = new JButton("취소");
		p_bt.add(bt_enroll);
		p_bt.add(bt_cancel);
		add(p_bt);

		bt_overlap.addActionListener(this);
		bt_find.addActionListener(this);
		bt_img.addActionListener(this);
		bt_enroll.addActionListener(this);
		bt_cancel.addActionListener(this);

		chooser = new JFileChooser("C:/java_workspace/MovieProject/res");

		setSize(400, 600);
	}

	public void OpenFile() {
		int result = chooser.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION) {

			try {
				File file = chooser.getSelectedFile();
				String path = file.getAbsolutePath();
				String filename = file.getName();
				tf_img.setText(filename);
				fis = new FileInputStream(file = chooser.getSelectedFile());
				fos = new FileOutputStream(savePath + filename);

				byte[] b = new byte[1024];
				int data;
				while ((fis.read(b)) != -1) {
					fos.write(b);
					fos.flush();
				}
				JOptionPane.showMessageDialog(getParent(), "등록완료");

				BufferedImage img = ImageIO.read(file);
				Image img2 = img.getScaledInstance(60, 80, Image.SCALE_SMOOTH);// 이미지크기
																				// 변경!
				// System.out.println(img2.getWidth()+","+img2.getHeight());
				icon.setImage(img2);
				la_img.updateUI();

			} catch (IOException e1) {

				e1.printStackTrace();
			} finally {
				if (fos != null) {

					try {
						fos.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
			if (fis != null) {

				try {
					fis.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		String idstr = (String) tf_id.getText();
		String pwdstr = new String(tf_pwd.getPassword());
		String pwdstr2 = new String(tf_pwd2.getPassword());
		String namestr = (String) tf_name.getText();
		String addrstr = (String) ta_add1.getText();
		String addrstr1 = (String) ta_add2.getText();

		String imgstr = (String) tf_img.getText();
		String qastr = (String) tf_qa.getText();
		String cardnumstr = (String) tf_cardnum.getText();
		String cardpwdstr = new String(tf_cardpwd.getPassword());
		String addqastr = (String)ch_qa.getSelectedItem();

		if (obj.equals(bt_overlap)) {
			if (!idstr.equals("")) {
				System.out.println("입력값있음");
				idCertify();
			} else {
				JOptionPane.showMessageDialog(this, "아이디를 입력하세요!!");
			}
		} else if (obj.equals(bt_find)) {
			AddressFrame sub = new AddressFrame(this);

		} else if (obj.equals(bt_img)) {
			OpenFile();
		} else if (obj == bt_enroll) {
			String pwd = new String(tf_pwd.getPassword());
			String pwd2 = new String(tf_pwd2.getPassword());
			if (pwd.equals(pwd2)) {
				if (!idstr.equals("")) {
					if (!pwdstr.equals("") && !pwdstr2.equals("")) {
						if (!namestr.equals(""))
							if (!qastr.equals("")) {
								if (!addrstr.equals("") && !addrstr1.equals("")) {
									if (!cardnumstr.equals("")) {
										if (!cardpwdstr.equals("")) {
											if (!imgstr.equals("")) {
												if(!addqastr.equals("선 택 ▼")){
												

												enrollCheck();
												cardCheck();

												MainFrame.setMainPanel();
												}else{
													JOptionPane.showMessageDialog(this, "비밀번호 찾기 질문을 선택하세요!!");
												}
											} else {
												JOptionPane.showMessageDialog(this,"이미지를 등록해 주세요");
											}
										} else {
											JOptionPane.showMessageDialog(this,  "카드비밀번호를 등록해 주세요");
										}
									} else {
										JOptionPane.showMessageDialog(this,"카드번호를 등록해 주세요");
									}
								} else {
									JOptionPane.showMessageDialog(this,"주소를 입력해 주세요" );
								}
							} else {
								JOptionPane.showMessageDialog(this, "질문의 답을 등록해 주세요"); 
							}
						else {
							JOptionPane.showMessageDialog(this, "이름을 입력해 주세요");
						}
					} else {
						JOptionPane.showMessageDialog(this, "비밀번호를 입력해 주세요");
					}
				} else {
					JOptionPane.showMessageDialog(this, "아이디를 입력해 주세요");
				}
			} else {
				JOptionPane.showMessageDialog(this, "비밀번호를 다시 입력해 주세요");
			}
		} else if (obj.equals(bt_cancel)) {
			System.out.println("동작 확인");
			MainFrame.setMainPanel();
		}

	}

	// 이미지 얻어오는 메서드
	/*
	 * public Image getImage(String name) { System.out.println(name); Image img
	 * = null;// 지역변수는 컴파일러가 초기화 해주지 않으므로 , 반드시 개발자가 사용전에 초기화 하자!!! Class
	 * myClass = this.getClass(); // Classloader는 JVM의 static에서 원본 class 소스를 읽는
	 * 자이다. URL url = myClass.getClassLoader().getResource(name);
	 * System.out.println(url); try { img = ImageIO.read(url); } catch
	 * (IOException e) { } return img; }
	 */

	/*
	 * public void createProfile(String filename) { //수정요망!!! img =
	 * getImage(filename); img = img.getScaledInstance(60, 80,
	 * Image.SCALE_SMOOTH); p_img.repaint(); }
	 */
	public void idCertify() {

		String id = tf_id.getText();

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"request\" : \"idCertify\",");
		sb.append("\"id\" : \"" + id + "\"");
		sb.append("}");

		try {
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();

			String responce = MainFrame.buffr.readLine();
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(responce);

			if (jsonObject.get("result").equals("ok")) {
				JOptionPane.showMessageDialog(this, "사용가능 한 ID 입니다. 계속 진행시 확인을 눌러주세요");
			} else if (jsonObject.get("result").equals("fail")) {
				JOptionPane.showMessageDialog(this, "중복된 아이디입니다. 다시작성해주세요");
			}
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void enrollCheck() {

		String id = tf_id.getText();
		String password = new String(tf_pwd.getPassword());
		String name = tf_name.getText();
		int address = AddressFrame.ch_city.getSelectedIndex();
		String question = ch_qa.getSelectedItem();
		String questionanswer = tf_qa.getText();
		String image = tf_img.getText();

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"request\" : \"regist\",");
		sb.append("\"id\" : \"" + id + "\",");
		sb.append("\"password\" : \"" + password + "\",");
		sb.append("\"name\" : \"" + name + "\",");
		sb.append("\"address\" : \"" + address + "\",");
		sb.append("\"question\" : \"" + question + "\",");
		sb.append("\"questionanswer\" : \"" + questionanswer + "\",");
		sb.append("\"image\" : \"" + image + "\"");

		sb.append("}");

		try {
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();
			System.out.println(sb.toString());
			// String msg = sb.toString();

			String responce = MainFrame.buffr.readLine();
			System.out.println("read 성공");
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(responce);

			if (jsonObject.get("result").equals("ok")) {
				JOptionPane.showMessageDialog(this, "회원가입 성공");

			} else if (jsonObject.get("result").equals("fail")) {
				JOptionPane.showMessageDialog(this, "회원가입 실패");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public void cardCheck() {

		String cardnum = tf_cardnum.getText();
		String cardpwd = new String(tf_cardpwd.getPassword());
		String id = tf_id.getText();

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"request\" : \"cardcheck\",");
		sb.append("\"id\" : \"" + id + "\",");
		sb.append("\"cardnum\" : \"" + cardnum + "\",");
		sb.append("\"cardpwd\" : \"" + cardpwd + "\"");
		sb.append("}");

		try {
			System.out.println("card : " + sb.toString());
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();

			String responce = MainFrame.buffr.readLine();
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(responce);

			if (jsonObject.get("result").equals("ok")) {
				System.out.println("카드전송 완료!!");
			} else if (jsonObject.get("result").equals("fail")) {
				System.out.println("카드전송 실패!!");
			}
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
