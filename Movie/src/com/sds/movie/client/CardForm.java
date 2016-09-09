package com.sds.movie.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

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

public class CardForm extends JFrame implements ActionListener {
	JTextField tf1;
	JPasswordField tf2;
	JButton bt1;
	JButton bt2;
	JPanel pl1;
	JPanel pl_ct1;
	JPanel pl_ct2;
	JPanel pl_ct3;
	JLabel lb1;
	JLabel lb2;
	JLabel lb3;
	JLabel price;
	HashMap seatInfo;
	String theater;

	public CardForm(String pri,HashMap seatInfo,String theater) {
		this.seatInfo = seatInfo;
		this.theater = theater;
		
		lb1 = new JLabel("��ȭ����", 0);
		lb2 = new JLabel("ī�� ��ȣ");
		lb3 = new JLabel("ī�� ���");
		price = new JLabel("�� �����ݾ� : " + pri);
		bt1 = new JButton("����");
		bt2 = new JButton("���");
		tf1 = new JTextField(15);
		tf2 = new JPasswordField(15);
		pl1 = new JPanel();
		pl_ct1 = new JPanel();
		pl_ct2 = new JPanel();
		pl_ct3 = new JPanel();
		pl_ct3.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		price.setFont(new Font("", Font.BOLD, 25));
		
		tf1.setText((String)MainFrame.getMyInfo().get("card_num"));
		tf1.setEnabled(false);
		
		pl1.add(bt1);
		pl1.add(bt2);
		pl_ct1.add(lb2);
		pl_ct1.add(tf1);
		pl_ct2.add(lb3);
		pl_ct2.add(tf2);
		pl_ct3.add(pl_ct1);
		pl_ct3.add(pl_ct2);
		pl_ct3.add(price);

		lb1.setPreferredSize(new Dimension(130, 20));
		lb2.setPreferredSize(new Dimension(70, 20));
		lb3.setPreferredSize(new Dimension(70, 20));
		bt1.setPreferredSize(new Dimension(80, 20));
		bt2.setPreferredSize(new Dimension(80, 20));
		bt1.addActionListener(this);
		bt2.addActionListener(this);

		add(lb1, BorderLayout.NORTH);
		add(pl_ct3);
		add(pl1, BorderLayout.SOUTH);

		setSize(350, 200);
		setVisible(true);

	}

	// �����Ϸ�
	public void regist() {
		
		if (tf1.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "ī���ȣ�� �Է��ϼ���");
		} else if (tf2.getPassword().length == 0) {
			JOptionPane.showMessageDialog(this, "ī������ �Է��ϼ���");

		} else if(new String(tf2.getPassword()).equals((String)MainFrame.getMyInfo().get("card_pwd"))){
			
			//ī�� ��й�ȣ Ȯ�� ����
			DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddHHmm");
			Calendar today = Calendar.getInstance(); // ���� ��¥ ������ ��ü
			String fileName =dateFormat2.format(today.getTime());
		//	System.out.println("11111"+MainFrame.getReserveInfo().get("movie_name")+".jpg");
			HashMap movie=(HashMap)MainFrame.getCineInfo().get((String)MainFrame.getReserveInfo().get("movie_name")+".jpg");
			
		//	System.out.println("1112222"+MainFrame.getCineInfo());
		//	System.out.println("1112222"+MainFrame.getCineInfo().get("�ͳ�.jpg"));
			
		//	System.out.println("22222"+movie);
			StringBuffer ticket_name = new StringBuffer();
			ticket_name.append(fileName);
			ticket_name.append((String)movie.get("movie_id"));
			ticket_name.append((String)MainFrame.getMyInfo().get("member_id"));
		//	System.out.println("33333"+MainFrame.getReserveInfo());
			String cinema = (String)MainFrame.getReserveInfo().get("cinema");
			String theater =(String)MainFrame.getReserveInfo().get("theater");
			
			//DB �����ϱ�(Ƽ�� �߱�)
			reserveTicket(fileName+".jpg",ticket_name.toString(), cinema, theater);
			
			// pstmt.setString(2, tf2.getPassword());
			this.setVisible(false);
			JOptionPane.showMessageDialog(this, "��ſ� ��ȭ���� �ǽʽÿ�");
			
		}else{
			JOptionPane.showMessageDialog(this, "ī�� ��� ��ȣ�� Ʋ���̽��ϴ�.");
		}
	}
	
	//���� ���� DB ���� �� Ƽ�� �߱� �޼���  
	public void reserveTicket(String fileName,String ticket_name,String cinema,String theater){
		String msg = "{\"request\":\"reserve\",\"fileName\":\""+fileName+"\","
				+ "\"ticket_name\":\""+ticket_name+"\", \"member_id\":\""+MainFrame.getMyInfo().get("member_id")+"\","
						+ "\"cinema\":\""+cinema+"\",\"theater\":\""+theater+"\"}";
		try {
			MainFrame.buffw.write(msg+"\n");
			MainFrame.buffw.flush();
			
			String response=MainFrame.buffr.readLine();
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject)jsonParser.parse(response);
			if(jsonObject.get("result1").equals("ok")&&jsonObject.get("result2").equals("ok")){
				new ViewTicket(fileName,ticket_name, ticket_name);
			}else{
				MainFrame.createDialog("Ƽ�� �߱��� ���� �߻�");
			}
			System.out.println(response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
	}
	//���� ��ҽ� ����� �¼� ������ ��� ��Ű�� �޼���
	public void setSeatsInfo(String theater) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"request\" : \"sitbool2\",\"data\":[");
		for (int i = 0; i < seatInfo.size(); i++) {
			sb.append("{\"sit_bool\" : \"0\",\"theater\":\"" + theater + "\",\"sitName\":\"" + seatInfo.get(i + 1) + "\"");
			if (i != (seatInfo.size() - 1)) {
				sb.append("},");
			} else {
				sb.append("}");
			}
		}
		sb.append("]}");

		System.out.println(sb.toString());

		try {
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();

			String responce = MainFrame.buffr.readLine();
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(responce);
			
			if (jsonObject.get("result").equals("ok")) {

			} else {
				MainFrame.createDialog("�¼��� �ٽ� �������ּ���.");
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
		if(obj==bt1){
			regist();
		}else if(obj==bt2){
			setSeatsInfo(theater);
			this.setVisible(false);
		}
	}
}
