package com.sds.movie.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ViewComm extends JPanel implements ActionListener {

	JPanel p_wrapper;

	JPanel p_area;
	JTextArea comments;
	JScrollPane a_scorll;

	JPanel p_bt;
	JButton bt_alert, bt_delete;

	String comment;
	String img;
	long comm_id;
	UpdateComm update;
	MovieInfo movieInfo;

	public ViewComm(String comment, String img, long comm_id, MovieInfo movieInfo) {

		// TODO Auto-generated constructor stub
		this.img = img;
		this.comm_id = comm_id;
		this.comment = comment;
		this.movieInfo = movieInfo;
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
		comments.setEditable(false);

		// 버튼
		p_bt = new JPanel();
		p_bt.setLayout(new FlowLayout());
		p_bt.setPreferredSize(new Dimension(100, 100));
		bt_alert = new JButton("수정");
		bt_alert.setPreferredSize(new Dimension(60, 30));
		bt_delete = new JButton("삭제");
		bt_delete.setPreferredSize(new Dimension(60, 30));

		// 댓글
		p_area.add(a_scorll);
		// 버튼
		p_bt.add(bt_alert);
		p_bt.add(bt_delete);

		p_wrapper.add(p_area, BorderLayout.CENTER);
		p_wrapper.add(p_bt, BorderLayout.EAST);

		add(p_wrapper);

		requestComm(comment);

		bt_alert.addActionListener(this);
		bt_delete.addActionListener(this);

	}

	public void requestComm(String comment) {
		comments.setText(comment);
	}

	public void deleteComment(String img, MovieInfo movieInfo) {
		this.movieInfo = movieInfo;
		// 댓글삭제
		String member_id = (String) MainFrame.getMyInfo().get("member_id");
		HashMap imgInfo = (HashMap) MainFrame.getCineInfo().get(img);
		String movie_id = (String) imgInfo.get("movie_id");
		String commtxt = comments.getText();

		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append("\"request\":\"deleteComm\",");
		sb.append("\"member_id\":\"" + member_id + "\",");
		sb.append("\"movie_id\":\"" + movie_id + "\",");
		sb.append("\"movie_id\":\"" + commtxt + "\",");
		sb.append("\"comm_id\":\"" + comm_id + "\"");
		sb.append("}");

		System.out.println("mmmmmmmmmmm" + sb.toString());

		try {
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();

			String msg = MainFrame.buffr.readLine();
			System.out.println("33333333" + msg);

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);

			if (jsonObject.get("result").equals("ok")) {
				JOptionPane.showMessageDialog(this, "댓글삭제가 완료되었습니다.");
				sb.delete(0, sb.length());
				movieInfo.viewComment(img);

			} else {
				System.out.println("에러");
				return;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();

		if (obj == bt_alert && MainFrame.getMyInfo().size() != 0) {
			// 수정
			update = new UpdateComm(img,comm_id,movieInfo);
			update.comments.setText(comment);
			

		} else if (obj == bt_delete && MainFrame.getMyInfo().size() != 0) {
			// 삭제
			deleteComment(img, movieInfo);

		}
	}

}