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
	// ��ü �г�
	JPanel p_wrapper;
	JScrollPane w_scroll;

	// �̹���
	JPanel p_img, p_movieInfo;
	// �̹��� Inner �г�
	JLabel p_innerImg, p_producer, p_runtime, p_opendate, p_actor;

	// ����
	JPanel p_info;
	// ���� Inner �г�
	JPanel p_innerTitle, p_innerCon;

	// �������� Container
	JLabel lb_title, lb_content; // ����,����
	JTextArea content;
	JScrollPane c_scroll;

	// ���
	JPanel p_comm;
	JPanel p_innerComm, p_comms;
	JTextField tf_comm;
	JButton bt_ok;

	//ViewComm vc, vc2, vc3;
	JScrollPane comm_scroll;

	ImageIcon icon;
	String img;

	public MovieInfo(String title) {

		// ��üPanel ����
		p_wrapper = new JPanel();
		p_wrapper.setPreferredSize(new Dimension(380, 560)); // width, height��
																// ���缭 �ؾ��ҵ�.
		w_scroll = new JScrollPane(p_wrapper);

		// �̹������� ����
		p_img = new JPanel();
		p_movieInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p_movieInfo.setPreferredSize(new Dimension(160, 130));
		p_img.setPreferredSize(new Dimension(380, 130));
		p_innerImg = new JLabel(icon);

		p_producer = new JLabel();
		p_runtime = new JLabel();
		p_opendate = new JLabel();
		p_actor = new JLabel();

		// ��ȭ�������� ����
		p_info = new JPanel();
		p_info.setBackground(Color.RED);
		p_innerTitle = new JPanel();

		p_innerTitle.setPreferredSize(new Dimension(0, 50));
		// p_innerTitle.setBackground(Color.DARK_GRAY);
		p_innerCon = new JPanel();

		// ��۰��� ����
		p_comm = new JPanel();
		p_comm.setPreferredSize(new Dimension(380, 150));

		p_innerComm = new JPanel();
		tf_comm = new JTextField(20);
		bt_ok = new JButton("����");

		bt_ok.addActionListener(this);

		// container ����
		lb_title = new JLabel(title); // ��ȭ����
		lb_title.setFont(new Font("���", 40, 20));
		lb_content = new JLabel("�ٰŸ�"); // ��ȭ ���� -�ٰŸ� Label
		content = new JTextArea(20, 30); // ��ȭ ���� -�ٰŸ� ����
		content.setEditable(false); // ��ħ x
		content.setLineWrap(true);
		c_scroll = new JScrollPane(content);

		// ��ü layout
		p_wrapper.setLayout(new BorderLayout());
		// p_wrapper.setLayout(new GridLayout(3, 1));
		// p_innerImg.setPreferredSize(new Dimension(200, 177));

		// �̹��� layout & ���� container �߰�
		p_img.setLayout(new GridLayout(0, 2));
		p_movieInfo.add(p_producer);
		p_movieInfo.add(p_actor);
		p_movieInfo.add(p_runtime);
		p_movieInfo.add(p_opendate);

		p_img.add(p_innerImg);
		p_img.add(p_movieInfo);

		// ��ȭ�����г� layout & ���� container �߰�
		p_info.setLayout(new BorderLayout());
		p_info.add(p_innerTitle, BorderLayout.NORTH);
		p_innerTitle.add(lb_title); // ����
		p_info.add(p_innerCon, BorderLayout.CENTER);

		p_innerCon.add(lb_content, BorderLayout.NORTH); // ����
		p_innerCon.add(c_scroll, BorderLayout.CENTER);

		// ���
		p_innerComm.add(tf_comm);
		p_innerComm.add(bt_ok);

		p_comm.setLayout(new BorderLayout()); // ��� �г� ���̾ƿ� - ������ ����
		p_comm.add(p_innerComm, BorderLayout.NORTH);

		p_comms = new JPanel();
		p_comms.setPreferredSize(new Dimension(350, 400)); // ���̴� vc�� ������ŭ
															// ����(������)

		comm_scroll = new JScrollPane(p_comms);
		p_comm.setBackground(Color.RED);
		p_comm.add(comm_scroll);

		// �߰�
		p_wrapper.add(p_img, BorderLayout.NORTH);
		p_wrapper.add(p_info, BorderLayout.CENTER);
		p_wrapper.add(p_comm, BorderLayout.SOUTH);

		add(w_scroll);

		setSize(450, 650);
		
	}

	// ��ȭ ���� �Է��ϴ� �޼���!!
	public void setInfo(String img) {
		this.img = img;
		HashMap info = (HashMap) MainFrame.getCineInfo().get(img);
		icon = new ImageIcon(MainFrame.imgPath + (String) info.get("movie_img"));
		icon.setImage(icon.getImage().getScaledInstance(100, 120, Image.SCALE_SMOOTH));// �̹���
																						// ũ��
																						// �ҷ�����!
		p_innerImg.setIcon(icon);// �󺧿� �̹��� �ֱ�!!
		p_producer.setText("���� : " + (String) info.get("producer"));
		p_actor.setText("�⿬��� : " + (String) info.get("actor"));
		p_runtime.setText("�󿵽ð� : " + (String) info.get("producer") + "��");
		p_opendate.setText("������ : " + (String) info.get("opendate"));

		lb_title.setText((String) info.get("name"));
		lb_title.setFont(new Font("TimesRoman", Font.BOLD, 30));
		content.setText((String) info.get("story"));

	}
	
	public void viewComment(String img) {
		//��� ����
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
		// �ڸ�Ʈ �߰�
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
		
			// ���� ��û
			try {
				MainFrame.buffw.write(sb.toString() + "\n");
				MainFrame.buffw.flush();

				// ����
				String msg = MainFrame.buffr.readLine();
				System.out.println("vvvvvvvvvvvvvv" + msg);

				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);

				if (jsonObject.get("result").equals("ok")) {
					JOptionPane.showMessageDialog(this, "��۾��Ⱑ �Ϸ�Ǿ����ϴ�.");
					tf_comm.setText("");
					sb.delete(0, sb.length());
					viewComment(img);
				} else {
					System.out.println("����");
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
			JOptionPane.showMessageDialog(this, "�α����ϼ���");
			
		}
	}
}
