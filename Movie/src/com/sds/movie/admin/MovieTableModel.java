package com.sds.movie.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sds.movie.client.MainFrame;

public class MovieTableModel extends AbstractTableModel{
	String[] columnTitle ={"��ȭ��ȣ","��ȭ����","������","����","�⿬��","�󿵽ð�","�ٰŸ�","������"};
	ArrayList<String[]> cineInfo = new ArrayList<String[]>();
	
	@Override
	public int getRowCount() {
		return cineInfo.size();
	}
	
	@Override
	public String getColumnName(int col) {
		return columnTitle[col];
	}
	
	@Override
	public int getColumnCount() {
		return columnTitle.length;
	}

	@Override
	public String getValueAt(int row, int col) {
		return (String)cineInfo.get(row)[col];
	}

	public MovieTableModel() {
		getMovieInfo();
	}
	
	public void getMovieInfo(){
		String json="{ \"request\" : \"movieInfo\"}";
		try {
			MainFrame.buffw.write(json+"\n");
			MainFrame.buffw.flush();
			String responce = MainFrame.buffr.readLine(); //���� �ޱ�
			System.out.println(responce);
			cineInfo.removeAll(cineInfo);//�ʱ�ȭ
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject =(JSONObject)jsonParser.parse(responce);
			if(jsonObject.get("result").equals("ok")){
				JSONArray jsonArray = (JSONArray)jsonObject.get("data");
				for(int i=0; i < jsonArray.size(); i++){
					String[] record = new String[8];
					JSONObject obj = (JSONObject)jsonArray.get(i);
					System.out.println(obj.get("movie_id"));
					record[0]=(String)obj.get("movie_id");
					record[1]=(String)obj.get("name");
					record[2]=(String)obj.get("movie_img");
					record[3]=(String)obj.get("producer");
					record[4]=(String)obj.get("actor");
					record[5]=(String)obj.get("runtime");
					record[6]=(String)obj.get("story");
					record[7]=(String)obj.get("opendate");
					
					cineInfo.add(record);
					System.out.println(cineInfo);
				}
				System.out.println(cineInfo.size());
			}else{ 
				MainFrame.createDialog("��ȭ������ �����ϴ�.");
			}
			//this.fireTableDataChanged();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
}
