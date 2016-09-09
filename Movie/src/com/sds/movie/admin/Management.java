package com.sds.movie.admin;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sds.movie.client.MainFrame;

public class Management extends JPanel{
	JTable table;
	MovieTableModel movieTableModel;
	JScrollPane scroll;
	JPanel p_west;
	JPanel p_south;
	
	Choice addr;
	Choice cinema;
	Choice theater;
	
	MovieInfoUpdate movieInfoUpdate;
	
	public Management() {
		movieTableModel = new MovieTableModel();
		table = new JTable(movieTableModel);
		scroll = new JScrollPane(table);
		p_west = new JPanel();
		p_south = new JPanel();
		p_west.setBackground(Color.YELLOW);
		p_west.setPreferredSize(new Dimension(100, 500));
		p_south.setBackground(Color.DARK_GRAY);
		p_south.setPreferredSize(new Dimension(1000, 100));
		setLayout(new BorderLayout());
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(table.getValueAt(table.getSelectedRow(), 0));
				getWestList((String)table.getValueAt(table.getSelectedRow(), 0));
				
				String movie_id = (String)table.getValueAt(table.getSelectedRow(), 0);
				String filename=(String)table.getValueAt(table.getSelectedRow(), 2);
				String movie_name=(String)table.getValueAt(table.getSelectedRow(), 1);
				String movie_producer=(String)table.getValueAt(table.getSelectedRow(), 3);
				String movie_actor=(String)table.getValueAt(table.getSelectedRow(), 4);
				String movie_runtime=(String)table.getValueAt(table.getSelectedRow(), 5);
				String movie_story= (String)table.getValueAt(table.getSelectedRow(), 6);
				String movie_openday=(String)table.getValueAt(table.getSelectedRow(), 7);
				movieInfoUpdate = new MovieInfoUpdate(filename, movie_id, movie_name, movie_producer, movie_actor, movie_runtime, movie_story, movie_openday);
			}
			
		});
		movieManagement();
		
		add(p_west,BorderLayout.WEST);
		add(scroll);
		add(p_south,BorderLayout.SOUTH);
		
		setSize(1000, 600);
	}
	
	public void movieManagement(){
		addr = new Choice();
		cinema = new Choice();
		theater = new Choice();
		
		addr.add("지    역▼");
		cinema.add("영화관▼");
		theater.add("상영관▼");
		
		p_west.add(addr);
		p_west.add(cinema);
		p_west.add(theater);
		
		this.updateUI();
	}
	
	public void getWestList(String movie_id){
		String msg="{\"request\" : \"westList\",\"movie_id\" : \""+ movie_id +"\"}";
		System.out.println(msg);
		try {
			MainFrame.buffw.write(msg+"\n");
			MainFrame.buffw.flush();
			System.out.println("보내기 완료");
			
			String response=MainFrame.buffr.readLine();
			System.out.println(response);
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject)jsonParser.parse(response);
			if(jsonObject.get("result").equals("ok")){
				//System.out.println("11111");
				JSONArray jsonArr = (JSONArray)jsonObject.get("data");
				addr.removeAll();
				cinema.removeAll();
				theater.removeAll();
				for(int i=0; i < jsonArr.size(); i++){
					JSONObject obj=(JSONObject)jsonArr.get(i);
					//System.out.println("22222");
				
					if(addr.getItemCount() == 0){
						addr.add("지    역▼");
						addr.add((String)obj.get("addr"));
					}else{
						boolean result = true;
						for(int j=0; j < addr.getItemCount(); j++){
							if(addr.getItem(j).equals((String)obj.get("addr"))){
								result = false;
							}	
						}
						if(result){
							addr.add((String)obj.get("addr"));
							//System.out.println(addr);
						}
					}
					//System.out.println("33333");
				
					if(cinema.getItemCount() == 0){
						cinema.add("영화관▼");
						cinema.add((String)obj.get("cinema"));
					}else{
						boolean result = true;
						for(int j=0; j < cinema.getItemCount(); j++){
							if(cinema.getItem(j).equals((String)obj.get("cinema"))){
								result = false;
							}	
						}
						if(result){
							cinema.add((String)obj.get("cinema"));
							//System.out.println(cinema);
						}
					}
					//System.out.println("4444");
				
					if(theater.getItemCount() == 0){
						theater.add("상영관▼");
						theater.add((String)obj.get("theater"));
					}else{
						boolean result = true;
						for(int j=0; j < theater.getItemCount(); j++){
							if(theater.getItem(j).equals((String)obj.get("theater"))){
								result = false;
							}	
						}
						if(result){
							theater.add((String)obj.get("theater"));
						}
					}
					
				}
				
			}else if(jsonObject.get("result").equals("fail")){
				addr.removeAll();
				cinema.removeAll();
				theater.removeAll();
				addr.add("지    역▼");
				cinema.add("영화관▼");
				theater.add("상영관▼");
			}
			
			updateUI();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	public void getTableModel(){
		movieTableModel.getMovieInfo();
		movieTableModel.fireTableDataChanged();
		table.updateUI();
	}
/*
	public static void main(String[] args){
		JFrame j = new JFrame("관리자");
		Management mng = new Management();
		j.add(mng);
		
		j.setSize(400, 600);
		j.setVisible(true);
		
	}*/
	
}
