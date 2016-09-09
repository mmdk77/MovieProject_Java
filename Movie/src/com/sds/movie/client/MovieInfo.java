package com.sds.movie.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MovieInfo extends JPanel implements ActionListener {
	// 전체 패널
	JPanel p_wrapper;
	JScrollPane w_scroll;

	// 이미지
	JPanel p_img, p_movieInfo;
	// 이미지 Inner 패널
	JLabel p_innerImg, p_producer, p_runtime, p_opendate, p_actor;

	// 정보
	JPanel p_info;
	// 정보 Inner 패널
	JPanel p_innerTitle, p_innerCon;

	// 정보관련 Container
	JLabel lb_title, lb_content; // 제목,내용
	JTextArea content;
	JScrollPane c_scroll;

	// 댓글
	JPanel p_comm;
	JPanel p_innerComm, p_comms;
	JTextField tf_comm;
	JButton bt_ok;

	//ViewComm vc, vc2, vc3;
	JScrollPane comm_scroll;

	ImageIcon icon;
	String img;

	public MovieInfo(String title) {

		// 전체Panel 생성
		p_wrapper = new JPanel();
		p_wrapper.setPreferredSize(new Dimension(380, 560)); // width, height는
																// 맞춰서 해야할듯.
		w_scroll = new JScrollPane(p_wrapper);

		// 이미지관련 생성
		p_img = new JPanel();
		p_movieInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p_movieInfo.setPreferredSize(new Dimension(160, 130));
		p_img.setPreferredSize(new Dimension(380, 130));
		p_innerImg = new JLabel(icon);

		p_producer = new JLabel();
		p_runtime = new JLabel();
		p_opendate = new JLabel();
		p_actor = new JLabel();

		// 영화정보관련 생성
		p_info = new JPanel();
		p_info.setBackground(Color.RED);
		p_innerTitle = new JPanel();

		p_innerTitle.setPreferredSize(new Dimension(0, 50));
		// p_innerTitle.setBackground(Color.DARK_GRAY);
		p_innerCon = new JPanel();

		// 댓글관련 생성
		p_comm = new JPanel();
		p_comm.setPreferredSize(new Dimension(380, 150));

		p_innerComm = new JPanel();
		tf_comm = new JTextField(20);
		bt_ok = new JButton("쓰기");

		bt_ok.addActionListener(this);

		// container 생성
		lb_title = new JLabel(title); // 영화제목
		lb_title.setFont(new Font("고딕", 40, 20));
		lb_content = new JLabel("줄거리"); // 영화 정보 -줄거리 Label
		content = new JTextArea(20, 30); // 영화 정보 -줄거리 내용
		content.setEditable(false); // 고침 x
		content.setLineWrap(true);
		c_scroll = new JScrollPane(content);

		// 전체 layout
		p_wrapper.setLayout(new BorderLayout());
		// p_wrapper.setLayout(new GridLayout(3, 1));
		// p_innerImg.setPreferredSize(new Dimension(200, 177));

		// 이미지 layout & 관련 container 추가
		p_img.setLayout(new GridLayout(0, 2));
		p_movieInfo.add(p_producer);
		p_movieInfo.add(p_actor);
		p_movieInfo.add(p_runtime);
		p_movieInfo.add(p_opendate);

		p_img.add(p_innerImg);
		p_img.add(p_movieInfo);

		// 영화정보패널 layout & 관련 container 추가
		p_info.setLayout(new BorderLayout());
		p_info.add(p_innerTitle, BorderLayout.NORTH);
		p_innerTitle.add(lb_title); // 제목
		p_info.add(p_innerCon, BorderLayout.CENTER);

		p_innerCon.add(lb_content, BorderLayout.NORTH); // 내용
		p_innerCon.add(c_scroll, BorderLayout.CENTER);

		// 댓글
		p_innerComm.add(tf_comm);
		p_innerComm.add(bt_ok);

		p_comm.setLayout(new BorderLayout()); // 댓글 패널 레이아웃 - 보더로 변경
		p_comm.add(p_innerComm, BorderLayout.NORTH);

		p_comms = new JPanel();
		p_comms.setPreferredSize(new Dimension(350, 400)); // 높이는 vc의 갯수만큼
															// 증가(변수로)

		comm_scroll = new JScrollPane(p_comms);
		p_comm.setBackground(Color.RED);
		p_comm.add(comm_scroll);

		// 추가
		p_wrapper.add(p_img, BorderLayout.NORTH);
		p_wrapper.add(p_info, BorderLayout.CENTER);
		p_wrapper.add(p_comm, BorderLayout.SOUTH);

		add(w_scroll);

		setSize(450, 650);
		
	}

	// 영화 정보 입력하는 메서드!!
	public void setInfo(String img) {
		this.img = img;
		HashMap info = (HashMap) MainFrame.getCineInfo().get(img);
		icon = new ImageIcon(MainFrame.imgPath + (String) info.get("movie_img"));
		icon.setImage(icon.getImage().getScaledInstance(100, 120, Image.SCALE_SMOOTH));// 이미지
																						// 크기
																						// 불러오기!
		p_innerImg.setIcon(icon);// 라벨에 이미지 넣기!!
		p_producer.setText("감독 : " + (String) info.get("producer"));
		p_actor.setText("출연배우 : " + (String) info.get("actor"));
		p_runtime.setText("상영시간 : " + (String) info.get("producer") + "분");
		p_opendate.setText("개봉일 : " + (String) info.get("opendate"));

		lb_title.setText((String) info.get("name"));
		lb_title.setFont(new Font("TimesRoman", Font.BOLD, 30));
		content.setText((String) info.get("story"));

	}
	
	public void viewComment(String img) {
		//댓글 보기
		HashMap imgInfo = (HashMap) MainFrame.getCineInfo().get(img);
		String movie_id = (String) imgInfo.get("movie_id");

		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append("\"request\":\"ViewComm\",");
		sb.append("\"movie_id\":\"" + movie_id + "\"");
		sb.append("}");

		try {
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();

			String msg = MainFrame.buffr.readLine();
			System.out.println("33333333" + msg);

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);
			JSONArray jsonArray = (JSONArray) jsonObject.get("data");

			p_comms.removeAll();
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject obj = (JSONObject) jsonArray.get(i);
				String txt = (String) obj.get("commtxt");
				long comm_id = (Long) obj.get("comm_id");
				

				ViewComm vc = new ViewComm(txt,img,comm_id,this);
				p_comms.add(vc);
				p_comms.updateUI();
			}
			p_comms.setPreferredSize(new Dimension(360, 110 * jsonArray.size()));

		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addComment(String img) {
		// 코멘트 추가
		String member_id = (String) MainFrame.getMyInfo().get("member_id");
		HashMap imgInfo = (HashMap) MainFrame.getCineInfo().get(img);
		String movie_id = (String) imgInfo.get("movie_id");
		String comment = tf_comm.getText();

		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append("\"request\":\"comment\",");
		sb.append("\"member_id\":\"" + member_id + "\",");
		sb.append("\"movie_id\":\"" + movie_id + "\",");
		sb.append("\"comment\":\"" + comment + "\"");
		sb.append("}");

		System.out.println("xxxxxxxxxx" + sb.toString());
		
			// 지역 요청
			try {
				MainFrame.buffw.write(sb.toString() + "\n");
				MainFrame.buffw.flush();

				// 응답
				String msg = MainFrame.buffr.readLine();
				System.out.println("vvvvvvvvvvvvvv" + msg);

				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);

				if (jsonObject.get("result").equals("ok")) {
					JOptionPane.showMessageDialog(this, "댓글쓰기가 완료되었습니다.");
					tf_comm.setText("");
					sb.delete(0, sb.length());
					viewComment(img);
				} else {
					System.out.println("에러");
					return;
				}
			
			} catch (IOException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	


	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == bt_ok && MainFrame.getMyInfo().size() != 0) {
			addComment(img);
		}else {
			JOptionPane.showMessageDialog(this, "로그인하세요");
			
		}
	}
}
