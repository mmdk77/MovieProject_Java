package com.sds.movie.client;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CinemaSelect extends JPanel implements ItemListener{
	
	//전체
	JPanel p_wrapper;
	
	//위쪽
	JPanel p_north;  //위에붙을패널
	JPanel p_movieTitle;//선택한 영화제목
	JPanel p_innerChoice; //컴포넌트 담을 패널
	public JLabel lb_mTitle; //지역, 영화관 라벨

	JLabel lb_city;

	JLabel lb_cinema;
	Choice ch_city,ch_cinema; //지역, 영화관 선택
	
	//중앙
	JPanel p_center; //중앙에 붙을패널
	JPanel p_innerTitle,p_innerSelect; 
	JLabel lb_theater;
	
	JScrollPane scroll;
	
	//멤버변수
	String cinema;
	
	TheaterSelect ts;
	
	public CinemaSelect(String title) {
		
		//this.cinema = cinema;
	
		//전체 둘러쌀 패널 생성
		p_wrapper = new JPanel();
		p_wrapper.setPreferredSize(new Dimension(380, 550)); //위치 지정 해야함
		
		//전체패널 layout
		p_wrapper.setLayout(new BorderLayout());
		
		//위쪽 메뉴 패널
		p_movieTitle = new JPanel();
		p_north = new JPanel();
		
		//위쪽 layout	
		p_north.setLayout(new GridLayout(2, 0));
		
		//위쪽 안 패널
		p_innerChoice = new JPanel();
		
		//위 패널의 들어갈 컴포넌트
		lb_mTitle = new JLabel(title); //영화제목
		lb_mTitle.setFont(new Font("굴림", 40, 40));
		
		lb_city = new JLabel("지역"); //지역선택
		lb_city.setFont(new Font("굴림", 15, 15));
		ch_city = new Choice();
		ch_city.setPreferredSize(new Dimension(145, 10));
		
		
		lb_cinema = new JLabel("영화관"); //상영관선택
		lb_cinema.setFont(new Font("굴림", 15, 15));
		ch_cinema = new Choice();
		ch_cinema.setPreferredSize(new Dimension(145, 10));
		ch_cinema.add("선                         택▼");

		//중앙 패널 생성
		p_center = new JPanel();
		p_center.setBackground(Color.CYAN);
		int top =4;					// *TOP ---DB에서 사용할 배열 길이
		p_center.setPreferredSize(new Dimension(300, top*200)); 

		scroll = new JScrollPane(p_center);
		
		//중앙 안쪽 패널
		p_innerTitle = new JPanel();
		p_innerSelect = new JPanel();
		
		
		//중앙패널의 들어갈 컴포넌트
		lb_theater = new JLabel("현재 상영중인 관");
		lb_theater.setFont(new Font("굴림", 20	, 20));
		
	
		//위쪽 컴포넌트 추가
		p_movieTitle.add(lb_mTitle);
		p_innerChoice.add(lb_city);
		p_innerChoice.add(ch_city);
		p_innerChoice.add(lb_cinema);
		p_innerChoice.add(ch_cinema);
		p_north.add(p_movieTitle);
		p_north.add(p_innerChoice);
		
		//중앙 컴포넌트 추가 
		p_innerTitle.add(lb_theater);
		p_center.add(p_innerTitle); //상영관 제목
		
		
		
		
		//이벤트
		ch_city.addItemListener(this);
		ch_cinema.addItemListener(this);
				
		//전체 패널 추가
		p_wrapper.add(p_north, BorderLayout.NORTH);
		p_wrapper.add(scroll, BorderLayout.CENTER);
		add(p_wrapper);
		requestCity();
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		if(obj==ch_city){
			//영화관 요청
			requestCinema();
		}if(obj==ch_cinema){
			//상영관 등록 
			requestTheater();
			
		}
		
	}
	public void requestCity(){
		//String city= ch_city.getSelectedItem(); 뭐때문에 썻더라??
		StringBuffer sb = new StringBuffer();
		
		sb.append("{");
		sb.append("\"request\":\"city\"");
		sb.append("}");
		
		try {
			//지역 요청
			MainFrame.buffw.write(sb.toString()+"\n");
			MainFrame.buffw.flush();
			
			//응답
			String msg = MainFrame.buffr.readLine();
			System.out.println("1111111"+msg);
			
			//파싱시작.
			ch_city.add("선                         택▼");
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject)jsonParser.parse(msg);
			JSONArray jsonArray = (JSONArray) jsonObject.get("data");
			
			if(jsonObject.get("result").equals("ok")){	
				for(int i =0; i<jsonArray.size();i++){
					JSONObject obj  = (JSONObject)jsonArray.get(i);
					String region = (String)obj.get("city");
					//System.out.println(region);

					ch_city.add(region);
				}

			}else if(jsonObject.get("result").equals("fail")){
				MainFrame.createDialog("데이터를 가져오지 못했습니다. 이거슨 실패");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void requestCinema(){
	
		int region=ch_city.getSelectedIndex();
		StringBuffer sb = new StringBuffer();
		System.out.println(region);
		
		sb.append("{");
		sb.append("\"request\":\"cinema\",");
		sb.append("\"city\":\""+region+"\"");
		sb.append("}");
		
		try {
			//지역 요청
			MainFrame.buffw.write(sb.toString()+"\n");
			MainFrame.buffw.flush();
			
			System.out.println(sb.toString());
			
			//응답
			String msg = MainFrame.buffr.readLine();
			System.out.println("222222222"+msg);
			
			//파싱시작.
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject)jsonParser.parse(msg);
			JSONArray jsonArray = (JSONArray) jsonObject.get("data");
			
			if(jsonObject.get("result").equals("ok")){	
				ch_cinema.removeAll();
				ch_cinema.add("선                         택▼");
				for(int i =0; i<jsonArray.size();i++){
					JSONObject obj  = (JSONObject)jsonArray.get(i);
					
					String name = (String)obj.get("name");
					//System.out.println(region);
					
					ch_cinema.add(name);
				}

			}else if(jsonObject.get("result").equals("fail")){
				MainFrame.createDialog("데이터를 가져오지 못했습니다. 이거슨 실패");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	public void requestTheater() {
		
		int choiceCinema = ch_cinema.getSelectedIndex();
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append("\"request\":\"theater\",");
		sb.append("\"cinema\":\""+choiceCinema+"\"");
		sb.append("}");
		
		System.out.println("dddddddddddddddddd"+sb.toString());
		try {
			//지역 요청
			MainFrame.buffw.write(sb.toString()+"\n");
			MainFrame.buffw.flush();
			
			//응답
			String msg = MainFrame.buffr.readLine();
			System.out.println("33333333"+msg);
			
			//파싱시작.
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject)jsonParser.parse(msg);
			JSONArray jsonArray = (JSONArray) jsonObject.get("data");
			
			if(jsonObject.get("result").equals("ok")){	
				//System.out.println("1111111지워져라!");	
				if(ts!=null){
					p_center.removeAll();
					p_center.updateUI();
				}
				//lb_theater = new JLabel("현재 상영중인 관");
				//lb_theater.setFont(new Font("굴림", 20	, 20));
				//p_innerTitle.add(lb_theater);
				p_center.add(p_innerTitle);
				
				for(int i =0; i<jsonArray.size();i++){
					JSONObject obj  = (JSONObject)jsonArray.get(i);
					
					//int theater_id = Integer.parseInt((String)obj.get("theater"));
					String theater = (String)obj.get("theater");
					long theater_id = (Long)obj.get("theater_id");				
					String cinema = ch_cinema.getSelectedItem();
					System.out.println("-"+theater+"-"+cinema+"-"+theater_id);

					ts = new TheaterSelect(cinema, theater, theater_id);
					p_center.add(ts);
					//ts.updateUI();
					
				}
				

			}else if(jsonObject.get("result").equals("fail")){
				MainFrame.createDialog("데이터를 가져오지 못했습니다. 이거슨 실패");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}



