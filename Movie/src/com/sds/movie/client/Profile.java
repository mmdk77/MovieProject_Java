package com.sds.movie.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Profile extends JFrame implements ActionListener {
	JLabel la_img;
	JLabel la_id, la_id2, la_name, la_name2;
	JLabel la_regdate, la_regdate2;// ���Գ�¥
	JLabel la_pwd, la_pwdqa, la_pwdqa2, la_pwdas;
	JLabel la_cardnum, la_cardnum2,la_cardpwd;
	JTextField tf_id, tf_name, tf_pwdqa, tf_pwdas, tf_cardnum;
	JPasswordField tf_pwd, tf_cardpwd;
	JPanel p_north, p_center, p_south;
	JPanel p_id, p_name, p_cardnum, p_cardpwd,p_pwd, p_pwdqa, p_pwdas, p_regdate;

	ImageIcon icon;
	JButton bt_home, bt_mycgv, bt_discount, bt_info, bt_special, bt_event, bt_magagine, bt_service, bt_cs;

	JButton bt_change, bt_change1, bt_change2, bt_cardchange;
	JFileChooser chooser;
	FileInputStream fis;
	FileOutputStream fos;
	String savePath = "//M120226/movieproject_res/user_img/";
	File file = null;
	
	

	public Profile() {
		

		icon = new ImageIcon(this.getClass().getClassLoader().getResource("Profile.png"));
		setLayout(new BorderLayout());
		chooser = new JFileChooser("C:/");
		la_img = new JLabel(icon);
		la_img.setPreferredSize(new Dimension(150, 100));
		p_north = new JPanel();
		p_north.add(la_img);
		add(p_north, BorderLayout.NORTH);

		p_center = new JPanel();
		p_center.setLayout(new GridLayout(9, 0));

		p_id = new JPanel();
		p_name = new JPanel();
		p_cardnum = new JPanel();
		p_cardpwd = new JPanel();
		p_pwd = new JPanel();
		p_pwdqa = new JPanel();
		p_pwdas = new JPanel();
		p_regdate = new JPanel();
		

		la_id = new JLabel("���̵� : ");
		la_name = new JLabel("��    �� : ");
		la_regdate = new JLabel("���Գ�¥ :");
		la_pwd = new JLabel("���ο��� : ");
		la_pwdqa = new JLabel("������� : ");
		la_pwdqa2 = new JLabel();
		la_pwdas = new JLabel("��������� : ");
		la_cardnum = new JLabel("ī���ȣ : ");
		la_cardpwd = new JLabel("ī���� : ");
		// tf_pwdqa = new JTextField(10);
		tf_pwdas = new JTextField(10);
		tf_name = new JTextField(10);
		tf_pwd = new JPasswordField(10);
		la_cardnum2 = new JLabel();
		tf_cardpwd = new JPasswordField(10);

		la_id2 = new JLabel();
		la_name2 = new JLabel();
		la_regdate2 = new JLabel();
		bt_change = new JButton("�������");
		bt_change1 = new JButton("����");
		bt_cardchange = new JButton("ī�庯��");
		// bt_change2 = new JButton("����");

		p_id.add(la_id);
		p_id.add(la_id2);

		p_name.add(la_name);
		p_name.add(tf_name);
		p_name.add(bt_change1);
		p_regdate.add(la_regdate);
		p_regdate.add(la_regdate2);
		// p_cardnum.add(bt_change2);
		p_pwd.add(la_pwd);
		p_pwd.add(tf_pwd);
		p_pwdqa.add(la_pwdqa);
		p_pwdqa.add(la_pwdqa2);
		p_pwdas.add(la_pwdas);
		p_pwdas.add(tf_pwdas);
		p_pwdas.add(bt_change);
		
		p_cardnum.add(la_cardnum);
		p_cardnum.add(la_cardnum2);
		
		p_cardpwd.add(la_cardpwd);
		p_cardpwd.add(tf_cardpwd);
		p_cardpwd.add(bt_cardchange);

		p_center.add(p_id);
		p_center.add(p_name);
		p_center.add(p_regdate);
		p_center.add(p_pwd);
		p_center.add(p_pwdqa);
		p_center.add(p_pwdas);
		p_center.add(p_cardnum);
		p_center.add(p_cardpwd);

		add(p_center, BorderLayout.CENTER);

		/*
		 * URL url = this.getClass().getClassLoader().getResource("home.png");
		 * icon = new ImageIcon(url);
		 */

		bt_home = new JButton("Ȩ");
		bt_mycgv = new JButton("MyCGV");
		bt_discount = new JButton("��������");
		bt_info = new JButton("����");
		bt_special = new JButton("Ư����");
		bt_event = new JButton("�̺�Ʈ");
		bt_magagine = new JButton("�ް���");
		bt_service = new JButton("����");
		bt_cs = new JButton("������");

		p_south = new JPanel();
		p_south.setLayout(new GridLayout(3, 3));
		p_south.add(bt_home);
		p_south.add(bt_mycgv);
		p_south.add(bt_discount);
		p_south.add(bt_info);
		p_south.add(bt_special);
		p_south.add(bt_event);
		p_south.add(bt_magagine);
		p_south.add(bt_service);
		p_south.add(bt_cs);
		add(p_south, BorderLayout.SOUTH);
		p_south.setPreferredSize(new Dimension(400, 150));

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		la_img.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ChangePhoto();
			}

		});

		bt_change.addActionListener(this);
		bt_change1.addActionListener(this);
		bt_home.addActionListener(this);
		bt_cardchange.addActionListener(this);

		setVisible(true);
		setSize(400, 600);
		setBackground(Color.cyan);
		setBounds(500, 200, 400, 600);
		
		setTitle(MainFrame.getMyInfo().get("name")+"�� ��������");
		
		getProfileInfo();

	}

	public void getProfileInfo() {

		String id = (String) MainFrame.getMyInfo().get("id");
		String name = (String) MainFrame.getMyInfo().get("name");
		String regdate = (String) MainFrame.getMyInfo().get("regdate");
		String question = (String) MainFrame.getMyInfo().get("question");
		String img = (String) MainFrame.getMyInfo().get("member_img");
		
		String cardnum = (String) MainFrame.getMyInfo().get("card_num");
		//String cardpwd = (String) MainFrame.getMyInfo().get("card_pwd");

		icon = new ImageIcon(savePath+img);
		//icon.setImage(in););
			
		icon.setImage(icon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH));
		la_img.setIcon(icon);
		la_img.updateUI();
		
		la_id2.setText(id);
		tf_name.setText(name);
		la_regdate2.setText(regdate);
		la_pwdqa2.setText(question);
		la_cardnum2.setText(cardnum);		

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		String str = (String) MainFrame.getMyInfo().get("answer");
		
		String cardpwd = (String) MainFrame.getMyInfo().get("card_pwd");

		if (obj.equals(bt_change)) {
			System.out.println(str);
			if (str.equals(tf_pwdas.getText())) {
				ChangePwd();
			} else {
				JOptionPane.showMessageDialog(this, "��� ���� ����!! ���� Ȯ���� �ּ���");
			}

		} else if (obj.equals(bt_change1)) {
			ChangeName();

		}else if(obj.equals(bt_home)){			
			dispose();
		}else if(obj.equals(bt_cardchange)){
			if(cardpwd.equals(new String(tf_cardpwd.getPassword()))){
				JOptionPane.showMessageDialog(this, "ī�庯�� ȭ������ �̵�");
				ChangeCard changecard = new ChangeCard();
			}else{
				JOptionPane.showMessageDialog(this, "ī�庯�� ����");
			}
			
			
		}

	}

	public void ChangePhoto() {
		int result = chooser.showOpenDialog(getParent());
		File file = chooser.getSelectedFile();
		String filename = file.getName();

		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				// String path = file.getAbsolutePath();
				fis = new FileInputStream(file);
				fos = new FileOutputStream(savePath + "/" + filename);

				byte[] b = new byte[1024];
				int data;
				while ((fis.read(b) != -1)) {
					fos.write(b);
					fos.flush();
				}
				JOptionPane.showMessageDialog(getParent(), "��ϿϷ�");

				BufferedImage img = ImageIO.read(file);
				Image img2 = img.getScaledInstance(60, 80, Image.SCALE_SMOOTH);
				icon.setImage(img2);
				la_img.updateUI();

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fos != null) {

					try {
						fos.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
				if (fis != null) {
					
					try {
						fis.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

		}

		System.out.println(filename);
		String id = (String) MainFrame.getMyInfo().get("id");

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"request\" : \"changephoto\",");
		sb.append("\"id\" : \"" + id + "\",");
		sb.append("\"filename\" : \"" + filename + "\"");
		sb.append("}");

		System.out.println("1111211"+sb.toString());
		try {
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();

			String responce = MainFrame.buffr.readLine();
			System.out.println(responce);
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(responce);

			if (jsonObject.get("result").equals("ok")) {
				JOptionPane.showMessageDialog(this, "�������� �Ϸ�!");

			} else if (jsonObject.get("result").equals("fail")) {
				JOptionPane.showMessageDialog(this, "�������� ����!!");
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

	}

	public void ChangeName() {
		String id = (String) MainFrame.getMyInfo().get("id");
		String name = (String)tf_name.getText();

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"request\" : \"changename\",");
		sb.append("\"id\" : \"" + id + "\",");
		sb.append("\"name\" : \"" + name + "\"");
		sb.append("}");

		try {
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();

			String responce = MainFrame.buffr.readLine();
			System.out.println(responce);
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(responce);

			if (jsonObject.get("result").equals("ok")) {
				JOptionPane.showMessageDialog(this, "�̸� ���漺��");
			} else if (jsonObject.get("result").equals("fail")) {
				JOptionPane.showMessageDialog(this, "�̸� �������");
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

	}

	public void ChangePwd() {
		String id = (String) MainFrame.getMyInfo().get("id");
		String password = new String(tf_pwd.getPassword());

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"request\" : \"changepwd\",");
		sb.append("\"id\" : \"" + id + "\",");
		sb.append("\"password\" : \"" + password + "\"");
		sb.append("}");

		try {
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();

			String responce = MainFrame.buffr.readLine();
			System.out.println(responce);
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(responce);

			if (jsonObject.get("result").equals("ok")) {
				JOptionPane.showMessageDialog(this, "��� ���漺��");
			} else if (jsonObject.get("result").equals("fail")) {
				JOptionPane.showMessageDialog(this, "��� ���� ����!!");

			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		

	}
	
	
}
