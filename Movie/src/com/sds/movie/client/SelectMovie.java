package com.sds.movie.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SelectMovie extends JPanel{
	 JPanel panel; //���� Ŭ�������� �ڽ��� ǥ���ϱ����� ����
	ArrayList<JPanel> jpanelList;
	ArrayList<String> movieList;

	public SelectMovie() {
		panel = this;
		jpanelList = new ArrayList<JPanel>();
		movieList = new ArrayList<String>();
	}
	
	public void drawMovieList(){
		System.out.println("1111"+MainFrame.mainPanel.movieCount.size());
		//movieList �����!!
		movieList.removeAll(movieList);
		for(int i=0; i <MainFrame.mainPanel.movieCount.size();i++){
			movieList.add(MainFrame.mainPanel.movieCount.get(i));
		}
		//jpanelList �����!!
		for(int i=0; i < movieList.size();i++){
			JPanel p_movie = new JPanel();
			p_movie.setPreferredSize(new Dimension(350, 50));
			p_movie.setBackground(Color.CYAN);
			String[] title=movieList.get(i).split("\\.");//���� Ȯ���� �����ϱ����� Split
			JLabel la_name = new JLabel(title[0]);
			p_movie.add(la_name);
			p_movie.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					//JOptionPane.showMessageDialog(getParent(), la_name.getText()+"����");
					MainFrame.setCinemaSelect(la_name.getText());
				}
			});
			jpanelList.add(p_movie);
			this.add(p_movie);
			this.setPreferredSize(new Dimension(400, movieList.size()/2*50));
		}
	}
	
	public void drawMovieList(String JLabel){
		
		//movieList �����!!
		movieList.removeAll(movieList);
		for(int i=0; i <MainFrame.mainPanel.movieCount.size();i++){
			movieList.add(MainFrame.mainPanel.movieCount.get(i));
		}
		System.out.println("1111"+movieList.size());
		//jpanelList �����!!
		for(int i=0; i < movieList.size(); i++){
			String[] title=movieList.get(i).split("\\.");
			ImageIcon icon = new ImageIcon(MainFrame.imgPath+movieList.get(i));
			icon.setImage(icon.getImage().getScaledInstance(200, 300, Image.SCALE_SMOOTH));
			JLabel p_movie = new JLabel(icon);
			p_movie.setPreferredSize(new Dimension(180, 300));
			p_movie.setBackground(Color.CYAN);
			p_movie.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					//JOptionPane.showMessageDialog(getParent(), la_name.getText()+"����");
					MainFrame.setReserveInfo("movie_name",title[0]);//���������� ������ ��ȭ�̸� ���!
					MainFrame.setCinemaSelect(title[0]);
				}
			});
			this.add(p_movie);
		}
		this.setPreferredSize(new Dimension(400, movieList.size()/2*300));
	}

}
