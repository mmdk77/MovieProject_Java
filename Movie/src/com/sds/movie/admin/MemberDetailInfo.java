package com.sds.movie.admin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sds.movie.client.MainFrame;

public class MemberDetailInfo extends JFrame implements ActionListener {
	JPanel p_center;
	JPanel p_detail;

	ImageIcon icon;
	JLabel img; // 이미지
	JLabel lb_member_id;
	JLabel id;
	JLabel pwd;
	JLabel name;
	JLabel addr_id;
	JLabel question;
	JLabel answer;
	JLabel member_img;
	JLabel regdate;

	JTextField t_member_id;
	JTextField t_id;
	JTextField t_pwd;
	JTextField t_name;
	JTextField t_addr_id;
	JTextField t_question;
	JTextField t_answer;
	JTextField t_member_img;
	JTextField t_regdate;

	JButton bt_bt_update;
	JButton bt_bt_delete;
	
	JFileChooser chooser = new JFileChooser("C:/Users/student/Downloads/");
	String path = "//M120226/movieproject_res/user_img/";
	FileInputStream fis;
	FileOutputStream fos;
	String movie_img;
	
	String member_id;

	public MemberDetailInfo(String member_id, String id, String pwd, String name, String addr_id, String question,
			String answer, String member_img, String regdate) {
		this.member_id=member_id;
		
		p_center = new JPanel();
		p_detail = new JPanel(new BorderLayout());
		System.out.println("111111111" + member_img);
		movie_img = member_img;
		icon = new ImageIcon(path + member_img);
		icon.setImage(icon.getImage().getScaledInstance(120, 129, Image.SCALE_SMOOTH));
		img = new JLabel(icon);


		this.lb_member_id = new JLabel("Member_ID");
		this.id = new JLabel("ID");
		this.pwd = new JLabel("Password");
		this.name = new JLabel("Name");
		this.addr_id = new JLabel("주소");
		this.question = new JLabel("질문");
		this.answer = new JLabel("답");
		this.member_img = new JLabel("프로필 이미지");
		this.regdate = new JLabel("가입날짜");

		t_member_id = new JTextField(member_id, 10);
		t_id = new JTextField(id, 10);
		t_pwd = new JTextField(pwd, 10);
		t_name = new JTextField(name, 10);
		t_addr_id = new JTextField(addr_id, 10);
		t_question = new JTextField(question, 10);
		t_answer = new JTextField(answer, 10);
		t_member_img = new JTextField(member_img, 10);
		t_regdate = new JTextField(regdate, 10);

		bt_bt_update = new JButton("수정");
		bt_bt_delete = new JButton("삭제");

		t_member_id.setEnabled(false);
		t_id.setEnabled(false);

		t_name.setEnabled(false);
		t_addr_id.setEnabled(false);
		t_question.setEnabled(false);
		t_answer.setEnabled(false);
		t_member_img.setEnabled(false);
		t_regdate.setEnabled(false);

		img.setPreferredSize(new Dimension(200, 200));
		this.lb_member_id.setPreferredSize(new Dimension(120, 30));
		this.id.setPreferredSize(new Dimension(120, 30));
		this.pwd.setPreferredSize(new Dimension(120, 30));
		this.name.setPreferredSize(new Dimension(120, 30));
		this.addr_id.setPreferredSize(new Dimension(120, 30));
		this.question.setPreferredSize(new Dimension(120, 30));
		this.answer.setPreferredSize(new Dimension(120, 30));
		this.member_img.setPreferredSize(new Dimension(120, 30));
		this.regdate.setPreferredSize(new Dimension(120, 30));

		p_detail.setPreferredSize(new Dimension(280, 100));

		p_center.add(img);
		p_center.add(this.lb_member_id);
		p_center.add(t_member_id);
		p_center.add(this.id);
		p_center.add(t_id);
		p_center.add(this.pwd);
		p_center.add(t_pwd);
		p_center.add(this.name);
		p_center.add(t_name);
		p_center.add(this.addr_id);
		p_center.add(t_addr_id);
		p_center.add(this.question);
		p_center.add(t_question);
		p_center.add(this.answer);
		p_center.add(t_answer);
		p_center.add(this.member_img);
		p_center.add(t_member_img);
		p_center.add(this.regdate);
		p_center.add(t_regdate);

		p_center.add(bt_bt_update);
		p_center.add(bt_bt_delete);

		add(p_center);

		// setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(400, 0, 300, 700);
		setVisible(true);

		bt_bt_delete.addActionListener(this);
		bt_bt_update.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj==bt_bt_update){
			MemberUpdate();
		}else if(obj==bt_bt_delete){
			deleteMember();
		}
		
	}

	// 서버로 update 정보 전송 메서드
	public void MemberUpdate() {
		
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append("\"request\": \"memberUpdate\",");
		sb.append("\"member_id\": \""+member_id+"\"");

		sb.append("\"pwd\": \"" + t_pwd.getText() + "\"");

		sb.append("}");
		System.out.println(sb.toString());

		try {
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();

			String responce = MainFrame.buffr.readLine();
			System.out.println(responce);

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(responce);
			if (jsonObject.get("responce").equals("memberUpdate")) {
				if (jsonObject.get("result").equals("ok")) {
					MainFrame.createDialog("패스워드변경 성공");
					MainFrame.setAdminVisble(2);
					setVisible(false);
				} else {
					MainFrame.createDialog("패스워드변경 실패");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void deleteMember() {
		String json = "{\"request\":\"member_delete\",\"member_id\":\"" + member_id + "\"}";

		try {
			MainFrame.buffw.write(json + "\n");
			MainFrame.buffw.flush();

			String responce = MainFrame.buffr.readLine();
			System.out.println(responce);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(responce);
			if (jsonObject.get("responce").equals("member_delete") && jsonObject.get("result").equals("ok")) {
				MainFrame.createDialog("해당 회원이 삭제 되었습니다.");
				MainFrame.setAdminVisble(2);				
				setVisible(false);
			} else {
				MainFrame.createDialog("해당 회원 삭제를 실패하였습니다.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println(json);

	}

	/*
	 * public static void main(String[] args){ MemberDetailInfo mdi=new
	 * MemberDetailInfo("1", "1", "1", "1", "1", "1", "1", "1", "1"); }
	 */

}
