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
	JPanel p_center; // ��ȭ Image �� �׺�����Ͱ� �׷��� JPanel
	JPanel p_south; // ��ȭInfo �� �׷��� JPanel
	JPanel p_prev; // bt_prev ����� panel
	JPanel p_next; // bt_next ����� panel
	JLabel p_movie; // ��ȭ Image�� �׷��� Jpanel
	JButton bt_prev; // ��ȭInfo ���� �̵� ��ư
	JButton bt_next; // ��ȭInfo ���� �̵� ��ư
	JButton bt_reserve;
	JPanel p_navi; // ��ȭ �׺������ Icon�� �׷��� JPanel
	int count = 0; // ��ȭ Dis Index
	ArrayList<String> movieCount= new ArrayList<String>();

	JLabel[] naviList;  // ��ȭ �׺������ Icon(JLabel)
	boolean flag = true;
	
	Image img;
	ImageIcon icon;
	// ��ȭ Image�� ���콺 �̺�Ʈ ����
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
	// �̺�Ʈ ���� �� ���(updateUI)�� ������
	Thread thread;
	int time = 1000;// Thread.sleep�� �ð��� ���ϴ� ���
	
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
		// ������ �����׽���
		thread = new Thread(this);
		thread.start();

		// p_movie.setBackground(Color.WHITE);
		p_center.setLayout(new BorderLayout());
		p_prev.setPreferredSize(new Dimension(60, 400));
		p_next.setPreferredSize(new Dimension(60, 400));

		p_center.setPreferredSize(new Dimension(width, 400));

		// p_prev.add(bt_prev);
		// p_next.add(bt_next);
		bt_prev.addActionListener(this); // Action�̺�Ʈ ����
		bt_next.addActionListener(this); // Action�̺�Ʈ ����

		p_center.add(bt_prev, BorderLayout.WEST);
		p_center.add(bt_next, BorderLayout.EAST);
		// movie img�� �׷��� Panel ����
		createMoviePanel();
		// movie nevigator ����
		createNavi(20);

		add(p_center);
		// movie info panel ����
		createMovieInfo(width);
		//setPreferredSize(new Dimension(width, height));
	}
	//�������� movie ���� ��������!
	public void getMovieInfo(){
		String json="{ \"request\" : \"movieInfo\"}";
		try {
			MainFrame.buffw.write(json+"\n");
			MainFrame.buffw.flush();
			String responce = MainFrame.buffr.readLine(); //���� �ޱ�
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
				MainFrame.createDialog("��ȭ������ �����ϴ�.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	

	// moviePanel ���� �ż���
	// moviePanel�� ����� �޼���
	public void createMoviePanel() {
		icon = new ImageIcon(MainFrame.imgPath+movieCount.get(count));
		icon.setImage(icon.getImage().getScaledInstance(360,515, Image.SCALE_SMOOTH));
		p_movie = new JLabel(icon) ;

		p_movie.setPreferredSize(new Dimension(280, 400));
		// image����(Click)�� �̺�Ʈ������ ����
		p_movie.addMouseListener(mouseAdapter);
		p_center.add(p_movie);
	}

	// movie nevigator ���� �ż���
	// moviePanel�Ʒ� �׺������icon ����� �޼���
	public void createNavi(int height) {
		naviList = new JLabel[movieCount.size()];
		p_navi = new JPanel();
		for (int i = 0; i < movieCount.size(); i++) {
			JLabel lb = new JLabel("��");
			if (i == count) {
				lb.setText("��");
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

	// movieInfoPanel ���� �޼���
	// movie ������ ��Ÿ�� Panel���� �޼���
	public void createMovieInfo(int width) {
		p_south = new JPanel();
		bt_reserve = new JButton("����");
		bt_reserve.addActionListener(this); // Action�̺�Ʈ ����
		p_south.setBackground(Color.DARK_GRAY);
		p_south.setPreferredSize(new Dimension(width, 100));
		p_south.add(bt_reserve);

		add(p_south, BorderLayout.SOUTH);
	}

	// count ���� �޼���
	public void countUp(){
				//JOptionPane.showMessageDialog(this, count);
				if(count < movieCount.size()-1){
					count++;
				}else{
					count =0;
				}
				time=100;
				//System.out.println("��ư"+count);
	}
	//count ���� �޼���
	public void countDown(){
			// JOptionPane.showMessageDialog(this, "p_movie ����");
			if (count > 0) {
				count--;
			} else {
				count = movieCount.size() - 1;
			}
			time = 100;
			//System.out.println("��ư" + count);
	}
	//���Ź�ư ���� ���� �޼���
	public void reserve(){
		int result=JOptionPane.showConfirmDialog(this, "���� ��ȭ�� �����Ͻðڽ��ϱ�?");
		//System.out.println(result);
		if(result==JOptionPane.YES_OPTION){
			String[] title=movieCount.get(count).split("\\.");//���� Ȯ���� �����ϱ����� Split
			MainFrame.setReserveInfo("movie_name",title[0]);//���������� ������ ��ȭ�̸� ���!
		//	System.out.println(MainFrame.getReserveInfo().get("movie_name"));
		//	System.out.println(title[0]);
			MainFrame.setCinemaSelect(title[0]);
		}else if(result==JOptionPane.NO_OPTION){
			MainFrame.setMainSelectMovie();
		}
	}

	// Action �̺�Ʈ ���۱��� �޼���(bt_prev,bt_next,bt_reserve)
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
				// System.out.println("������");
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
