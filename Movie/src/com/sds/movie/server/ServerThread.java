/*
접속자마다 1:1로 요청을 처리하기 위함 서버측 쓰레드!!!
소켓을 보관한다!!
왜? 각종 요청이나 대화를 나누기 위해!!
*/
package com.sds.movie.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sds.movie.client.MainFrame;

public class ServerThread extends Thread {
	Socket client;
	BufferedReader buffr;
	BufferedWriter buffw;
	ServerMain serverMain;

	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	StringBuffer sb = new StringBuffer();

	public ServerThread(ServerMain serverMain, Socket client) {
		this.client = client;
		this.serverMain = serverMain;
		this.con = serverMain.con;
		try {
			buffr = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 듣기
	public void listen() {

		try {
			String msg = buffr.readLine();
			serverMain.area.append(msg + "\n");

			// msg는 클라이언트의 요청 타입에 따라 서버측의 업무 내용을 다르게 한다(즉 언제나 채팅은 아니다!!!!)

			JSONParser jsonParser = new JSONParser();
			// 파씽한 이후 부터는 문자열에 불과했던 제이슨 형식의 데이터를 마치 객체처럼 사용가능
			JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);
			serverMain.area.append("요청 : " + (String) jsonObject.get("request") + "\n");
			// 클라이언트의 요청이 로그인 이라면...
			if (jsonObject.get("request").equals("login")) {
				serverMain.area.append("로그인을 원하는군요!!\n");
				// 쿼리문 수행
				String sql = "select addr_id,m.MEMBER_ID as member_id,answer,id,pwd,member_img,name,question,regdate,card_id,card_num,card_pwd from member m,card c where m.member_id=c.MEMBER_ID And m.ID=? And m.PWD= ?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, (String) jsonObject.get("id")); // 1번쨰 바인드 변수
				pstmt.setString(2, (String) jsonObject.get("password"));// 2번쨰
																		// 바인드
																		// 변수
				rs = pstmt.executeQuery();
				sb.delete(0, sb.length());
				if (rs.next()) {
					sb.append("{");
					sb.append("\"responce\":\"login\",");
					sb.append("\"result\":\"ok\",");
					sb.append("\"data\":{");
					// serverMain.area.append(rs.getString("question"));
					sb.append("\"member_id\":\"" + rs.getInt("member_id") + "\",");
					sb.append("\"id\":\"" + rs.getString("id") + "\",");
					sb.append("\"pwd\":\"" + rs.getString("pwd") + "\",");
					sb.append("\"name\":\"" + rs.getString("name") + "\",");
					sb.append("\"addr_id\":\"" + rs.getInt("addr_id") + "\",");
					sb.append("\"question\":\"" + rs.getString("question") + "\",");
					sb.append("\"answer\":\"" + rs.getString("answer") + "\",");
					sb.append("\"member_img\":\"" + rs.getString("member_img") + "\",");
					sb.append("\"regdate\":\"" + rs.getString("regdate") + "\",");
					sb.append("\"card_id\":\"" + rs.getInt("card_id") + "\",");
					sb.append("\"card_num\":\"" + rs.getString("card_num") + "\",");
					sb.append("\"card_pwd\":\"" + rs.getString("card_pwd") + "\"");
					sb.append("}");
					sb.append("}");
				} else {
					sb.append("{");
					sb.append("\"responce\":\"login\",");
					sb.append("\"result\":\"fail\",");
					sb.append("\"data\":{}");
					sb.append("}");
				}

				release(pstmt, rs);// 반납

				// 클라이언트의 요청이 대화 이라면...
			} else if (jsonObject.get("request").equals("movieInfo")) {
				String sql = "select movie_id, name, story,movie_img,theater_id,to_char(opendate,'YYYY/MM/DD') as opendate, producer, actor, runtime from movie ORDER BY MOVIE_ID";
				pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = pstmt.executeQuery();
				if (rs != null) {
					// rs의 커서 위치를 record의 제일 마지막으로 옮기기
					rs.last();
					int total = rs.getRow(); // 현제 레코드의 위치를 반환
					rs.beforeFirst();// 현제 레코드를 제일 위로 옮겨놓음
					int count = 0;
					// StringBuffer 사용전 비우기!!!
					sb.delete(0, sb.length());

					sb.append("{");
					sb.append("\"responce\" : \"movieInfo\",");
					sb.append("\"result\" : \"ok\",");
					sb.append("\"data\" : [");
					while (rs.next()) {
						count++;
						String movie_id = rs.getString("movie_id");
						String name = rs.getString("name");
						String story = rs.getString("story");
						String movie_img = rs.getString("movie_img");
						String theater_id = rs.getString("theater_id");
						String opendate = rs.getString("opendate");
						String producer = rs.getString("producer");
						String actor = rs.getString("actor");
						String runtime = rs.getString("runtime");

						sb.append("{");
						sb.append("\"movie_id\" : \"" + movie_id + "\",");
						sb.append("\"name\" : \"" + name + "\",");
						sb.append("\"story\" : \"" + story + "\",");
						sb.append("\"movie_img\" : \"" + movie_img + "\",");
						sb.append("\"theater_id\" : \"" + theater_id + "\",");
						sb.append("\"opendate\" : \"" + opendate + "\",");
						sb.append("\"producer\" : \"" + producer + "\",");
						sb.append("\"actor\" : \"" + actor + "\",");
						sb.append("\"runtime\" : \"" + runtime + "\",");
						if (count <= total - 1) {
							sb.append("},");
						} else {
							sb.append("}");
						}
					}
					sb.append("]");
					sb.append("}");
				} else {
					sb.append("{");
					sb.append("\"responce\":\"movieInfo\",");
					sb.append("\"result\":\"fail\",");
					sb.append("\"data\":{}");
					sb.append("}");
				}

				release(pstmt, rs);// 반납
				// 클라이언트의 요청이 아이디 중복체크 이라면...
			} else if (jsonObject.get("request").equals("comment")) {
				/*
				 * 
				 * MovieInfo Class 댓글쓰기
				 * 
				 */
				String sql = "insert into comm(comm_id,member_id,commtxt, movie_id) values(seq_comm.nextval, ?,?,?)";

				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, (String) jsonObject.get("member_id"));
				pstmt.setString(2, (String) jsonObject.get("comment"));
				pstmt.setString(3, (String) jsonObject.get("movie_id"));

				int result = pstmt.executeUpdate();

				sb.delete(0, sb.length());

				if (result != -1) {
					sb.append("{");
					sb.append("\"responce\":\"comment\",");
					sb.append("\"result\":\"ok\"");
					sb.append("}");
				} else {
					sb.append("{");
					sb.append("\"responce\":\"comment\",");
					sb.append("\"result\":\"fail\",");
					sb.append("\"data\":{}");
					sb.append("}");
				}
				release(pstmt);// 반납

				// 클라이언트의 요청이 아이디 중복체크 이라면...
			} else if (jsonObject.get("request").equals("ViewComm")) {
				/*
				 * 
				 * MovieInfo Class 댓글 보기
				 * 
				 */
				String sql = "select * from comm where movie_id=? order by comm_id desc";

				System.out.println(Integer.parseInt((String) jsonObject.get("movie_id")));

				/*
				 * pstmt.setInt(2,
				 * Integer.parseInt((String)jsonObject.get("movie_id")));
				 */

				pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				pstmt.setInt(1, Integer.parseInt((String) jsonObject.get("movie_id")));
				rs = pstmt.executeQuery();
				if (rs != null) {
					// rs의 커서 위치를 record의 제일 마지막으로 옮기기
					rs.last();
					int total = rs.getRow(); // 현제 레코드의 위치를 반환
					rs.beforeFirst();// 현제 레코드를 제일 위로 옮겨놓음
					int count = 0;
					// StringBuffer 사용전 비우기!!!
					sb.delete(0, sb.length());

					sb.append("{");
					sb.append("\"responce\" : \"ViewComm\",");
					sb.append("\"result\" : \"ok\",");
					sb.append("\"data\" : [");

					while (rs.next()) {
						count++;
						int comm_id = rs.getInt("comm_id");
						int member_id = rs.getInt("member_id");
						int movie_id = rs.getInt("movie_id");
						String commtxt = rs.getString("commtxt");

						sb.append("{");
						sb.append("\"comm_id\" : " + comm_id + ",");
						sb.append("\"member_id\" : " + member_id + ",");
						sb.append("\"movie_id\" : " + movie_id + ",");
						sb.append("\"commtxt\" : \"" + commtxt + "\"");

						if (count <= total - 1) {
							sb.append("},");
						} else {
							sb.append("}");
						}
					}
					sb.append("]");
					sb.append("}");
				} else {
					System.out.println("실패");
				}
				release(pstmt, rs);// 반납
			} else if (jsonObject.get("request").equals("updateComm")) {
				/*
				 * MovieInfo Class 댓글수정
				 * 
				 */
				String sql = "update comm set commtxt=? where comm_id=? and member_id=?";

				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, (String) jsonObject.get("commtxt"));
				// pstmt.setInt(2,
				// Integer.parseInt((String)jsonObject.get("movie_id")));
				pstmt.setInt(2, Integer.parseInt((String) jsonObject.get("comm_id")));
				pstmt.setInt(3, Integer.parseInt((String) jsonObject.get("member_id")));

				serverMain.area.append(sql);
				sb.delete(0, sb.length());

				int result = pstmt.executeUpdate();
				serverMain.area.append("9999999887878787878" + result + "\n");

				if (result != -1) {
					sb.append("{");
					sb.append("\"responce\":\"comment\",");
					sb.append("\"result\":\"ok\"");
					sb.append("}");
				} else {
					sb.append("{");
					sb.append("\"responce\":\"comment\",");
					sb.append("\"result\":\"fail\",");
					sb.append("\"data\":{}");
					sb.append("}");
				}
				release(pstmt);// 반납

			} else if (jsonObject.get("request").equals("deleteComm")) {
				/*
				 * MovieInfo Class 댓글삭제
				 * 
				 */
				String sql = "delete FROM COMM WHERE COMM_ID=? AND MEMBER_ID=?";

				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt((String) jsonObject.get("comm_id")));
				pstmt.setInt(2, Integer.parseInt((String) jsonObject.get("member_id")));

				int result = pstmt.executeUpdate();

				sb.delete(0, sb.length());

				if (result != -1) {
					sb.append("{");
					sb.append("\"responce\":\"comment\",");
					sb.append("\"result\":\"ok\"");
					sb.append("}");
				} else {
					sb.append("{");
					sb.append("\"responce\":\"comment\",");
					sb.append("\"result\":\"fail\",");
					sb.append("\"data\":{}");
					sb.append("}");
				}
				release(pstmt);// 반납

			} else if (jsonObject.get("request").equals("idCertify")) {

				// 쿼리문 수행
				String sql = "select * from member where id=?";

				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, (String) jsonObject.get("id")); // registform
																	// 에서 입력된
																	// 아이디

