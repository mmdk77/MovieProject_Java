package com.sds.movie.client;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BenchSelect extends JPanel implements ActionListener {
	Choice adultTicket; // 성인
	Choice childTicket; // 학생

	JPanel panel, seat;
	public JLabel movieInfo,movieInfo2;
	JLabel selectTime;
	JLabel name;
	public JLabel time;
	JLabel adult;
	JLabel child;
	JLabel screen;
	JLabel seats[];
	JLabel price;
	JTextField priceLabel;
	JButton next;
	boolean[] select;
	ArrayList<String> ticket;
	static int member = 0; // member는 하나씩 감소하면서 0이되면 좌석이 전부 다 선택됨
	static int ticketNum; 
	int ab;
	int ch;
	int ssum;
	ArrayList<String> benchInfo = new ArrayList<String>();
	int[] sit_bool;
	HashMap<Integer, String> seatInfo = new HashMap<Integer, String>();
	String theater;
	boolean flag;
	
	
	public BenchSelect(String info, String theater, String movieTime) {
		setSize(400, 600);
		this.setLayout(null);

		time = new JLabel("상영시간 :" + movieTime);
		adult = new JLabel("어    른 :");
		child = new JLabel("청소년 :");
		price = new JLabel("가    격 :");

		movieInfo = new JLabel(); // 초이스 객체를 생성한다.
		movieInfo2 = new JLabel(info + theater);
		
		adultTicket = new Choice(); // 여른표 구매수를 입력받기 위한 텍스트필드
		childTicket = new Choice(); // 청소년표 구매수를 입력받기 위한 텍스트필드
		priceLabel = new JTextField();

		for (int i = 0; i < 5; i++) { // 인원수 최대 4명
			adultTicket.add(String.valueOf(i));
			childTicket.add(String.valueOf(i));
		}

		adultTicket.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				ab = Integer.parseInt((String) e.getItem());
				benchReset();	
			}
		});
		childTicket.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				ch = Integer.parseInt((String) e.getItem());
				benchReset();				
			}
		});

		movieInfo.setFont(new Font("", Font.BOLD, 25));
		movieInfo2.setFont(new Font("", Font.BOLD, 25));
		time.setFont(new Font("", Font.BOLD, 15));
		movieInfo.setBounds(150, 0, 200, 30);
		movieInfo2.setBounds(100, 35, 300, 30);
		time.setBounds(20, 65, 150, 30);
		adult.setBounds(20, 100, 70, 30);
		adultTicket.setBounds(100, 103, 40, 30);
		child.setBounds(20, 140, 70, 30);
		childTicket.setBounds(100, 143, 40, 30);

		screen = new JLabel("SCREEN");
		screen.setBackground(Color.white);
		screen.setOpaque(true); // 이속성을 true로 해주면 background color가 적용됨

		next = new JButton("결제");
		ticket = new ArrayList<String>(); //가져온 정보를 저장하고 예매가 완료되면 지금 예매된 정보를 add함

		seat = new JPanel(new GridLayout(8, 10)); // 영화 좌석정보를 panel로 했는데
													// GridLayout으로 잡고 8x10 좌석을 만듦
		seats = new JLabel[80]; // 영화 좌석에 번호를 지정할 라벨, background color를 바꿔서 좌석선택을 표시
		select = new boolean[80]; // 현재 선택한 놈들임 80번자리가 선택됫으면 select[80] = true임

		seat.setBackground(Color.white);
		seat.setOpaque(true);

		screen.setBounds(20, 190, 350, 25);
		screen.setHorizontalAlignment(SwingConstants.CENTER);

		// widget들 좌표지정
		seat.setBounds(20, 230, 350, 200);
		price.setBounds(20, 480, 70, 30);
		priceLabel.setBounds(90, 480, 150, 30);
		next.setBounds(280, 480, 90, 30);

		priceLabel.setEditable(false);
		
		add(time);
		add(adult);
		add(child);
		add(movieInfo);
		add(movieInfo2);
		add(adultTicket);
		add(childTicket);
		add(priceLabel);
		add(seat);
		add(screen);
		add(next);
		add(price);

		next.addActionListener(this);
	}
	// DB에서 좌석 정보 불러오기
	public void getBenchInfo(String theater) {
		benchInfo.removeAll(benchInfo);
		this.theater = theater;
		String json = "{\"request\":\"benchInfo\"," + "\"data\": \"" + theater + "\"}";
		try {
			MainFrame.buffw.write(json + "\n");
			MainFrame.buffw.flush();
			String result = MainFrame.buffr.readLine();

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
			if (jsonObject.get("result").equals("ok")) {
				JSONArray jsonArr = (JSONArray) jsonObject.get("data");
				int count = 0;
				sit_bool = new int[jsonArr.size()];
				for (int i = 0; i < jsonArr.size(); i++) {
					JSONObject obj = (JSONObject) jsonArr.get(i);
					sit_bool[i] = Integer.parseInt((String) obj.get("sit_bool"));
					benchInfo.add((String) obj.get("sit_name"));
				}
				//MainFrame.createDialog("좌석정보 얻어오기를 완료하였습니다.");
				drawbench();
				this.updateUI();
			} else if (jsonObject.get("result").equals("fail")) {
				MainFrame.createDialog("좌석정보 얻어오기가 실패하였습니다.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	// 좌석 그리기
	public void drawbench() {
		seat.removeAll();
		//System.out.println(benchInfo.size());
		if(benchInfo.size()==80){
			seat.setLayout(new GridLayout(8, 10));
			seats = new JLabel[benchInfo.size()];
			select = new boolean[benchInfo.size()];
		}else if(benchInfo.size()==130){
			seat.setLayout(new GridLayout(13, 10));
			seats = new JLabel[benchInfo.size()];
			select = new boolean[benchInfo.size()];
		}
		//System.out.println(seats.length);

		for (int i = 0; i < benchInfo.size(); i++) { // 좌석정보를 초기화해주기위한 반복문
			int k = i;
			//System.out.println(seats.length);
			seats[k] = new JLabel(benchInfo.get(i)); // 라벨을 하나씩 좌석번호로 초기화해줌														
			seats[k].setHorizontalAlignment(JLabel.CENTER); // 텍스트를 가운데 정렬															
			if (sit_bool[k] == 0) {
				seats[k].setBackground(Color.WHITE);
			} else {
				seats[k].setBackground(Color.GREEN);
			}
			seats[k].setOpaque(true);
			seats[k].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					ticketNum = Integer.parseInt(adultTicket.getSelectedItem())
							+ Integer.parseInt(childTicket.getSelectedItem());
					Object obj = e.getSource();
					JLabel la = (JLabel) obj;

					if (member > 0 && obj.equals(seats[k]) && la.getBackground() == Color.WHITE) {
						setSeatsColor(k);
					} else if (member >= 0 && ticketNum != 0 && obj.equals(seats[k])
							&& la.getBackground() == Color.MAGENTA) {
						setSeatsUnColor(k);
					}
					System.out.println(seatInfo);
				}
			});
			seat.add(seats[k]); 
		}
	}
	// 좌석 선택시 색변환 동작 메서드
	public void setSeatsColor(int k) {
		// green는 이전에다른사람이 예매한 좌석, member가 아직 0이아니면 선택할 수 있으므로 
		// MAGENTA로 색깔을 바꿔주고 select를 true로해줌
		if (seats[k].getBackground() != Color.GREEN) {
			seats[k].setBackground(Color.MAGENTA);
			select[k] = true;
			seatInfo.put(member, seats[k].getText());// 선택 좌석 정보 입력
			member--; // 한자리 선택했으므로 member는 감소시킴
		}
		seats[k].setOpaque(true); // 바뀐 배경색 적용
	}

	public void setSeatsUnColor(int k) {// MAGETA는선택했던 좌석
		if (seats[k].getBackground() == Color.MAGENTA && seats[k].getBackground() != Color.WHITE) {
			seats[k].setBackground(Color.WHITE); // 다시 선택하면 다시 흰색으로 배경을 바꿔줌
			select[k] = false; // 선택된 좌석을 false로 바꿔주고 member를 하나증가시킴
			seatInfo.remove(member, seats[k].getText());// 선택 좌석 정보 삭제
			member++;
		}
		seats[k].setOpaque(true); // 바뀐 배경색 적용
	}
	public String getReserveSeatInfo() {
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= seatInfo.size(); i++) {
			sb.append(seatInfo.get(i));
			if (i != seatInfo.size()) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public void setSeatsInfo(String theater) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"request\" : \"sitbool\",\"data\":[");
		for (int i = 0; i < seatInfo.size(); i++) {
			sb.append("{\"sit_bool\" : \"1\",\"theater\":\"" + theater + "\",\"sitName\":\"" + seatInfo.get(i + 1) + "\"");
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
				//MainFrame.createDialog("좌석정보가 있습니다.");
				flag = true;
			} else if(jsonObject.get("result").equals("fail1")){
				MainFrame.createDialog("선택하신좌석이 이미 예약되었습니다.");
				drawbench();
				benchClear();
			} else if(jsonObject.get("result").equals("fail2")){
				MainFrame.createDialog("좌석예매중 에러발생하였습니다.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}	
	//MainFrame이 사용하는 초기화 메서드
	public void benchClear(){
		adultTicket.select(0);
		childTicket.select(0);
		member=0;
		ab=0;
		ch=0;
		seatInfo.clear();
	}	
	//좌석 인원수 체크시 초기화 및 가격합계를 보여주는 메서드
	public void benchReset(){
		for (int i = 0; i < 10; i++) { // 좌석정보를 초기화해주기위한 반복문
			for (int j = 0; j < 8; j++) {
				final int k = i * 8 + j;
				if (!seats[k].getBackground().equals(Color.GREEN)) {
					seats[k].setBackground(Color.WHITE);
				}
			}
		}
		member = 0;
		member = ab + ch;
		ssum = (ab * 10000) + (ch * 8000);
		priceLabel.setText(Integer.toString(ssum));		
	}	
	public void actionPerformed(ActionEvent e) {
		System.out.println(MainFrame.getMyInfo().isEmpty());
		if (MainFrame.getMyInfo().isEmpty() != true && ticketNum != 0 && member == 0) {
			setSeatsInfo(theater);
			String value = getReserveSeatInfo();
			MainFrame.setReserveInfo("seat", value);
			if(flag){
			MainFrame.setCardForm(priceLabel.getText(), seatInfo, theater);
			}
		} else if(MainFrame.getMyInfo().isEmpty() == true) {
			MainFrame.createDialog("로그인이 필요합니다");			
		} else {
			MainFrame.createDialog("좌석을 선택하세요");
		}		
	}
}
