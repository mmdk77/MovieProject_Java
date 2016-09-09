package com.sds.movie.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TheaterSelect extends JPanel {

	// 전체
	JPanel p_wrapper;

	// 상영관 시간 담을 패널
	JPanel p_inner;
	JLabel lb_cinema;

	// 상영관
	JPanel p_theater;
	JLabel lb_theater;

	// 영화시간
	JPanel p_time;
	JLabel lb_time;

	long theater_id;

	String cinema;
	String theater;
	
	public TheaterSelect(String cinema, String theater, long theater_id) {
		this.cinema = cinema;
		this.theater = theater;
		// 전체 Panel 생성
		p_wrapper = new JPanel();
		// 전체패널 layout

		// theater & time 담을 패널
		p_inner = new JPanel();
		// inner layout
		p_inner.setLayout(new GridLayout(2, 1));

		// 상영관 Panel&컴포넌트 생성
		p_theater = new JPanel();

		// 상영관 컴포넌트
		lb_theater = new JLabel(theater); // DB에서 1,2관 받아야함
		lb_theater.setPreferredSize(new Dimension(300, 45));
		lb_theater.setFont(new Font("굴림", 15, 45));

		// 영화시간
		p_time = new JPanel();
		p_time.setLayout(new FlowLayout(FlowLayout.LEFT));

		requestRuntime(theater_id);

		// 상영관 패널 컴포넌트 추가
		p_theater.add(lb_theater);

		// 전체패널 추가
		p_inner.add(p_theater);
		//p_inner.setPreferredSize(new Dimension(300, 100));
		p_inner.add(p_time);

		p_wrapper.add(p_inner);
		add(p_wrapper);
	}

	public void requestRuntime(long theater_id) {

		StringBuffer sb = new StringBuffer();
		this.theater_id = theater_id;
		sb.append("{");
		sb.append("\"request\":\"time\"");
		sb.append("\"theater_id\":\"" + theater_id + "\"");
		sb.append("}");

		System.out.println(sb.toString());

		try {
			// 지역 요청
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();

			// 응답
			String msg = MainFrame.buffr.readLine();
			System.out.println("99999999999" + msg);

			// 파싱시작.

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);
			JSONArray jsonArray = (JSONArray) jsonObject.get("data");

			// System.out.println("시간시간보내");
			if (jsonObject.get("result").equals("ok")) {
				//JOptionPane.showMessageDialog(this, 50*(jsonArray.size()/8));
				p_inner.setPreferredSize(new Dimension(300, 50*(jsonArray.size()/8)));//영화시간 갯수에 따른 패널 높이 설정
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject obj = (JSONObject) jsonArray.get(i);
					// 시간 생성
					lb_time = new JLabel((String) obj.get("time")); // 일정시간 반복사용 for&while(시간도 DB에 저장되있는시간을가져와야함)
					lb_time.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							System.out.print((String) obj.get("time") + "이시간 영화예매");
							//예매 정보 담기(영화관,상영관,시간)
							MainFrame.setReserveInfo("cinema", cinema);
							MainFrame.setReserveInfo("theater", theater);
							MainFrame.setReserveInfo("time", (String) obj.get("time"));
							//System.out.println(MainFrame.getReserveInfo());
							MainFrame.setBenchSelect(cinema, theater, (String) obj.get("time"));
						}

					});

					// 시간 패널에 컴포넌트 추가
					p_time.add(lb_time);

				}
			} else if (jsonObject.get("result").equals("fail")) {
				MainFrame.createDialog("데이터를 가져오지 못했습니다. 이거슨 실패");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}