				rs = pstmt.executeQuery();

				sb.delete(0, sb.length());

				if (!rs.next()) {// 일치하는 정보가 없을 때 회원가입 성공!!
					sb.append("{");
					sb.append("\"responce\":\"idCertify\",");
					sb.append("\"result\":\"ok\"");
					sb.append("}");
				} else {
					sb.append("{");
					sb.append("\"responce\":\"idCertify\",");
					sb.append("\"result\":\"fail\",");
					sb.append("\"data\":{}");
					sb.append("}");
				}
				release(pstmt);// 반납
			} else if (jsonObject.get("request").equals("cardcheck")) {
				System.out.println("card test");
				String sql1 = "Select member_id from member where id=?";
				// String sql2 = "select seq_member.currval as member_id from
				// dual";
				pstmt = con.prepareStatement(sql1);

				serverMain.area.append("11111" + (String) jsonObject.get("id"));
				pstmt.setString(1, (String) jsonObject.get("id"));
				rs = pstmt.executeQuery();
				int member_id = 0;
				if (rs.next()) {
					member_id = rs.getInt("member_id");
				}

				serverMain.area.append("22222" + Integer.toString(member_id));
				release(pstmt, rs);
				// 카드 번호와 비밀번호 보내는 쿼리문 수행
				String sql = "insert into card(card_id,member_id,card_num, card_pwd)";
				sql = sql + " values(seq_card.nextval,?,?,?)";

				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, member_id);
				pstmt.setString(2, (String) jsonObject.get("cardnum"));
				pstmt.setString(3, (String) jsonObject.get("cardpwd"));

				int result = pstmt.executeUpdate();

				sb.delete(0, sb.length());

				if (result != -1) {
					sb.append("{");
					sb.append("\"responce\":\"cardcheck\",");
					sb.append("\"result\":\"ok\"");
					sb.append("}");
				} else {
					sb.append("{");
					sb.append("\"responce\":\"cardcheck\",");
					sb.append("\"result\":\"fail\",");
					sb.append("\"data\":{}");
					sb.append("}");
				}
				release(pstmt);// 반납
			} else if (jsonObject.get("request").equals("regist")) {
				// 회원등록 쿼리문 수행
				String sql = "insert into member(member_id,id,pwd,name,addr_id,question,answer,member_img)";
				sql = sql + " values(seq_member.nextval,?,?,?,?,?,?,?)";

				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, (String) jsonObject.get("id")); // 1번쨰 바인드 변수
				pstmt.setString(2, (String) jsonObject.get("password"));// 2번쨰
																		// 바인드
																		// 변수
				pstmt.setString(3, (String) jsonObject.get("name"));
				pstmt.setInt(4, Integer.parseInt((String) jsonObject.get("address")));
				pstmt.setString(5, (String) jsonObject.get("question"));
				pstmt.setString(6, (String) jsonObject.get("questionanswer"));
				pstmt.setString(7, (String) jsonObject.get("image"));

				int result = pstmt.executeUpdate();

				sb.delete(0, sb.length());

				if (result != -1) {
					sb.append("{");
					sb.append("\"responce\":\"regist\",");
					sb.append("\"result\":\"ok\"");
					sb.append("}");
				} else {
					sb.append("{");
					sb.append("\"responce\":\"regist\",");
					sb.append("\"result\":\"fail\",");
					sb.append("\"data\":{}");
					sb.append("}");
				}
				serverMain.area.append("회원가입 완료!!\n");

				release(pstmt);// 반납
			} else if (jsonObject.get("request").equals("changepwd")) {
				// 회원등록 쿼리문 수행
				String sql = "update member set pwd=? where id=?";

				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, (String) jsonObject.get("password")); // 1번쨰
																			// 바인드
																			// 변수
				pstmt.setString(2, (String) jsonObject.get("id"));// 2번쨰

				int result = pstmt.executeUpdate();

				sb.delete(0, sb.length());

				if (result != -1) {
					sb.append("{");
					sb.append("\"responce\":\"changepwd\",");
					sb.append("\"result\":\"ok\"");
					sb.append("}");
				} else {
					sb.append("{");
					sb.append("\"responce\":\"changepwd\",");
					sb.append("\"result\":\"fail\",");
					sb.append("\"data\":{}");
					sb.append("}");
				}
				serverMain.area.append("비밀번호변경 완료!!!\n");

				release(pstmt);// 반납

			} else if (jsonObject.get("request").equals("changename")) {
				// 회원등록 쿼리문 수행
				String sql = "update member set name=? where id=?";

				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, (String) jsonObject.get("name")); // 1번쨰 바인드
																		// 변수
				pstmt.setString(2, (String) jsonObject.get("id"));// 2번쨰

				int result = pstmt.executeUpdate();

				sb.delete(0, sb.length());

				if (result != -1) {
					sb.append("{");
					sb.append("\"responce\":\"changename\",");
					sb.append("\"result\":\"ok\"");
					sb.append("}");
				} else {
					sb.append("{");
					sb.append("\"responce\":\"changename\",");
					sb.append("\"result\":\"fail\",");
					sb.append("\"data\":{}");
					sb.append("}");
				}
				serverMain.area.append("비밀번호변경 완료!!!\n");

				release(pstmt);// 반납

			} else if (jsonObject.get("request").equals("changephoto")) {
				// 회원등록 쿼리문 수행
				String sql = "update member set member_img=? where id=?";

				pstmt = con.prepareStatement(sql);

				serverMain.area
						.append("22222" + (String) jsonObject.get("filename") + "," + (String) jsonObject.get("id"));
				pstmt.setString(1, (String) jsonObject.get("filename")); // 1번쨰
																			// 바인드
																			// 변수
				pstmt.setString(2, (String) jsonObject.get("id"));// 2번쨰

				int result = pstmt.executeUpdate();

				sb.delete(0, sb.length());

				if (result != -1) {
					sb.append("{");
					sb.append("\"responce\":\"changephoto\",");
					sb.append("\"result\":\"ok\"");
					sb.append("}");
				} else {
					sb.append("{");
					sb.append("\"responce\":\"changephoto\",");
					sb.append("\"result\":\"fail\",");
					sb.append("\"data\":{}");
					sb.append("}");
				}
				serverMain.area.append("사진변경 완료!!!\n");

				release(pstmt);// 반납

			} else if (jsonObject.get("request").equals("changecardnum")) {
				// 회원등록 쿼리문 수행
				String sql = "update card set card_num=?, card_pwd=? where member_id=?";

				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, (String) jsonObject.get("card_num")); // 1번쨰
																			// 바인드
																			// 변수
				pstmt.setString(2, (String) jsonObject.get("card_pwd"));
				pstmt.setString(3, (String) jsonObject.get("member_id"));// 2번쨰

				int result = pstmt.executeUpdate();

				sb.delete(0, sb.length());

				if (result != -1) {
					sb.append("{");
					sb.append("\"responce\":\"changecardnum\",");
					sb.append("\"result\":\"ok\"");
					sb.append("}");
				} else {
					sb.append("{");
					sb.append("\"responce\":\"changecardnum\",");
					sb.append("\"result\":\"fail\",");
					sb.append("\"data\":{}");
					sb.append("}");
				}
				serverMain.area.append("카드번호변경 완료!!!\n");

				release(pstmt);// 반납

			} else if (jsonObject.get("request").equals("changecardpwd")) {
				// 회원등록 쿼리문 수행
				String sql = "update card set card_pwd=? where id=?";

				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, (String) jsonObject.get("card_pwd")); // 1번쨰
																			// 바인드
																			// 변수
				pstmt.setString(2, (String) jsonObject.get("id"));// 2번쨰

				int result = pstmt.executeUpdate();

				sb.delete(0, sb.length());

				if (result != -1) {
					sb.append("{");
					sb.append("\"responce\":\"changecardpwd\",");
					sb.append("\"result\":\"ok\"");
					sb.append("}");
				} else {
					sb.append("{");
					sb.append("\"responce\":\"changecardpwd\",");
					sb.append("\"result\":\"fail\",");
					sb.append("\"data\":{}");
					sb.append("}");
				}
				serverMain.area.append("카드비밀번호변경 완료!!!\n");

				release(pstmt);// 반납

			} else if (jsonObject.get("request").equals("city")) {
				/*
				 * CinemaSelect Class의 <지역> Choice선택
				 */
				String sql = "select * from addr";
				pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = pstmt.executeQuery();
				if (rs != null) {
					// rs의 커서 위치를 record의 제일 마지막으로 옮기기
					rs.last();
					int total = rs.getRow(); // 현제 레코드의 위치를 반환
					rs.beforeFirst();// 현제 레코드를 제일 위로 옮겨놓음
					int count = 0;
					// StringBuffer 사용전 비우기!!!
					sb.delete(0, sb.length());

					sb.append("{");
					sb.append("\"responce\" : \"city\",");
					sb.append("\"result\" : \"ok\",");
					sb.append("\"data\" : [");

					while (rs.next()) {
						count++;
						int addr_id = rs.getInt("addr_id");
						String city = rs.getString("city");

						sb.append("{");
						sb.append("\"movie_id\" : " + addr_id + ",");
						sb.append("\"city\" : \"" + city + "\"");

						if (count <= total - 1) {
							sb.append("},");
						} else {
							sb.append("}");
						}
					}
					sb.append("]");
					sb.append("}");
				} else {
					System.out.println("실패");
				}
				release(pstmt, rs);// 반납
			} else if (jsonObject.get("request").equals("cinema")) {
				/*
				 * CinemaSelect Class의 <영화관> Choice선택
				 * 
				 */
				String sql = "select * from cinema where addr_id=?";
				pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				pstmt.setInt(1, Integer.parseInt((String) jsonObject.get("city")));
				// System.out.println((Long) jsonObject.get("addr_id"));
				rs = pstmt.executeQuery();
				if (rs != null) {
					// rs의 커서 위치를 record의 제일 마지막으로 옮기기
					rs.last();
					int total = rs.getRow(); // 현제 레코드의 위치를 반환
					rs.beforeFirst();// 현제 레코드를 제일 위로 옮겨놓음
					int count = 0;
					// StringBuffer 사용전 비우기!!!
					sb.delete(0, sb.length());

					sb.append("{");
					sb.append("\"responce\" : \"cinema\",");
					sb.append("\"result\" : \"ok\",");
					sb.append("\"data\" : [");

					while (rs.next()) {
						count++;
						int cinema_id = rs.getInt("cinema_id");
						String name = rs.getString("name");
						int addr_id = rs.getInt("addr_id");

						sb.append("{");
						sb.append("\"cinema_id\" : " + cinema_id + ",");
						sb.append("\"name\" : \"" + name + "\",");
						sb.append("\"addr_id\" : " + addr_id + "");// ,지우고

						if (count <= total - 1) {
							sb.append("},");
						} else {
							sb.append("}");
						}
					}
					sb.append("]");
					sb.append("}");
				} else {
					System.out.println("실패");
				}
				release(pstmt, rs);// 반납

			} else if (jsonObject.get("request").equals("theater")) {
				/*
				 * 
				 * TheaterSelect Class의 <상영관> 선택
				 * 
				 * 
				 * serverMain.area.append((String)jsonObject.get("cinema"));
				 */
				StringBuffer sql = new StringBuffer();
				sql.append(
						"select cinema_theater.cinema_theater_id, theater.theater_id, theater.name from cinema_theater, theater");
				sql.append(" where cinema_theater.CINEMA_ID=? and theater.theater_id=cinema_theater.THEATER_ID");

				pstmt = con.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				pstmt.setInt(1, Integer.parseInt((String) jsonObject.get("cinema")));

				// System.out.println(Integer.parseInt((String)
				// jsonObject.get("theater")));

				rs = pstmt.executeQuery();
				if (rs != null) {
					// rs의 커서 위치를 record의 제일 마지막으로 옮기기
					rs.last();
					int total = rs.getRow(); // 현제 레코드의 위치를 반환
					rs.beforeFirst();// 현제 레코드를 제일 위로 옮겨놓음
					int count = 0;
					// StringBuffer 사용전 비우기!!!
					sb.delete(0, sb.length());

					sb.append("{");
					sb.append("\"responce\" : \"theater\",");
					sb.append("\"result\" : \"ok\",");
					sb.append("\"data\" : [");

					while (rs.next()) {
						count++;
						int cinema_theater_id = rs.getInt("cinema_theater_id");
						int theater_id = rs.getInt("theater_id");
						String theater = rs.getString("name");

						sb.append("{");
						sb.append("\"cinema_theater_id\" : " + cinema_theater_id + ",");
						sb.append("\"theater_id\" : " + theater_id + ",");
						sb.append("\"theater\" : \"" + theater + "\"");

						if (count <= total - 1) {
							sb.append("},");
						} else {
							sb.append("}");
						}
					}
					sb.append("]");
					sb.append("}");
				} else {
					System.out.println("실패");
				}
				release(pstmt, rs);// 반납

			} else if (jsonObject.get("request").equals("time")) {
				/*
				 * 
				 * TheaterSelect Class의 <영화시간> 선택
				 * 
				 * 
				 * serverMain.area.append((String)jsonObject.get("cinema"));
				 */
				StringBuffer sql = new StringBuffer();
				sql.append("select tr.THEATER_ID, r.RUNTIME_ID, to_char(r.TIME,'HH24:MI') as time ");
				sql.append("from THEATER_RUNTIME tr, RUNTIME r where tr.theater_id=? and r.runtime_id=tr.runtime_id");
				System.out.println(sql);

				pstmt = con.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				pstmt.setInt(1, Integer.parseInt((String) jsonObject.get("theater_id")));

				// System.out.println(Integer.parseInt((String)
				// jsonObject.get("theater")));

				rs = pstmt.executeQuery();
				if (rs != null) {
					// rs의 커서 위치를 record의 제일 마지막으로 옮기기
					rs.last();
					int total = rs.getRow(); // 현제 레코드의 위치를 반환
					rs.beforeFirst();// 현제 레코드를 제일 위로 옮겨놓음
					int count = 0;
					// StringBuffer 사용전 비우기!!!
					sb.delete(0, sb.length());

					sb.append("{");
					sb.append("\"responce\" : \"theater\",");
					sb.append("\"result\" : \"ok\",");
					sb.append("\"data\" : [");

					while (rs.next()) {
						count++;
						int theater_id = rs.getInt("theater_id");
						int runtime_id = rs.getInt("runtime_id");
						String time = rs.getString("time");

						sb.append("{");
						sb.append("\"theater_id\" : " + theater_id + ",");
						sb.append("\"runtime_id\" : " + runtime_id + ",");
						sb.append("\"time\" : \"" + time + "\"");

						if (count <= total - 1) {
							sb.append("},");
						} else {
							sb.append("}");
						}
					}
					sb.append("]");
					sb.append("}");
				} else {
					System.out.println("실패");
				}
				release(pstmt, rs);// 반납

			} else if (jsonObject.get("request").equals("benchInfo")) {

				String sql = "Select*from sit where theater_id=(select theater_id from theater where name=?)";
				pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				pstmt.setString(1, (String) jsonObject.get("data"));
				rs = pstmt.executeQuery();
				sb.delete(0, sb.length());

				sb.append("{");
				sb.append("\"responce\" : \"benchInfo\",");
				if (rs != null) {
					rs.last();
					int total = rs.getRow();
					int count = 0;
					rs.beforeFirst();
					sb.append("\"result\":\"ok\",");
					sb.append("\"data\":[");
					while (rs.next()) {
						count++;
						sb.append("{");
						sb.append("\"sit_name\":\"" + rs.getString("sit_name") + "\",");
						sb.append("\"sit_bool\":\"" + rs.getString("sit_bool") + "\"");
						if (count < total - 1) {
							sb.append("},");
						} else {
							sb.append("}");
						}
					}
					sb.append("]");
				} else {
					sb.append("\"result\":\"fail\"");
				}
				sb.append("}");
				release(pstmt, rs);
			} else if (jsonObject.get("request").equals("sitbool")) {
				JSONArray jsonArr = (JSONArray) jsonObject.get("data");
				int result1 = 0;
				for (int i = 0; i < jsonArr.size(); i++) {
					serverMain.area.append(jsonArr.get(i).toString() + "\n");
					JSONObject obj = (JSONObject) jsonArr.get(i);
					String sql1 = "select theater_id from theater where name=?";
					pstmt = con.prepareStatement(sql1);
					pstmt.setString(1, (String) obj.get("theater"));
					rs = pstmt.executeQuery();
					int theater = 0;
					while (rs.next()) {
						serverMain.area.append(Integer.toString(rs.getInt("theater_id")) + "\n");
						theater = rs.getInt("theater_id");
					}
					release(pstmt, rs);
					serverMain.area.append(Integer.toString(theater) + "관 좌석 예매 현황 검사\n");
					String sql = "Select sit_bool from sit where sit_name=? and theater_id=?";
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, (String) obj.get("sitName"));
					pstmt.setInt(2, theater);

					rs = pstmt.executeQuery();
					while (rs.next()) {
						if (rs.getInt("sit_bool") != 0) {
							result1 = rs.getInt("sit_bool");
						}
					}
				}
				if (result1 == 0) {

					for (int i = 0; i < jsonArr.size(); i++) {
						serverMain.area.append(jsonArr.get(i).toString() + "\n");
						JSONObject obj = (JSONObject) jsonArr.get(i);
						String sql1 = "select theater_id from theater where name=?";
						pstmt = con.prepareStatement(sql1);
						pstmt.setString(1, (String) obj.get("theater"));
						rs = pstmt.executeQuery();
						int theater = 0;
						while (rs.next()) {
							serverMain.area.append(Integer.toString(rs.getInt("theater_id")) + "\n");
							theater = rs.getInt("theater_id");
						}
						release(pstmt, rs);
						serverMain.area.append("관은?" + Integer.toString(theater) + "\n");
						String sql = "update sit set sit_bool=? where sit_name=? and theater_id=?";
						pstmt = con.prepareStatement(sql);
						pstmt.setInt(1, Integer.parseInt((String) obj.get("sit_bool")));
						pstmt.setString(2, (String) obj.get("sitName"));
						pstmt.setInt(3, theater);

						int result = pstmt.executeUpdate();
						serverMain.area.append("자석 Bool 처리 " + result);
						if (result != 0) {
							result1 = result;
						} else {
							result1 = 0;
						}
						release(pstmt);
					}
					sb.delete(0, sb.length());
					sb.append("{");
					sb.append("\"responce\" : \"sitbool\",");
					if (result1 != 0) {
						sb.append("\"result\" : \"ok\"");
					} else {
						sb.append("\"result\" : \"fail2\"");
					}
					sb.append("}");
				} else {
					sb.delete(0, sb.length());
					sb.append("{");
					sb.append("\"responce\" : \"sitbool\",");
					sb.append("\"result\" : \"fail1\"");
					sb.append("}");
				}
			} else if (jsonObject.get("request").equals("sitbool2")) {
				JSONArray jsonArr = (JSONArray) jsonObject.get("data");
				int result1 = 0;

				for (int i = 0; i < jsonArr.size(); i++) {
					serverMain.area.append(jsonArr.get(i).toString() + "\n");
					JSONObject obj = (JSONObject) jsonArr.get(i);
					String sql1 = "select theater_id from theater where name=?";
					pstmt = con.prepareStatement(sql1);
					pstmt.setString(1, (String) obj.get("theater"));
					rs = pstmt.executeQuery();
					int theater = 0;
					while (rs.next()) {
						serverMain.area.append(Integer.toString(rs.getInt("theater_id")) + "\n");
						theater = rs.getInt("theater_id");
					}
					release(pstmt, rs);
					serverMain.area.append("관은?" + Integer.toString(theater) + "\n");
					String sql = "update sit set sit_bool=? where sit_name=? and theater_id=?";
					pstmt = con.prepareStatement(sql);
					pstmt.setInt(1, Integer.parseInt((String) obj.get("sit_bool")));
					pstmt.setString(2, (String) obj.get("sitName"));
					pstmt.setInt(3, theater);

					int result = pstmt.executeUpdate();
					serverMain.area.append("자석 Bool 처리 " + result);
					if (result != 0) {
						result1 = result;
					} else {
						result1 = 0;
					}
					release(pstmt);
				}
				sb.delete(0, sb.length());
				sb.append("{");
				sb.append("\"responce\" : \"sitbool\",");
				if (result1 != 0) {
					sb.append("\"result\" : \"ok\"");
				} else {
					sb.append("\"result\" : \"fail2\"");
				}
				sb.append("}");

			} else if (jsonObject.get("request").equals("reserve")) {

				String sql1 = "insert into TICKET (TICKET_ID, TICKET_NUM, TICKET_IMG, MEMBER_ID) values(seq_ticket.nextval,?,?,?)";
				pstmt = con.prepareStatement(sql1);
				pstmt.setString(1, (String) jsonObject.get("ticket_name"));
				pstmt.setString(2, (String) jsonObject.get("fileName"));
				pstmt.setInt(3, Integer.parseInt((String) jsonObject.get("member_id")));
				int result1 = pstmt.executeUpdate();

				sb.delete(0, sb.length());
				sb.append("{");
				sb.append("\"responce\" : \"reserve\",");

				if (result1 != 0) {
					release(pstmt);
					sb.append("\"result1\" : \"ok\",");
					String sql2 = "insert into cinema_theater_ticket(CINEMA_THEATER_TICKET_ID,TICKET_ID,CINEMA_THEATER_ID) values(seq_cinema_theater_ticket.nextval,(select ticket_id from ticket where ticket_num=?),"
							+ " (select cinema_theater_id from CINEMA_THEATER where cinema_id=(select cinema_id from cinema where name=?) "
							+ " AND theater_id=(select theater_id from theater where name=?)))";
					pstmt = con.prepareStatement(sql2);
					pstmt.setString(1, (String) jsonObject.get("ticket_name"));
					pstmt.setString(2, (String) jsonObject.get("cinema"));
					pstmt.setString(3, (String) jsonObject.get("theater"));
					int result2 = pstmt.executeUpdate();
					if (result2 != 0) {
						sb.append("\"result2\" : \"ok\"");
					}
				} else {
					sb.append("\"result\" : \"fail\"");
				}
				sb.append("}");

				release(pstmt);
			} else if (jsonObject.get("request").equals("userlist")) {
				String sql = "select *from chatmember";
				pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = pstmt.executeQuery();
				if (rs != null) {
					// rs의 커서 위치를 record의 제일 마지막으로 옮기기
					rs.last();
					int total = rs.getRow(); // 현제 레코드의 위치를 반환
					rs.beforeFirst();// 현제 레코드를 제일 위로 옮겨놓음
					int count = 0;
					// StringBuffer 사용전 비우기!!!
					sb.delete(0, sb.length());

					sb.append("{");
					sb.append("\"responce\" : \"userlist\",");
					sb.append("\"result\" : \"ok\",");
					sb.append("\"data\" : [");
					while (rs.next()) {
						count++;
						int chatmember_id = rs.getInt("chatmember_id");
						String id = rs.getString("id");
						String password = rs.getString("password");
						String name = rs.getString("name");
						String profile = rs.getString("profile");
						String status = rs.getString("status");

						sb.append("{");
						sb.append("\"chatmember_id\" : " + chatmember_id + ",");
						sb.append("\"id\" : \"" + id + "\",");
						sb.append("\"password\" : \"" + password + "\",");
						sb.append("\"name\" : \"" + name + "\",");
						sb.append("\"profile\" : \"" + profile + "\",");
						sb.append("\"status\" : \"" + status + "\",");
						if (count <= total - 1) {
							sb.append("},");
						} else {
							sb.append("}");
						}
					}
					sb.append("]");
					sb.append("}");
				} else {
					sb.append("{");
					sb.append("\"responce\" : \"userlist\",");
					sb.append("\"result\" : \"fail\",");
					sb.append("}");
				}
				release(pstmt, rs);
			} else if (jsonObject.get("request").equals("westList")) {
				serverMain.area.append("영화관련 리스트 가져오기!" + "\n");

				String sql = "select a.CITY,c.NAME as cinema,t.NAME as theater from addr a,cinema c, theater t,cinema_theater ct,cinema_theater_movie ctm "
						+ " where a.addr_id=c.ADDR_ID and c.CINEMA_ID=ct.CINEMA_ID and t.THEATER_ID=ct.THEATER_ID and "
						+ " ct.CINEMA_THEATER_ID=ctm.CINEMA_THEATER_ID and ctm.MOVIE_ID=?";
				pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				pstmt.setInt(1, Integer.parseInt((String) jsonObject.get("movie_id")));
				serverMain.area.append("해당 영화는 : " + (String) jsonObject.get("movie_id") + "\n");
				rs = pstmt.executeQuery();

				sb.delete(0, sb.length());
				sb.append("{");
				sb.append("\"responce\" : \"westList\",");
				if (rs.last()) {
					serverMain.area.append("쿼리문 정상 수행 \n");
					sb.append("\"result\" : \"ok\",");
					sb.append("\"data\" : [");

					int total = rs.getRow();
					rs.beforeFirst();
					int count = 0;
					while (rs.next()) {
						count++;
						sb.append("{");
						sb.append("\"addr\":\"" + rs.getString("city") + "\",");
						sb.append("\"cinema\":\"" + rs.getString("cinema") + "\",");
						sb.append("\"theater\":\"" + rs.getString("theater") + "\"");

						if (count <= total - 1) {
							sb.append("},");
						} else {
							sb.append("}");
						}
					}
					sb.append("]");
				} else {
					sb.append("\"result\" : \"fail\",");
					sb.append("\"data\" : [{}]");
				}
				sb.append("}");

				release(pstmt, rs);

			} else if (jsonObject.get("request").equals("movie_update")) {
				String sql = "update movie set name=?,movie_img=?,producer=?,actor=?,runtime=?,story=?,opendate=? where movie_id=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, (String) jsonObject.get("name"));
				pstmt.setString(2, (String) jsonObject.get("movie_img"));
				pstmt.setString(3, (String) jsonObject.get("producer"));
				pstmt.setString(4, (String) jsonObject.get("actor"));
				pstmt.setString(5, (String) jsonObject.get("runtime"));
				pstmt.setString(6, (String) jsonObject.get("story"));
				pstmt.setString(7, (String) jsonObject.get("opendate"));
				pstmt.setInt(8, Integer.parseInt((String) jsonObject.get("movie_id")));

				int result = pstmt.executeUpdate();
				sb.delete(0, sb.length());
				sb.append("{");
				sb.append("\"responce\" : \"movie_update\",");
				if (result != 0) {
					sb.append("\"result\" : \"ok\"");
				} else {
					sb.append("\"result\" : \"fail\"");
				}
				sb.append("}");
				release(pstmt);// 반납
			} else if (jsonObject.get("request").equals("movie_insert")) {
				serverMain.area.append("영화등록\n");
				String sql = "insert into MOVIE (MOVIE_ID, NAME, STORY, MOVIE_IMG, THEATER_ID, OPENDATE, PRODUCER, ACTOR, RUNTIME)"
						+ " values(seq_movie.nextval,?,?,?,?,?,?,?,?)";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, (String) jsonObject.get("name"));
				pstmt.setString(2, (String) jsonObject.get("story"));
				pstmt.setString(3, (String) jsonObject.get("movie_img"));
				pstmt.setString(4, (String) jsonObject.get("theater_id"));
				pstmt.setString(5, (String) jsonObject.get("opendate"));
				pstmt.setString(6, (String) jsonObject.get("producer"));
				pstmt.setString(7, (String) jsonObject.get("actor"));
				pstmt.setString(8, (String) jsonObject.get("runtime"));

				int result = pstmt.executeUpdate();
				sb.delete(0, sb.length());
				sb.append("{");
				sb.append("\"responce\" : \"movie_insert\",");
				if (result != 0) {
					sb.append("\"result\" : \"ok\"");
				} else {
					sb.append("\"result\" : \"fail\"");
				}
				sb.append("}");
				release(pstmt);// 반납
			} else if (jsonObject.get("request").equals("excel_data")) {
				File file = new File((String) jsonObject.get("fileName"));

				try {
					XSSFWorkbook workbook = new XSSFWorkbook(file);
					XSSFSheet sheet = workbook.getSheet("movie");
					int result1 = 1;

					for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
						XSSFRow row = sheet.getRow(i);
						String sql = "insert into movie(movie_id,name,story,movie_img,theater_id,opendate,producer,actor,runtime) "
								+ "values(seq_movie.nextval,?,?,?,?,?,?,?,?)";
						pstmt = con.prepareStatement(sql);
						System.out.println("1," + sheet.getPhysicalNumberOfRows());
						for (int j = 0; j < row.getLastCellNum(); j++) {
							XSSFCell cell = row.getCell(j);
							if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
								System.out.println((j + 1) + cell.getStringCellValue());
								pstmt.setString(j + 1, cell.getStringCellValue());
							} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
								pstmt.setInt(j + 1, (int) cell.getNumericCellValue());
								System.out.println((j + 1) + "," + cell.getNumericCellValue());
							}
						}
						int result = pstmt.executeUpdate();
						if (result == 0) {
							result1 = 0;
						}
					}
					sb.delete(0, sb.length());
					sb.append("{");
					sb.append("\"responce\" : \"excel_data\",");
					if (result1 != 0) {
						sb.append("\"result\" : \"ok\"");
					} else {
						sb.append("\"result\" : \"fail\"");
					}
					sb.append("}");

				} catch (InvalidFormatException e) {
					e.printStackTrace();
				}

			} else if (jsonObject.get("request").equals("movie_delete")) {

				String sql = "delete from movie where movie_id =?";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt((String) jsonObject.get("movie_id")));

				int result = pstmt.executeUpdate();
				sb.delete(0, sb.length());
				sb.append("{");
				sb.append("\"responce\":\"movie_delete\",");
				if (result != 0) {
					sb.append("\"result\":\"ok\"");
				} else {
					sb.append("\"result\":\"fail\"");
				}
				sb.append("}");

			} else if (jsonObject.get("request").equals("memberInfo")) {
				/*
				 * MemberManager Class멤버정보 가져오기
				 */
				StringBuffer sql = new StringBuffer();
				sql.append("select * from member order by member_id asc");

				pstmt = con.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				rs = pstmt.executeQuery();
				if (rs != null) {
					// rs의 커서 위치를 record의 제일 마지막으로 옮기기
					rs.last();
					int total = rs.getRow(); // 현제 레코드의 위치를 반환
					rs.beforeFirst();// 현제 레코드를 제일 위로 옮겨놓음
					int count = 0;
					// StringBuffer 사용전 비우기!!!
					sb.delete(0, sb.length());

					sb.append("{");
					sb.append("\"responce\" : \"memberInfo\",");
					sb.append("\"result\" : \"ok\",");
					sb.append("\"data\" : [");
					while (rs.next()) {
						count++;
						String member_id = rs.getString("member_id");
						String id = rs.getString("id");
						String pwd = rs.getString("pwd");
						String name = rs.getString("name");
						String addr_id = rs.getString("addr_id");
						String question = rs.getString("question");
						String answer = rs.getString("answer");
						String member_img = rs.getString("member_img");
						String regdate = rs.getString("regdate");

						sb.append("{");
						sb.append("\"member_id\" : \"" + member_id + "\",");
						sb.append("\"id\" : \"" + id + "\",");
						sb.append("\"pwd\" : \"" + pwd + "\",");
						sb.append("\"name\" : \"" + name + "\",");
						sb.append("\"addr_id\" : \"" + addr_id + "\",");
						sb.append("\"question\" : \"" + question + "\",");
						sb.append("\"answer\" : \"" + answer + "\",");
						sb.append("\"member_img\" : \"" + member_img + "\",");
						sb.append("\"regdate\" : \"" + regdate + "\"");

						if (count <= total - 1) {
							sb.append("},");
						} else {
							sb.append("}");
						}
					}
					sb.append("]");
					sb.append("}");
				} else {
					sb.append("{");
					sb.append("\"responce\":\"memberInfo\",");
					sb.append("\"result\":\"fail\",");
					sb.append("\"data\":{}");
					sb.append("}");
				}

				release(pstmt, rs);// 반납
				// 클라이언트의 요청이 아이디 중복체크 이라면...
			} else if (jsonObject.get("request").equals("memberUpdate")) {
				/*
				 * MemberDetail Class 회원관리 -비밀번호 변경
				 */
				String sql = "update member set pwd=? where member_id=?";
				pstmt = con.prepareStatement(sql);
				System.out.println(sql);
				pstmt.setString(1, (String) jsonObject.get("pwd"));
				pstmt.setString(2, (String) jsonObject.get("member_id"));

				int result = pstmt.executeUpdate();

				sb.delete(0, sb.length());
				sb.append("{");
				sb.append("\"responce\" : \"memberUpdate\"");
				if (result != 0) {
					sb.append("\"result\" : \"ok\"");
				} else {
					sb.append("\"result\" : \"fail\"");
				}
				sb.append("}");
				release(pstmt);// 반납
			} else if (jsonObject.get("request").equals("member_delete")) {
				String sql = "delete from member where member_id =?";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt((String) jsonObject.get("member_id")));
				System.out.println(sql);
				int result = pstmt.executeUpdate();
				sb.delete(0, sb.length());
				sb.append("{");
				sb.append("\"responce\":\"member_delete\",");
				if (result != 0) {
					sb.append("\"result\":\"ok\"");
				} else {
					sb.append("\"result\":\"fail\"");
				}
				sb.append("}");
			}

			sendMsg(sb.toString()); // 다시 전송!!
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 데이터 베이스 관련 객체 닫는 메서드
	// DML(insert, update, delete)일 경우
	public void release(PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// select문일 경우
	public void release(PreparedStatement pstmt, ResultSet rs) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// 말하기
	public void sendMsg(String msg) {
		try {
			serverMain.area.append(msg + "\n");
			buffw.write(msg + "\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		while (true) {
			listen();
		}
	}

}
