package com.sds.movie.client;

import java.awt.ComponentOrientation;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ChangeCard extends JFrame implements ActionListener {

	JPanel p_title, p_cardnum, p_cardpwd, p_button;
	JTextField tf_cardnum;
	JPasswordField tf_cardpwd;
	JButton bt_enroll;
	JLabel la_title, la_cardnum, la_cardpwd;

	public ChangeCard() {

		GridLayout gr = new GridLayout(4, 0);
		setLayout(gr);
		p_title = new JPanel();
		p_cardnum = new JPanel();
		p_cardpwd = new JPanel();
		p_button = new JPanel();

		tf_cardnum = new JTextField(10);
		tf_cardpwd = new JPasswordField(10);

		la_title = new JLabel("ī�� ����");
		la_cardnum = new JLabel("���ο� ī���ȣ : ");
		la_cardpwd = new JLabel("���ο� ��й�ȣ : ");

		bt_enroll = new JButton("���");

		p_title.add(la_title);
		p_cardnum.add(la_cardnum);
		p_cardnum.add(tf_cardnum);
		p_cardpwd.add(la_cardpwd);
		p_cardpwd.add(tf_cardpwd);
		p_button.add(bt_enroll);

		add(p_title);
		add(p_cardnum);
		add(p_cardpwd);
		add(p_button);

		bt_enroll.addActionListener(this);
		
		setTitle(MainFrame.getMyInfo().get("name")+"�� ī�庯��");

		setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		setBounds(600, 200, 400, 200);
		setVisible(true);

	}

	public void ChangeCardNum() {
		String member_id = (String) MainFrame.getMyInfo().get("member_id");
		String card_num = tf_cardnum.getText();
		String card_pwd = new String(tf_cardpwd.getPassword());

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"request\" : \"changecardnum\",");
		sb.append("\"member_id\" : \"" + member_id + "\",");
		sb.append("\"card_pwd\" : \"" + card_pwd + "\",");
		sb.append("\"card_num\" : \"" + card_num + "\"");
		sb.append("}");

		try {
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();

			String responce = MainFrame.buffr.readLine();
			System.out.println(responce);
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(responce);

			if (jsonObject.get("result").equals("ok")) {
				JOptionPane.showMessageDialog(this, "ī���ȣ ������ �����߽��ϴ�. ����ȭ������ �̵��մϴ�.");
			} else if (jsonObject.get("result").equals("fail")) {
				JOptionPane.showMessageDialog(this, "ī�庯ȣ ���� ����!!");

			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		String cardnumstr = (String) tf_cardnum.getText();
		String cardpwdstr = new String(tf_cardpwd.getPassword());

		if (obj.equals(bt_enroll)) {
			if (!cardnumstr.equals("")) {
				if (!cardpwdstr.equals("")) {
					ChangeCardNum();

					dispose();
				} else {
					JOptionPane.showMessageDialog(this, "ī���й�ȣ�� �Է��� �ּ���!!");
				}
			} else {
				JOptionPane.showMessageDialog(this, "ī���ȣ�� �Է��� �ּ���!!");
			}
		} else {
			JOptionPane.showMessageDialog(this, "ī�庯�� ��� ����!!");

		}

	}

}
