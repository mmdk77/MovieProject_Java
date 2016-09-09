package com.sds.movie.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TheaterSelect extends JPanel {

	// ��ü
	JPanel p_wrapper;

	// �󿵰� �ð� ���� �г�
	JPanel p_inner;
	JLabel lb_cinema;

	// �󿵰�
	JPanel p_theater;
	JLabel lb_theater;

	// ��ȭ�ð�
	JPanel p_time;
	JLabel lb_time;

	long theater_id;

	String cinema;
	String theater;
	
	public TheaterSelect(String cinema, String theater, long theater_id) {
		this.cinema = cinema;
		this.theater = theater;
		// ��ü Panel ����
		p_wrapper = new JPanel();
		// ��ü�г� layout

		// theater & time ���� �г�
		p_inner = new JPanel();
		// inner layout
		p_inner.setLayout(new GridLayout(2, 1));

		// �󿵰� Panel&������Ʈ ����
		p_theater = new JPanel();

		// �󿵰� ������Ʈ
		lb_theater = new JLabel(theater); // DB���� 1,2�� �޾ƾ���
		lb_theater.setPreferredSize(new Dimension(300, 45));
		lb_theater.setFont(new Font("����", 15, 45));

		// ��ȭ�ð�
		p_time = new JPanel();
		p_time.setLayout(new FlowLayout(FlowLayout.LEFT));

		requestRuntime(theater_id);

		// �󿵰� �г� ������Ʈ �߰�
		p_theater.add(lb_theater);

		// ��ü�г� �߰�
		p_inner.add(p_theater);
		//p_inner.setPreferredSize(new Dimension(300, 100));
		p_inner.add(p_time);

		p_wrapper.add(p_inner);
		add(p_wrapper);
	}

	public void requestRuntime(long theater_id) {

		StringBuffer sb = new StringBuffer();
		this.theater_id = theater_id;
		sb.append("{");
		sb.append("\"request\":\"time\"");
		sb.append("\"theater_id\":\"" + theater_id + "\"");
		sb.append("}");

		System.out.println(sb.toString());

		try {
			// ���� ��û
			MainFrame.buffw.write(sb.toString() + "\n");
			MainFrame.buffw.flush();

			// ����
			String msg = MainFrame.buffr.readLine();
			System.out.println("99999999999" + msg);

			// �Ľ̽���.

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);
			JSONArray jsonArray = (JSONArray) jsonObject.get("data");

			// System.out.println("�ð��ð�����");
			if (jsonObject.get("result").equals("ok")) {
				//JOptionPane.showMessageDialog(this, 50*(jsonArray.size()/8));
				p_inner.setPreferredSize(new Dimension(300, 50*(jsonArray.size()/8)));//��ȭ�ð� ������ ���� �г� ���� ����
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject obj = (JSONObject) jsonArray.get(i);
					// �ð� ����
					lb_time = new JLabel((String) obj.get("time")); // �����ð� �ݺ���� for&while(�ð��� DB�� ������ִ½ð��������;���)
					lb_time.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							System.out.print((String) obj.get("time") + "�̽ð� ��ȭ����");
							//���� ���� ���(��ȭ��,�󿵰�,�ð�)
							MainFrame.setReserveInfo("cinema", cinema);
							MainFrame.setReserveInfo("theater", theater);
							MainFrame.setReserveInfo("time", (String) obj.get("time"));
							//System.out.println(MainFrame.getReserveInfo());
							MainFrame.setBenchSelect(cinema, theater, (String) obj.get("time"));
						}

					});

					// �ð� �гο� ������Ʈ �߰�
					p_time.add(lb_time);

				}
			} else if (jsonObject.get("result").equals("fail")) {
				MainFrame.createDialog("�����͸� �������� ���߽��ϴ�. �̰Ž� ����");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}