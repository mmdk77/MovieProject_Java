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
	Choice adultTicket; // ����
	Choice childTicket; // �л�

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
	static int member = 0; // member�� �ϳ��� �����ϸ鼭 0�̵Ǹ� �¼��� ���� �� ���õ�
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

		time = new JLabel("�󿵽ð� :" + movieTime);
		adult = new JLabel("��    �� :");
		child = new JLabel("û�ҳ� :");
		price = new JLabel("��    �� :");

		movieInfo = new JLabel(); // ���̽� ��ü�� �����Ѵ�.
		movieInfo2 = new JLabel(info + theater);
		
		adultTicket = new Choice(); // ����ǥ ���ż��� �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�
		childTicket = new Choice(); // û�ҳ�ǥ ���ż��� �Է¹ޱ� ���� �ؽ�Ʈ�ʵ�
		priceLabel = new JTextField();

		for (int i = 0; i < 5; i++) { // �ο��� �ִ� 4��
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
		screen.setOpaque(true); // �̼Ӽ��� true�� ���ָ� background color�� �����

		next = new JButton("����");
		ticket = new ArrayList<String>(); //������ ������ �����ϰ� ���Ű� �Ϸ�Ǹ� ���� ���ŵ� ������ add��

		seat = new JPanel(new GridLayout(8, 10)); // ��ȭ �¼������� panel�� �ߴµ�
													// GridLayout���� ��� 8x10 �¼��� ����
		seats = new JLabel[80]; // ��ȭ �¼��� ��ȣ�� ������ ��, background color�� �ٲ㼭 �¼������� ǥ��
		select = new boolean[80]; // ���� ������ ����� 80���ڸ��� ���õ����� select[80] = true��

		seat.setBackground(Color.white);
		seat.setOpaque(true);

		screen.setBounds(20, 190, 350, 25);
		screen.setHorizontalAlignment(SwingConstants.CENTER);

		// widget�� ��ǥ����
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
	// DB���� �¼� ���� �ҷ�����
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
				//MainFrame.createDialog("�¼����� �����⸦ �Ϸ��Ͽ����ϴ�.");
				drawbench();
				this.updateUI();
			} else if (jsonObject.get("result").equals("fail")) {
				MainFrame.createDialog("�¼����� �����Ⱑ �����Ͽ����ϴ�.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	// �¼� �׸���
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

		for (int i = 0; i < benchInfo.size(); i++) { // �¼������� �ʱ�ȭ���ֱ����� �ݺ���
			int k = i;
			//System.out.println(seats.length);
			seats[k] = new JLabel(benchInfo.get(i)); // ���� �ϳ��� �¼���ȣ�� �ʱ�ȭ����														
			seats[k].setHorizontalAlignment(JLabel.CENTER); // �ؽ�Ʈ�� ��� ����															
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
	// �¼� ���ý� ����ȯ ���� �޼���
	public void setSeatsColor(int k) {
		// green�� �������ٸ������ ������ �¼�, member�� ���� 0�̾ƴϸ� ������ �� �����Ƿ� 
		// MAGENTA�� ������ �ٲ��ְ� select�� true������
		if (seats[k].getBackground() != Color.GREEN) {
			seats[k].setBackground(Color.MAGENTA);
			select[k] = true;
			seatInfo.put(member, seats[k].getText());// ���� �¼� ���� �Է�
			member--; // ���ڸ� ���������Ƿ� member�� ���ҽ�Ŵ
		}
		seats[k].setOpaque(true); // �ٲ� ���� ����
	}

	public void setSeatsUnColor(int k) {// MAGETA�¼����ߴ� �¼�
		if (seats[k].getBackground() == Color.MAGENTA && seats[k].getBackground() != Color.WHITE) {
			seats[k].setBackground(Color.WHITE); // �ٽ� �����ϸ� �ٽ� ������� ����� �ٲ���
			select[k] = false; // ���õ� �¼��� false�� �ٲ��ְ� member�� �ϳ�������Ŵ
			seatInfo.remove(member, seats[k].getText());// ���� �¼� ���� ����
			member++;
		}
		seats[k].setOpaque(true); // �ٲ� ���� ����
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
				//MainFrame.createDialog("�¼������� �ֽ��ϴ�.");
				flag = true;
			} else if(jsonObject.get("result").equals("fail1")){
				MainFrame.createDialog("�����Ͻ��¼��� �̹� ����Ǿ����ϴ�.");
				drawbench();
				benchClear();
			} else if(jsonObject.get("result").equals("fail2")){
				MainFrame.createDialog("�¼������� �����߻��Ͽ����ϴ�.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}	
	//MainFrame�� ����ϴ� �ʱ�ȭ �޼���
	public void benchClear(){
		adultTicket.select(0);
		childTicket.select(0);
		member=0;
		ab=0;
		ch=0;
		seatInfo.clear();
	}	
	//�¼� �ο��� üũ�� �ʱ�ȭ �� �����հ踦 �����ִ� �޼���
	public void benchReset(){
		for (int i = 0; i < 10; i++) { // �¼������� �ʱ�ȭ���ֱ����� �ݺ���
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
			MainFrame.createDialog("�α����� �ʿ��մϴ�");			
		} else {
			MainFrame.createDialog("�¼��� �����ϼ���");
		}		
	}
}
