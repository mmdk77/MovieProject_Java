package com.sds.movie.admin;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sds.movie.client.MainFrame;

public class MemberTableModel extends AbstractTableModel{
	
	String[] column={"Member_id","ID","Password","Name","Address_id","Question","Answer","Member_img","regdate"};
	
	ArrayList<String[]> memberList = new ArrayList<String[]>();

	public MemberTableModel() {
		// TODO Auto-generated constructor stub
		getMemberInfo();
	}
	
	public void getMemberInfo(){
		String json="{ \"request\" : \"memberInfo\"}";

		try {
			System.out.println(json);
			MainFrame.buffw.write(json+"\n");
			MainFrame.buffw.flush();
			String responce = MainFrame.buffr.readLine(); //응답 받기
			System.out.println(responce);
			memberList.removeAll(memberList);//초기화
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject =(JSONObject)jsonParser.parse(responce);
			if(jsonObject.get("result").equals("ok")){
				JSONArray jsonArray = (JSONArray)jsonObject.get("data");
				for(int i=0; i < jsonArray.size(); i++){
					String[] record = new String[10];
					JSONObject obj = (JSONObject)jsonArray.get(i);
					System.out.println(obj.get("member_id"));
					record[0]=(String)obj.get("member_id");
					record[1]=(String)obj.get("id");
					record[2]=(String)obj.get("pwd");
					record[3]=(String)obj.get("name");
					record[4]=(String)obj.get("addr_id");
					record[5]=(String)obj.get("question");
					record[6]=(String)obj.get("answer");
					record[7]=(String)obj.get("member_img");
					record[8]=(String)obj.get("regdate");
					//record[8]=(String)obj.get("usetime");
					
					memberList.add(record);
					System.out.println(memberList);
				}
				System.out.println(memberList.size());
			}else{ 
				MainFrame.createDialog("회원정보가 없습니다.");
			}
			//this.fireTableDataChanged();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}	


	
	
	@Override
	public String getColumnName(int col) {
		// TODO Auto-generated method stub
		return column[col];
	}
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return memberList.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return column.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		return (String)memberList.get(row)[col];
	}
	



}
