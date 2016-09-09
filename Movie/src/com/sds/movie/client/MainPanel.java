package com.sds.movie.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.print.DocFlavor.STRING;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MainPanel extends JPanel implements ActionListener, Runnable {
	JPanel p_center; // 영화 Image 및 네비게이터가 그려질 JPanel
	JPanel p_south; // 영화Info 가 그려질 JPanel
	JPanel p_prev; // bt_prev 예비용 panel
	JPanel p_next; // bt_next 예비용 panel
	JLabel p_movie; // 영화 Image가 그려질 Jpanel
	JButton bt_prev; // 영화Info 이전 이동 버튼
	JButton bt_next; // 영화Info 이후 이동 버튼
	JButton bt_reserve;
	JPanel p_navi; // 영화 네비게이터 Icon이 그려질 JPanel
	int count = 0; // 영화 Dis Index
	ArrayList<String> movieCount= new ArrayList<String>();

	JLabel[] naviList;  // 영화 네비게이터 Icon(JLabel)
	boolean flag = true;
	
	Image img;
	ImageIcon icon;
	// 영화 Image의 마우스 이벤트 구현
	MouseAdapter mouseAdapter=new MouseAdapter(){
		public void mouseClicked(MouseEvent e){
			Object obj=e.getSource();
			
			if(obj.equals(p_movie)){
				System.out.println(movieCount.get(count));
				System.out.println("MovieInfo");
				
				MainFrame.setMovieInfoPanel(movieCount.get(count));
				return;
			}
			while(true){
				for(int i=0;i<naviList.length;i++){
					if(obj.equals(naviList[i])){
						count=i;
						p_center.remove(p_navi);
						createNavi(20);
						updateUI();
						return;
					}
				}
			}
		}
	};
	// 이벤트 동작 시 사용(updateUI)될 쓰레드
	Thread thread;
	int time = 1000;// Thread.sleep의 시간을 정하는 요소
	
	public MainPanel(int width, int height) {
		if(MainFrame.getCineInfo().size()==0){
			getMovieInfo();
		}
		
		setLayout(new BorderLayout());
		p_center = new JPanel();
		p_prev = new JPanel();
		p_next = new JPanel();
		bt_prev = new JButton("<");
		bt_next = new JButton(">");
		// 쓰레드 생성및시작
		thread = new Thread(this);
		thread.start();

		// p_movie.setBackground(Color.WHITE);
		p_center.setLayout(new BorderLayout());
		p_prev.setPreferredSize(new Dimension(60, 400));
		p_next.setPreferredSize(new Dimension(60, 400));

		p_center.setPreferredSize(new Dimension(width, 400));

		// p_prev.add(bt_prev);
		// p_next.add(bt_next);
		bt_prev.addActionListener(this); // Action이벤트 연결
		bt_next.addActionListener(this); // Action이벤트 연결

		p_center.add(bt_prev, BorderLayout.WEST);
		p_center.add(bt_next, BorderLayout.EAST);
		// movie img가 그려질 Panel 생성
		createMoviePanel();
		// movie nevigator 생성
		createNavi(20);

		add(p_center);
		// movie info panel 생성
		createMovieInfo(width);
		//setPreferredSize(new Dimension(width, height));
	}
	//서버에서 movie 정보 가져오기!
	public void getMovieInfo(){
		String json="{ \"request\" : \"movieInfo\"}";
		try {
			MainFrame.buffw.write(json+"\n");
			MainFrame.buffw.flush();
			String responce = MainFrame.buffr.readLine(); //응답 받기
			System.out.println(responce);
			MainFrame.getCineInfo().clear();
			movieCount.removeAll(movieCount);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject =(JSONObject)jsonParser.parse(responce);
			if(jsonObject.get("result").equals("ok")){
				JSONArray jsonArray = (JSONArray)jsonObject.get("data");
				for(int i=0; i < jsonArray.size(); i++){
					HashMap<String,String> hashMap = new HashMap<String,String>();
					JSONObject obj = (JSONObject)jsonArray.get(i);
					//System.out.println(obj.get("movie_id"));
					hashMap.put("movie_id",(String)obj.get("movie_id"));
					hashMap.put("name",(String)obj.get("name"));
					hashMap.put("story",(String)obj.get("story"));
					hashMap.put("movie_img",(String)obj.get("movie_img"));
					hashMap.put("theater_id",(String)obj.get("theater_id"));
					hashMap.put("opendate",(String)obj.get("opendate"));
					hashMap.put("producer",(String)obj.get("producer"));
					hashMap.put("actor",(String)obj.get("actor"));
					hashMap.put("runtime",(String)obj.get("runtime"));
					//	System.out.println(arr);
					movieCount.add((String)obj.get("movie_img"));
					MainFrame.getCineInfo().put((String)obj.get("movie_img"),hashMap);
					//System.out.println(MainFrame.getCineInfo().get((String)obj.get("movie_img")));
				}
			//	System.out.println(movieCount.size());
			}else{ 
				MainFrame.createDialog("영화정보가 없습니다.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	

	// moviePanel 생성 매서드
	// moviePanel을 만드는 메서드
	public void createMoviePanel() {
		icon = new ImageIcon(MainFrame.imgPath+movieCount.get(count));
		icon.setImage(icon.getImage().getScaledInstance(360,515, Image.SCALE_SMOOTH));
		p_movie = new JLabel(icon) ;

		p_movie.setPreferredSize(new Dimension(280, 400));
		// image선택(Click)시 이벤트리스너 연결
		p_movie.addMouseListener(mouseAdapter);
		p_center.add(p_movie);
	}

	// movie nevigator 생성 매서드
	// moviePanel아래 네비게이터icon 만드는 메서드
	public void createNavi(int height) {
		naviList = new JLabel[movieCount.size()];
		p_navi = new JPanel();
		for (int i = 0; i < movieCount.size(); i++) {
			JLabel lb = new JLabel("●");
			if (i == count) {
				lb.setText("○");
			}
			// System.out.println(280/movieCount);
			lb.setPreferredSize(new Dimension(200 / movieCount.size(), height));
			naviList[i] = lb;
			naviList[i].addMouseListener(mouseAdapter);
			p_navi.add(naviList[i]);
		}
		p_center.add(p_navi, BorderLayout.SOUTH);
		updateUI();
	}

	// movieInfoPanel 생성 메서드
	// movie 정보를 나타낼 Panel생성 메서드
	public void createMovieInfo(int width) {
		p_south = new JPanel();
		bt_reserve = new JButton("예매");
		bt_reserve.addActionListener(this); // Action이벤트 연결
		p_south.setBackground(Color.DARK_GRAY);
		p_south.setPreferredSize(new Dimension(width, 100));
		p_south.add(bt_reserve);

		add(p_south, BorderLayout.SOUTH);
	}

	// count 증가 메서드
	public void countUp(){
				//JOptionPane.showMessageDialog(this, count);
				if(count < movieCount.size()-1){
					count++;
				}else{
					count =0;
				}
				time=100;
				//System.out.println("버튼"+count);
	}
	//count 감소 메서드
	public void countDown(){
			// JOptionPane.showMessageDialog(this, "p_movie 이전");
			if (count > 0) {
				count--;
			} else {
				count = movieCount.size() - 1;
			}
			time = 100;
			//System.out.println("버튼" + count);
	}
	//예매버튼 관련 동작 메서드
	public void reserve(){
		int result=JOptionPane.showConfirmDialog(this, "현재 영화를 선택하시겠습니까?");
		//System.out.println(result);
		if(result==JOptionPane.YES_OPTION){
			String[] title=movieCount.get(count).split("\\.");//파일 확장자 제거하기위해 Split
			MainFrame.setReserveInfo("movie_name",title[0]);//예매정보에 선택한 영화이름 담기!
		//	System.out.println(MainFrame.getReserveInfo().get("movie_name"));
		//	System.out.println(title[0]);
			MainFrame.setCinemaSelect(title[0]);
		}else if(result==JOptionPane.NO_OPTION){
			MainFrame.setMainSelectMovie();
		}
	}

	// Action 이벤트 동작구현 메서드(bt_prev,bt_next,bt_reserve)
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj.equals(bt_prev)) {
			countDown();
		}else if(obj.equals(bt_next)){
			countUp();
		}else if(obj.equals(bt_reserve)){
			reserve();
		}

	}

	@Override
	public void run() {
		while (flag) {
			try {
				Thread.sleep(time);
				// System.out.println("동작중");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			p_center.remove(p_navi);
			p_center.remove(p_movie);
			createMoviePanel();
			createNavi(20);
			updateUI();
			time = 1000;
		}
	}
}
