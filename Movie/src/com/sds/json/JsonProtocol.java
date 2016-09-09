/*
Client와 Server간의 JSON을 이용한 IO를 위해 JSON StringBuffer를/을 만드는 클래스
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
	
	 //일반적 JSON 만들때 사용 !!
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
	
	//json 배열에 넣을 Json 만들때 사용
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
	
	//json 배열에 넣을 Json 만들때 사용
	
	
	//JSON 에 배열Data가 들어갈때 사용!
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
		String[] b={"huho","0000","이현호","32"};

		ArrayList<String [] > c = new ArrayList<String[]>();
		c.add(a);
		c.add(b);
		
		JsonProtocol jp = new JsonProtocol(c);
		
		
	}

}
