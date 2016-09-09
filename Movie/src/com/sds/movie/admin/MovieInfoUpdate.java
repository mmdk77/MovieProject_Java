package com.sds.movie.admin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sds.movie.client.MainFrame;

public class MovieInfoUpdate extends JFrame implements ActionListener {
	JPanel p_center;
	JPanel p_story;
	JScrollPane scroll;
	ImageIcon icon;
	JLabel img;
	JLabel id;
	JLabel name;
	JLabel producer;
	JLabel actor;
	JLabel runtime;
	JLabel story;
	JLabel openday;
	JTextField t_id;
	JTextField t_name;
	JTextField t_producer;
	JTextField t_actor;
	JTextField t_runtime;
	JTextField t_openday;
	JTextArea ta_story;
	JButton bt_update,bt_delete;
	JFileChooser chooser = new JFileChooser("C:/Users/student/Downloads/");
	String path = "//M120226/movieproject_res/movie_img/";
	FileInputStream fis;
	FileOutputStream fos;
	String movie_img;
	String movie_id;

	public MovieInfoUpdate(String filename, String movie_id, String movie_name, String movie_producer,
			String movie_actor, String movie_runtime, String movie_sytory, String movie_openday) {
		this.movie_id = movie_id;
		p_center = new JPanel();
		p_story = new JPanel(new BorderLayout());
		System.out.println("111111111"+filename);
		movie_img = filename;
		icon = new ImageIcon(path + filename);
		icon.setImage(icon.getImage().getScaledInstance(120, 129, Image.SCALE_SMOOTH));
		img = new JLabel(icon);
		img.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int result = chooser.showOpenDialog(getParent());
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String filename = chooser.getName(file);
					movie_img = filename;
					System.out.println(filename);
					try {
						fis = new FileInputStream(file);
						fos = new FileOutputStream(path + filename);
						byte[] b = new byte[1024];
						while (fis.read(b) != -1) {
							fos.write(b);
							fos.flush();
						}
						icon = new ImageIcon(path + filename);
						icon.setImage(icon.getImage().getScaledInstance(120, 129, Image.SCALE_SMOOTH));
						img.setIcon(icon);
						img.updateUI();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		id = new JLabel("ID");
		name = new JLabel("이름");
		producer = new JLabel("감독");
		actor = new JLabel("출연진");
		runtime = new JLabel("상영시간");
		story = new JLabel("줄거리");
		openday = new JLabel("개봉일");

		t_id = new JTextField(movie_id, 10);
		t_name = new JTextField(movie_name, 10);
		t_producer = new JTextField(movie_producer, 10);
		t_actor = new JTextField(movie_actor, 10);
		t_runtime = new JTextField(movie_runtime, 10);
		t_openday = new JTextField(movie_openday, 10);
		ta_story = new JTextArea(movie_sytory);
		scroll = new JScrollPane(ta_story);
		bt_update = new JButton("등록");
		bt_delete = new JButton("삭제");

		ta_story.setLineWrap(true);
		t_id.setEnabled(false);

		img.setPreferredSize(new Dimension(200, 200));
		id.setPreferredSize(new Dimension(120, 30));
		name.setPreferredSize(new Dimension(120, 30));
		producer.setPreferredSize(new Dimension(120, 30));
		actor.setPreferredSize(new Dimension(120, 30));
		runtime.setPreferredSize(new Dimension(120, 30));
		story.setPreferredSize(new Dimension(280, 30));
		p_story.setPreferredSize(new Dimension(280, 100));
		openday.setPreferredSize(new Dimension(120, 30));

		p_story.add(scroll);
		
		bt_update.addActionListener(this);
		bt_delete.addActionListener(this);
		
		p_center.add(img);
		p_center.add(id);
		p_center.add(t_id);
		p_center.add(name);
		p_center.add(t_name);
		p_center.add(producer);
		p_center.add(t_producer);
		p_center.add(actor);
		p_center.add(t_actor);
		p_center.add(runtime);
		p_center.add(t_runtime);
		p_center.add(story);
		p_center.add(p_story);
		p_center.add(openday);
		p_center.add(t_openday);
		p_center.add(bt_update);
		p_center.add(bt_delete);

		add(p_center);

		// setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(400, 0, 300, 700);
		setVisible(true);

	}

	public void deleteMovie(){
		String json = "{\"request\":\"movie_delete\",\"movie_id\":\""+movie_id+"\"}";
		
		try {
			MainFrame.buffw.write(json+"\n");
			MainFrame.buffw.flush();
			
			String responce=MainFrame.buffr.readLine();
			System.out.println(responce);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject)jsonParser.parse(responce);
			if(jsonObject.get("responce").equals("movie_delete")&&jsonObject.get("result").equals("ok")){
				MainFrame.createDialog("해당 영화가 삭제 되었습니다.");
				MainFrame.setAdminVisble(0);
				setVisible(false);
			}else{
				MainFrame.createDialog("해당 영화가 삭제가 실패하였습니다.");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		System.out.println(json);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj==bt_update){
			setMovieUpdate();
		}else if( obj== bt_delete){
			deleteMovie();
		}
	}

	// 서버로 update 정보 전송 메서드
	public void setMovieUpdate(){
		StringBuffer sb = new StringBuffer();
		
		sb.append("{");

		sb.append("\"request\": \"movie_update\",");
		sb.append("\"movie_id\": \""+t_id.getText()+"\",");
		sb.append("\"name\": \""+t_name.getText()+"\",");
		sb.append("\"movie_img\": \""+movie_img+"\",");
		sb.append("\"movie_img\": \""+movie_img+"\",");
		sb.append("\"producer\": \""+t_producer.getText()+"\",");
		sb.append("\"actor\": \""+t_actor.getText()+"\",");
		sb.append("\"runtime\": \""+t_runtime.getText()+"\",");
		sb.append("\"story\": \""+ta_story.getText()+"\",");
		sb.append("\"opendate\": \""+t_openday.getText()+"\"");
		sb.append("}");
		System.out.println(sb.toString());
	
		try {
			MainFrame.buffw.write(sb.toString()+"\n");
			MainFrame.buffw.flush();
			
			String responce =MainFrame.buffr.readLine();
			System.out.println(responce);
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject)jsonParser.parse(responce);
			if(jsonObject.get("responce").equals("movie_update")){
				if(jsonObject.get("result").equals("ok")){
					MainFrame.createDialog("영화정보 업데이트 성공");	
					MainFrame.setAdminVisble(0);
					setVisible(false);
				}else{
					MainFrame.createDialog("영화정보 업데이트 실패");
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
	}
	/*
	 * public static void main(String[]args){ new MovieInfoUpdate("터널.jpg", "1",
	 * "터널", "몰라", "하정우", "120", "터널 무너짐", "2016.08.19"); }
	 */
}
