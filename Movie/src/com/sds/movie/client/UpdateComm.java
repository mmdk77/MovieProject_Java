package com.sds.movie.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UpdateComm extends JDialog implements ActionListener{

	JPanel p_wrapper;

	JPanel p_area;
	JTextArea comments;
	JScrollPane a_scorll;

	JPanel p_bt;
	JButton bt_ok, bt_cancel;
	
	ViewComm viewComm;
	
	MovieInfo movieInfo;
	String img;
	long comm_id;

	public UpdateComm(String img,long comm_id, MovieInfo movieInfo) {
		this.comm_id=comm_id;
		this.img=img;
		this.movieInfo=movieInfo;
		
		System.out.println(img);
		
		// 전체패널
		p_wrapper = new JPanel();
		p_wrapper.setLayout(new BorderLayout());
		p_wrapper.setPreferredSize(new Dimension(350, 95));


		// 댓글내용
		p_area = new JPanel();
		p_area.setLayout(new BorderLayout());
		p_area.setPreferredSize(new Dimension(180, 100));
		comments = new JTextArea();
		a_scorll = new JScrollPane(comments);
		comments.setEditable(true);

		// 버튼
		p_bt = new JPanel();
		p_bt.setLayout(new FlowLayout());
		p_bt.setPreferredSize(new Dimension(100, 100));
		bt_ok = new JButton("확인");
		bt_ok.setPreferredSize(new Dimension(60, 30));
		bt_cancel = new JButton("취소");
		bt_cancel.setPreferredSize(new Dimension(60, 30));

		// 댓글
		p_area.add(a_scorll);
		// 버튼
		p_bt.add(bt_ok);
		p_bt.add(bt_cancel);


		p_wrapper.add(p_area, BorderLayout.CENTER);
		p_wrapper.add(p_bt, BorderLayout.EAST);

		add(p_wrapper);
		this.setVisible(true);
		this.setSize(400,110);
		this.setResizable(false);
		
		
		bt_ok.addActionListener(this);
		bt_cancel.addActionListener(this);
		

	}
	
	public void updateComment(String img, MovieInfo movieInfo) {
	
		this.movieInfo= movieInfo;
		
		String member_id = (String) MainFrame.getMyInfo().get("member_id");
		HashMap imgInfo = (HashMap) MainFrame.getCineInfo().get(img);
		String movie_id = (String) imgInfo.get("movie_id");
		String commtxt = comments.getText();

		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append("\"request\":\"updateComm\",");
		sb.append("\"member_id\":\"" + member_id + "\",");
		sb.append("\"movie_id\":\"" + movie_id + "\",");
		sb.append("\"commtxt\":\"" + commtxt + "\",");
		sb.append("\"comm_id\":\"" + comm_id + "\"");
		
		sb.append("}");

				
		try {
			MainFrame.buffw.write(sb.toString() + "\n");
			System.out.println("uuuuuuuuuu" + sb.toString());
			MainFrame.buffw.flush();

			String msg = MainFrame.buffr.readLine();
			System.out.println("33333333" + msg);

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);

			if (jsonObject.get("result").equals("ok")) {
				JOptionPane.showMessageDialog(this, "댓글수정이 완료되었습니다.");
				sb.delete(0, sb.length());
				System.out.println(movieInfo+img);
				movieInfo.viewComment(img);
			} else {
				System.out.println("에러");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if(obj==bt_ok){
			//수정 
			updateComment(img,movieInfo);
			dispose();
		}else if(obj==bt_cancel){
			//취소
			dispose();
		}
		
	}

}