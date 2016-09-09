package com.sds.movie.client;

import java.awt.Choice;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class AddressFrame extends JFrame implements ItemListener, ActionListener {
	JButton bt_sub, bt_lay, bt_can;
	static Choice ch_city, ch_gu;
	Choice ch_dong;
	String st_dong[];
	JTextField tf_sub;
	JTextArea ta_certify;
	JLabel lb_city, lb_certify, lb_gu, lb_tf;
	RegistForm registForm;

	public AddressFrame(RegistForm registForm) {
		this.registForm = registForm;
		// GridLayout gr= new GridLayout(3, 3);
		setLayout(null);

		ch_city = new Choice();
		lb_city = new JLabel("시");
		lb_gu = new JLabel("구");
		ch_dong = new Choice();
		lb_tf = new JLabel("상세주소");
		tf_sub = new JTextField();
		// bt_sub = new JButton("등록");
		// lb_certify = new JLabel("주소확인");
		// ta_certify = new JTextArea();
		bt_lay = new JButton("입력");
		bt_can = new JButton("취소");

		ch_city.add("선택▼");
		/*
		 * ch_city.add("서울시"); ch_city.add("대전광역시"); ch_city.add("광주광역시");
		 * ch_city.add("부산시"); ch_city.add("대구광역시"); ch_city.add("제주시");
		 */
		getAddr();
		lb_city.setBounds(20, 10, 30, 30);
		ch_city.setBounds(50, 10, 150, 20);
		lb_gu.setBounds(220, 10, 30, 30);
		ch_dong.setBounds(250, 10, 150, 20);
		lb_tf.setBounds(10, 50, 70, 30);
		tf_sub.setBounds(80, 50, 250, 25);
		bt_lay.setBounds(340, 50, 70, 20);
		// lb_certify.setBounds(10, 100, 70, 20);
		// ta_certify.setBounds(80, 90, 300, 50);
		// bt_lay.setBounds(180, 180, 70, 20);

		add(lb_gu);
		add(lb_city);
		add(ch_city);
		add(lb_tf);
		add(tf_sub);
		// add(bt_sub);
		// add(lb_certify);
		// add(ta_certify);
		add(bt_lay);
		// add(bt_can);

		ch_dong.add("선택▼");
		add(ch_dong);

		ch_city.addItemListener(this);
		// bt_sub.addActionListener(this);
		bt_lay.addActionListener(this);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		setTitle("회원가입 주소등록");

		setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		setSize(440, 200);
		setVisible(true);

	}

	public void GangDong() {
		ch_dong.removeAll();
		ch_dong.add("인제군");
		ch_dong.add("속초군");
		ch_dong.add("양구군");
		ch_dong.add("고성군");
		ch_dong.add("화천군");
		ch_dong.add("춘천시");
		ch_dong.add("홍천군");
		ch_dong.add("횡성군");

	}

	public void GyungDong() {
		ch_dong.removeAll();
		ch_dong.add("포항시");
		ch_dong.add("경주시");
		ch_dong.add("상주시");
		ch_dong.add("구미시");
		ch_dong.add("영천시");
		ch_dong.add("문경시");
		ch_dong.add("경산시");
		ch_dong.add("군위군");

	}

	public void InCheonDong() {
		ch_dong.removeAll();
		ch_dong.add("만석동");
		ch_dong.add("화수동");
		ch_dong.add("장영동");
		ch_dong.add("화평동");
		ch_dong.add("문학동");
		ch_dong.add("동춘동");
		ch_dong.add("송도동");
		ch_dong.add("만수동");

	}

	public void DaeDong() {
		ch_dong.removeAll();
		ch_dong.add("서구");
		ch_dong.add("동구");
		ch_dong.add("중구");
		ch_dong.add("유성구");
		ch_dong.add("노은구");
		// add(ch_dong);

	}

	public void SeoulDong() {
		// ch_dong = new Choice();
		ch_dong.removeAll();
		ch_dong.add("동작구");
		ch_dong.add("노원구");
		ch_dong.add("강남구");
		ch_dong.add("광진구");
		ch_dong.add("서대문구");
		ch_dong.add("영등포구");
		// add(ch_dong);

	}

	public void DaeguDong() {
		// ch_dong = new Choice();
		ch_dong.removeAll();
		ch_dong.add("지성구");
		ch_dong.add("수성구");
		ch_dong.add("달서구");
		ch_dong.add("북구");
		// add(ch_dong);

	}

	public void GwangDong() {
		// ch_dong = new Choice();
		ch_dong.removeAll();
		ch_dong.add("북구");
		ch_dong.add("동구");
		ch_dong.add("광산구");
		// add(ch_dong);
	}

	public void BuDong() {
		// ch_dong = new Choice();
		ch_dong.removeAll();
		ch_dong.add("해운대구");
		ch_dong.add("경포대구");
		ch_dong.add("서구");
		ch_dong.add("동구");
		ch_dong.add("중구");
		// add(ch_dong);
	}

	public void JeDong() {
		// ch_dong = new Choice();
		ch_dong.removeAll();
		ch_dong.add("서귀포시");
		ch_dong.add("우도군");
		ch_dong.add("한라시");
		// add(ch_dong);
	}

	public void getAddr() {

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"request\" : \"city\"");
		sb.append("}");

		try {
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();
			System.out.println(sb.toString());

			String responce = MainFrame.buffr.readLine();

			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(responce);

			if (jsonObject.get("result").equals("ok")) {
				JSONArray jsonArr = (JSONArray) jsonObject.get("data");
				for (int i = 0; i < jsonArr.size(); i++) {
					JSONObject obj = (JSONObject) jsonArr.get(i);
					String city = (String) obj.get("city");
					// long addr_id=(Long)obj.get("addr_id");
					ch_city.add(city);
				}

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
	public void itemStateChanged(ItemEvent e) {
		Choice ch = (Choice) e.getSource();
		String str = ch.getSelectedItem();

		if (str.equals("서울")) {
			System.out.println("서울택했어?");
			SeoulDong();

		} else if (str.equals("대전/충청")) {
			System.out.println("대전택했어?");
			DaeDong();
		} else if (str.equals("광주/전라")) {
			System.out.println("광주택했어?");
			GwangDong();
		} else if (str.equals("부산시")) {
			System.out.println("부산택했어?");
			BuDong();
		} else if (str.equals("대구/울산/부산")) {
			System.out.println("대구택했어?");
			DaeguDong();
		} else if (str.equals("제주")) {
			System.out.println("제주택했어?");
			JeDong();
		} else if (str.equals("인천/경기")) {
			System.out.println("인천택했어?");
			InCheonDong();
		} else if (str.equals("강원")) {
			System.out.println("강원택했어?");
			GangDong();
		} else if (str.equals("경상")) {
			System.out.println("경상택했어?");
			GyungDong();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String str = ch_city.getSelectedItem();
		String str1 = ch_dong.getSelectedItem();
		String tf_addr = tf_sub.getText();
		Object obj = e.getSource();

		if (obj.equals(bt_sub)) {

			String msg = tf_sub.getText();
			System.out.println(str);
			ta_certify.append(str + "  " + str1 + "  " + msg);

		} else if (obj.equals(bt_lay)) {
			// System.out.println("입력눌렀어?");
			if (str.equals("선택▼")) {
				JOptionPane.showMessageDialog(this, "입력 할 수 없는 값 입니다. 도시를 선택하세요!!");
				
			} else if(tf_addr.equals("")){
				JOptionPane.showMessageDialog(this, "상세 주소를 입력하세요!!");
			}else{
				String msg = tf_sub.getText();
				registForm.ta_add1.setText(str + "  " + str1);
				registForm.ta_add2.setText(msg);
				this.dispose();
			}
		}

	}

}