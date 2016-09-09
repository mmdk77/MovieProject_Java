/*
Client�� Server���� JSON�� �̿��� IO�� ���� JSON StringBuffer��/�� ����� Ŭ����
*/

package com.sds.json;

import java.util.ArrayList;

public class JsonProtocol {
	static StringBuffer sb = new StringBuffer();
	static ArrayList<String[]> protocol;
	
	public JsonProtocol(){
		this.protocol = new ArrayList<String[]>();
		sb = new StringBuffer();
	}
	
	public JsonProtocol(ArrayList protocol) {
		this.protocol = protocol;
		sb = new StringBuffer();	
	}
	
	 //�Ϲ��� JSON ���鶧 ��� !!
	public static String setJson(){
		sb.delete(0, sb.length());
		sb.append("{");
		for(int i=0; i < protocol.get(0).length ; i++){
			if(i!=protocol.get(0).length-1){
				sb.append("\""+protocol.get(0)[i]+"\" : \""+protocol.get(1)[i]+"\",");
			}else{
				sb.append("\""+protocol.get(0)[i]+"\" : \""+protocol.get(1)[i]+"\"");
			}
		}
		sb.append("}");

		return sb.toString();
	}
	
	//json �迭�� ���� Json ���鶧 ���
	public static String setJson(ArrayList<String[]> protocol){
		sb.delete(0, sb.length());
		sb.append("{");
		for(int i=0; i < protocol.get(0).length ; i++){
			if(i!=protocol.get(0).length-1){
				sb.append("\""+protocol.get(0)[i]+"\" : \""+protocol.get(1)[i]+"\",");
			}else{
				sb.append("\""+protocol.get(0)[i]+"\" : \""+protocol.get(1)[i]+"\"");
			}
		}
		sb.append("}");

		return sb.toString();
	}
	
	//json �迭�� ���� Json ���鶧 ���
	
	
	//JSON �� �迭Data�� ���� ���!
	public static String setJsonArr(ArrayList<String[]> protocol){
		sb.delete(0, sb.length());
		sb.append("{");
		for(int i=0; i < protocol.get(0).length ; i++){
			if(i!=protocol.get(0).length-1){
				sb.append("\""+protocol.get(0)[i]+"\" : \""+protocol.get(1)[i]+"\",");
			}else{
				sb.append("\""+protocol.get(0)[i]+"\" : [");
				sb.append(protocol.get(1)[i]);
				sb.append("]");
			}
		}
		sb.append("}");

		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		String[] a={"id","pwd","name","age"};
		String[] b={"huho","0000","����ȣ","32"};

		ArrayList<String [] > c = new ArrayList<String[]>();
		c.add(a);
		c.add(b);
		
		JsonProtocol jp = new JsonProtocol(c);
		
		
	}

}
