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
		
		lb1 = new JLabel("영화예매", 0);
		lb2 = new JLabel("카드 번호");
		lb3 = new JLabel("카드 비번");
		price = new JLabel("현 결제금액 : " + pri);
		bt1 = new JButton("결제");
		bt2 = new JButton("취소");
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

	// 결제완료
	public void regist() {
		
		if (tf1.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "카드번호를 입력하세요");
		} else if (tf2.getPassword().length == 0) {
			JOptionPane.showMessageDialog(this, "카드비번을 입력하세요");

		} else if(new String(tf2.getPassword()).equals((String)MainFrame.getMyInfo().get("card_pwd"))){
			
			//카드 비밀번호 확인 동작
			DateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddHHmm");
			Calendar today = Calendar.getInstance(); // 오늘 날짜 가져올 객체
			String fileName =dateFormat2.format(today.getTime());
		//	System.out.println("11111"+MainFrame.getReserveInfo().get("movie_name")+".jpg");
			HashMap movie=(HashMap)MainFrame.getCineInfo().get((String)MainFrame.getReserveInfo().get("movie_name")+".jpg");
			
		//	System.out.println("1112222"+MainFrame.getCineInfo());
		//	System.out.println("1112222"+MainFrame.getCineInfo().get("터널.jpg"));
			
		//	System.out.println("22222"+movie);
			StringBuffer ticket_name = new StringBuffer();
			ticket_name.append(fileName);
			ticket_name.append((String)movie.get("movie_id"));
			ticket_name.append((String)MainFrame.getMyInfo().get("member_id"));
		//	System.out.println("33333"+MainFrame.getReserveInfo());
			String cinema = (String)MainFrame.getReserveInfo().get("cinema");
			String theater =(String)MainFrame.getReserveInfo().get("theater");
			
			//DB 저장하기(티켓 발급)
			reserveTicket(fileName+".jpg",ticket_name.toString(), cinema, theater);
			
			// pstmt.setString(2, tf2.getPassword());
			this.setVisible(false);
			JOptionPane.showMessageDialog(this, "즐거운 영화관람 되십시오");
			
		}else{
			JOptionPane.showMessageDialog(this, "카드 비밀 번호가 틀리셨습니다.");
		}
	}
	
	//예매 내용 DB 저장 및 티켓 발급 메서드  
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
				MainFrame.createDialog("티켓 발급중 에러 발생");
			}
			System.out.println(response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
	}
	//결제 취소시 예약된 좌석 정보를 취소 시키는 메서드
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
				MainFrame.createDialog("좌석을 다시 선택해주세요.");
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
