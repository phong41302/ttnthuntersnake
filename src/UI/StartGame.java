package UI;

import Mode.Snake;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.BorderLayout;

public class StartGame {

	private JFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartGame window = new StartGame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public StartGame() {
		initialize();
	}

	/**
	 * Cài đặt nội dung cho frame
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("SnakeAI");
		frame.setResizable(false);
		SnakePanel panel = new SnakePanel();
		new Thread(panel).start();// Tạo thread xử lí Frame - bắt đầu trò chơi
		panel.setBackground(Color.BLACK);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.setBounds(100, 100, Snake.map_size + 30, Snake.map_size + 50);// Frame size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
