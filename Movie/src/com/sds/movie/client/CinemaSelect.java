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
	
	//��ü
	JPanel p_wrapper;
	
	//����
	JPanel p_north;  //���������г�
	JPanel p_movieTitle;//������ ��ȭ����
	JPanel p_innerChoice; //������Ʈ ���� �г�
	public JLabel lb_mTitle; //����, ��ȭ�� ��

	JLabel lb_city;

	JLabel lb_cinema;
	Choice ch_city,ch_cinema; //����, ��ȭ�� ����
	
	//�߾�
	JPanel p_center; //�߾ӿ� �����г�
	JPanel p_innerTitle,p_innerSelect; 
	JLabel lb_theater;
	
	JScrollPane scroll;
	
	//�������
	String cinema;
	
	TheaterSelect ts;
	
	public CinemaSelect(String title) {
		
		//this.cinema = cinema;
	
		//��ü �ѷ��� �г� ����
		p_wrapper = new JPanel();
		p_wrapper.setPreferredSize(new Dimension(380, 550)); //��ġ ���� �ؾ���
		
		//��ü�г� layout
		p_wrapper.setLayout(new BorderLayout());
		
		//���� �޴� �г�
		p_movieTitle = new JPanel();
		p_north = new JPanel();
		
		//���� layout	
		p_north.setLayout(new GridLayout(2, 0));
		
		//���� �� �г�
		p_innerChoice = new JPanel();
		
		//�� �г��� �� ������Ʈ
		lb_mTitle = new JLabel(title); //��ȭ����
		lb_mTitle.setFont(new Font("����", 40, 40));
		
		lb_city = new JLabel("����"); //��������
		lb_city.setFont(new Font("����", 15, 15));
		ch_city = new Choice();
		ch_city.setPreferredSize(new Dimension(145, 10));
		
		
		lb_cinema = new JLabel("��ȭ��"); //�󿵰�����
		lb_cinema.setFont(new Font("����", 15, 15));
		ch_cinema = new Choice();
		ch_cinema.setPreferredSize(new Dimension(145, 10));
		ch_cinema.add("��                         �á�");

		//�߾� �г� ����
		p_center = new JPanel();
		p_center.setBackground(Color.CYAN);
		int top =4;					// *TOP ---DB���� ����� �迭 ����
		p_center.setPreferredSize(new Dimension(300, top*200)); 

		scroll = new JScrollPane(p_center);
		
		//�߾� ���� �г�
		p_innerTitle = new JPanel();
		p_innerSelect = new JPanel();
		
		
		//�߾��г��� �� ������Ʈ
		lb_theater = new JLabel("���� ������ ��");
		lb_theater.setFont(new Font("����", 20	, 20));
		
	
		//���� ������Ʈ �߰�
		p_movieTitle.add(lb_mTitle);
		p_innerChoice.add(lb_city);
		p_innerChoice.add(ch_city);
		p_innerChoice.add(lb_cinema);
		p_innerChoice.add(ch_cinema);
		p_north.add(p_movieTitle);
		p_north.add(p_innerChoice);
		
		//�߾� ������Ʈ �߰� 
		p_innerTitle.add(lb_theater);
		p_center.add(p_innerTitle); //�󿵰� ����
		
		
		
		
		//�̺�Ʈ
		ch_city.addItemListener(this);
		ch_cinema.addItemListener(this);
				
		//��ü �г� �߰�
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
			//��ȭ�� ��û
			requestCinema();
		}if(obj==ch_cinema){
			//�󿵰� ��� 
			requestTheater();
			
		}
		
	}
	public void requestCity(){
		//String city= ch_city.getSelectedItem(); �������� ������??
		StringBuffer sb = new StringBuffer();
		
		sb.append("{");
		sb.append("\"request\":\"city\"");
		sb.append("}");
		
		try {
			//���� ��û
			MainFrame.buffw.write(sb.toString()+"\n");
			MainFrame.buffw.flush();
			
			//����
			String msg = MainFrame.buffr.readLine();
			System.out.println("1111111"+msg);
			
			//�Ľ̽���.
			ch_city.add("��                         �á�");
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
				MainFrame.createDialog("�����͸� �������� ���߽��ϴ�. �̰Ž� ����");
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
			//���� ��û
			MainFrame.buffw.write(sb.toString()+"\n");
			MainFrame.buffw.flush();
			
			System.out.println(sb.toString());
			
			//����
			String msg = MainFrame.buffr.readLine();
			System.out.println("222222222"+msg);
			
			//�Ľ̽���.
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject)jsonParser.parse(msg);
			JSONArray jsonArray = (JSONArray) jsonObject.get("data");
			
			if(jsonObject.get("result").equals("ok")){	
				ch_cinema.removeAll();
				ch_cinema.add("��                         �á�");
				for(int i =0; i<jsonArray.size();i++){
					JSONObject obj  = (JSONObject)jsonArray.get(i);
					
					String name = (String)obj.get("name");
					//System.out.println(region);
					
					ch_cinema.add(name);
				}

			}else if(jsonObject.get("result").equals("fail")){
				MainFrame.createDialog("�����͸� �������� ���߽��ϴ�. �̰Ž� ����");
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
			//���� ��û
			MainFrame.buffw.write(sb.toString()+"\n");
			MainFrame.buffw.flush();
			
			//����
			String msg = MainFrame.buffr.readLine();
			System.out.println("33333333"+msg);
			
			//�Ľ̽���.
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject)jsonParser.parse(msg);
			JSONArray jsonArray = (JSONArray) jsonObject.get("data");
			
			if(jsonObject.get("result").equals("ok")){	
				//System.out.println("1111111��������!");	
				if(ts!=null){
					p_center.removeAll();
					p_center.updateUI();
				}
				//lb_theater = new JLabel("���� ������ ��");
				//lb_theater.setFont(new Font("����", 20	, 20));
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
				MainFrame.createDialog("�����͸� �������� ���߽��ϴ�. �̰Ž� ����");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}



