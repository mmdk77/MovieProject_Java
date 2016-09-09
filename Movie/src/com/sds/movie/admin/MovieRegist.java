package com.sds.movie.admin;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sds.movie.client.MainFrame;
import com.sds.movie.client.TheaterSelect;

public class MovieRegist extends JPanel implements ActionListener, ItemListener {
	JPanel p_center, p_south;
	JPanel p_top, p_choice;
	ImageIcon icon;
	JLabel movie_img;

	Choice ch_addr;
	Choice ch_cinema;
	Choice ch_theater;

	JLabel la_name, la_producer, la_actor, la_runtime, la_story, la_file, la_openday;
	JTextField t_name, t_producer, t_actor, t_runtime, t_file, t_openday;
	JTextArea ta_story;
	JScrollPane scroll;
	JFileChooser chooser;

	JButton bt_open, bt_run;
	JButton bt_regist;

	FileInputStream fis;
	FileOutputStream fos;
	String savePath_img = "//M120226/movieproject_res/movie_img/";
	String savePath_file = "//M120226/movieproject_res/movie_file/";

	HashMap<String, Long> list = new HashMap<String, Long>();
	long theater_id;
	String filename;
	String excelFilename;

	public MovieRegist() {
		setLayout(new BorderLayout());
		p_center = new JPanel();
		p_south = new JPanel();
		p_top = new JPanel(new GridLayout(1, 2));
		p_choice = new JPanel(null);

		chooser = new JFileChooser("c:/");
		icon = new ImageIcon("//M120226/movieproject_res/movie_img/movie_default.png");
		movie_img = new JLabel(icon);
		movie_img.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int result = chooser.showOpenDialog(getParent());
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					filename = chooser.getName(file);
					System.out.println(filename);
					try {
						fis = new FileInputStream(file);
						fos = new FileOutputStream(savePath_img + filename);
						byte[] b = new byte[1024];
						while (fis.read(b) != -1) {
							fos.write(b);
							fos.flush();
						}
						icon = new ImageIcon(savePath_img + filename);
						icon.setImage(icon.getImage().getScaledInstance(120, 129, Image.SCALE_SMOOTH));
						movie_img.setIcon(icon);
						movie_img.updateUI();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					System.out.println(chooser.getSelectedFile());
				}

			}

		});

		ch_addr = new Choice();
		ch_addr.add("지역선택▼");
		ch_addr.addItemListener(this);
		ch_cinema = new Choice();
		ch_cinema.add("영화관선택▼");
		ch_cinema.addItemListener(this);
		ch_theater = new Choice();
		ch_theater.add("상영관선택▼");
		ch_theater.addItemListener(this);

		ch_addr.setBounds(0, 60, 100, 30);
		ch_cinema.setBounds(0, 100, 100, 30);
		ch_theater.setBounds(0, 140, 100, 30);

		p_choice.add(ch_addr);
		p_choice.add(ch_cinema);
		p_choice.add(ch_theater);

		p_top.add(movie_img);
		p_top.add(p_choice);

		la_name = new JLabel("제    목");
		la_producer = new JLabel("감    독");
		la_actor = new JLabel("출 연 진");
		la_runtime = new JLabel("상영 시간");
		la_openday = new JLabel("개봉일");
		la_story = new JLabel("줄 거 리");
		la_file = new JLabel("엑셀파일");

		t_name = new JTextField(15);
		t_producer = new JTextField(15);
		t_actor = new JTextField(15);
		t_runtime = new JTextField(15);
		t_openday = new JTextField(15);
		ta_story = new JTextArea(10, 5);
		scroll = new JScrollPane(ta_story);

		t_file = new JTextField(20);
		bt_open = new JButton("열기");
		bt_run = new JButton("Moives DB 등록");
		bt_regist = new JButton("등록");

		bt_run.addActionListener(this);
		bt_open.addActionListener(this);
		bt_regist.addActionListener(this);

		movie_img.setPreferredSize(new Dimension(200, 200));
		p_choice.setPreferredSize(new Dimension(150, 100));
		p_top.setPreferredSize(new Dimension(350, 200));

		la_name.setPreferredSize(new Dimension(200, 20));
		la_producer.setPreferredSize(new Dimension(200, 20));
		la_actor.setPreferredSize(new Dimension(200, 20));
		la_runtime.setPreferredSize(new Dimension(200, 20));
		la_openday.setPreferredSize(new Dimension(200, 20));
		la_story.setPreferredSize(new Dimension(350, 20));
		scroll.setPreferredSize(new Dimension(350, 100));

		p_center.add(p_top);
		p_center.add(la_name);
		p_center.add(t_name);
		p_center.add(la_producer);
		p_center.add(t_producer);
		p_center.add(la_actor);
		p_center.add(t_actor);
		p_center.add(la_runtime);
		p_center.add(t_runtime);
		p_center.add(la_openday);
		p_center.add(t_openday);
		p_center.add(la_story);
		p_center.add(scroll);
		p_center.add(bt_regist);

		p_south.add(la_file);
		p_south.add(t_file);
		p_south.add(bt_open);
		p_south.add(bt_run);

		p_center.setPreferredSize(new Dimension(400, 300));
		p_south.setPreferredSize(new Dimension(400, 80));
		add(p_center);
		add(p_south, BorderLayout.SOUTH);

		setSize(400, 600);
		setVisible(true);

		bt_run.setEnabled(false);
		requestCity();
	}

	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getSource();
		if (obj == ch_addr) {
			requestCinema();
		} else if (obj == ch_cinema) {
			requestTheater();
		} else if (obj == ch_theater) {
			theater_id = list.get(ch_theater.getSelectedItem());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == bt_regist) {
			insertMovieData();
		} else if (obj == bt_open) {
			fileInsert();
		} else if (obj == bt_run) {
			insertExcelData(excelFilename);
			// MainFrame.createDialog("서버에 excel파일 DB등록 요청하기!");
		}
	}

	public void requestCity() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"request\":\"city\"");
		sb.append("}");

		try {
			// 지역 요청
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();
			// 응답
			String msg = MainFrame.buffr.readLine();
			System.out.println("1111111" + msg);
			// 파싱시작.
			ch_addr.removeAll();
			ch_addr.add("지역선택▼");
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);
			JSONArray jsonArray = (JSONArray) jsonObject.get("data");

			if (jsonObject.get("result").equals("ok")) {
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject obj = (JSONObject) jsonArray.get(i);
					String region = (String) obj.get("city");
					// System.out.println(region);
					ch_addr.add(region);
				}

			} else if (jsonObject.get("result").equals("fail")) {
				MainFrame.createDialog("데이터를 가져오지 못했습니다.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public void requestCinema() {
		int region = ch_addr.getSelectedIndex();
		StringBuffer sb = new StringBuffer();
		System.out.println(region);

		sb.append("{");
		sb.append("\"request\":\"cinema\",");
		sb.append("\"city\":\"" + region + "\"");
		sb.append("}");

		try {
			// 지역 요청
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();

			System.out.println(sb.toString());

			// 응답
			String msg = MainFrame.buffr.readLine();
			System.out.println("222222222" + msg);

			// 파싱시작.
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);
			JSONArray jsonArray = (JSONArray) jsonObject.get("data");

			if (jsonObject.get("result").equals("ok")) {
				ch_cinema.removeAll();
				ch_cinema.add("영화관선택▼");
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject obj = (JSONObject) jsonArray.get(i);
					String name = (String) obj.get("name");
					ch_cinema.add(name);
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

	public void requestTheater() {
		int choiceCinema = ch_cinema.getSelectedIndex();
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append("\"request\":\"theater\",");
		sb.append("\"cinema\":\"" + choiceCinema + "\"");
		sb.append("}");

		System.out.println("dddddddddddddddddd" + sb.toString());
		try {
			// 지역 요청
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();

			// 응답
			String msg = MainFrame.buffr.readLine();
			System.out.println(msg);
			// 파싱시작.
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);
			JSONArray jsonArray = (JSONArray) jsonObject.get("data");

			if (jsonObject.get("result").equals("ok")) {
				ch_theater.removeAll();
				ch_theater.add("영화관선택▼");
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject obj = (JSONObject) jsonArray.get(i);
					String name = (String) obj.get("theater");

					list.put(name, (Long) obj.get("theater_id"));
					ch_theater.add(name);
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

	// 서버에 MovieInfo 등록 요청 메서드!
	public void insertMovieData() {
		System.out.println("1111111" + t_actor.getText());
		if (t_actor.getText().equals("") || t_name.getText().equals("") || t_openday.getText().equals("")
				|| t_producer.getText().equals("") || t_runtime.getText().equals("")) {
			MainFrame.createDialog("해당 목록을 모두 입력해 주세요");
			return;
		}
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append("\"request\": \"movie_insert\",");
		sb.append("\"name\": \"" + t_name.getText() + "\",");
		sb.append("\"movie_img\": \"" + filename + "\",");
		sb.append("\"theater_id\": \"" + theater_id + "\",");
		sb.append("\"producer\": \"" + t_producer.getText() + "\",");
		sb.append("\"actor\": \"" + t_actor.getText() + "\",");
		sb.append("\"runtime\": \"" + t_runtime.getText() + "\",");
		sb.append("\"story\": \"" + ta_story.getText() + "\",");
		sb.append("\"opendate\": \"" + t_openday.getText() + "\"");
		sb.append("}");
		System.out.println(sb.toString());

		try {
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();

			String responce = MainFrame.buffr.readLine();
			System.out.println(responce);

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(responce);
			if (jsonObject.get("responce").equals("movie_insert")) {
				if (jsonObject.get("result").equals("ok")) {
					MainFrame.createDialog("영화정보 등록 성공");
					MainFrame.setAdminVisble(0);
					setVisible(false);
				} else {
					MainFrame.createDialog("영화정보 등록 실패");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void fileInsert() {
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			excelFilename = chooser.getName(file);
			t_file.setText(excelFilename);
			try {
				fis = new FileInputStream(file);
				fos = new FileOutputStream(savePath_file + excelFilename);
				byte[] b = new byte[1024];
				int data;
				while ((data = fis.read(b)) != -1) {
					fos.write(b);
					fos.flush();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		bt_run.setEnabled(true);
	}

	public void insertExcelData(String fileName) {
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append("\"request\": \"excel_data\",");
		sb.append("\"fileName\": \"" + savePath_file + fileName + "\"");
		sb.append("}");

		try {
			System.out.println(sb.toString());
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();

			String responce = MainFrame.buffr.readLine();
			System.out.println(responce);

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(responce);
			if (jsonObject.get("result").equals("ok")) {
				MainFrame.createDialog("Excel data DB저장 완료");
			} else if (jsonObject.get("result").equals("fail")) {
				MainFrame.createDialog("Excel data DB저장 실패");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	/*
	 * public static void main(String[] args) { JFrame frame = new
	 * JFrame("영화등록화면"); MovieRegist movieRegist = new MovieRegist();
	 * frame.add(movieRegist); frame.setSize(400, 700); frame.setVisible(true);
	 * 
	 * }
	 */
}
