package UI;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.*;

import AI.SnakeAI;
import Mode.Node;
import Mode.Snake;

public class SnakePanel extends JPanel implements Runnable {
	Snake snake;
	SnakeAI ai;
	boolean AI_loose = false;
	int snake_speed = 1; // giá trị càng lớn, tốc độ càng chậm

	public SnakePanel() {
		snake = new Snake();
		Node n = new Node(10, 10);// vị trí ban đầu của con rắn
		snake.getS().add(n);
		snake.setFirst(n);
		snake.setLast(n);
		snake.setTail(new Node(0, 10)); // Cài đặt vị trí đồ ăn
		snake.setFood(new Node(80, 80));
		ai = new SnakeAI();

	}

	public void paint(Graphics g) {
		super.paint(g);
		// Vẽ tường
		g.setColor(Color.ORANGE);
		g.drawRect(10, 10, Snake.map_size, Snake.map_size); // map range
		g.setColor(Color.WHITE);
		// Tô màu cho rắn
		paintSnake(g, snake);
		g.setColor(Color.WHITE);
		// Tô màu cho mồi
		paintFood(g, snake.getFood());
		// Tìm hướng di chuyển tối ưu
		int dir = ai.play(snake, snake.getFood());
		if (dir == -1) { // Không tìm thấy hướng di chuyển nào thì kết thúc trò chơi
			AI_loose = true;
		} else {
			snake.move(dir); //Di chuyển rắn
		}
	}

	// Vẽ rắn
	public void paintSnake(Graphics g, Snake snake) {
		// Tô màu cho rắn thông qua xét từng node một.
		for (Node n : snake.getS()) {
			if (n.toString().equals(snake.getFirst().toString())) {
				g.setColor(Color.GREEN); // Tô đầu rắn màu xanh
			}
			if (n.toString().equals(snake.getLast().toString())
					&& !snake.getFirst().toString().equals(snake.getLast().toString())) {
				g.setColor(Color.BLUE); // Tô đuôi rắn màu xanh
			}
			g.fillRect(n.getX(), n.getY(), snake.size, snake.size);
			g.setColor(Color.WHITE); // Tô thân con rán màu trắng
		}
	}

	// Vẽ mồi

	public void paintFood(Graphics g, Node food) {
		g.setColor(Color.RED);// Set màu đỏ
		g.fillRect(food.getX(), food.getY(), snake.size, snake.size);
	}

	public void run() {
		while (!AI_loose) {
			try {
				Thread.sleep(snake_speed);
				this.repaint();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		JOptionPane.showMessageDialog(null, "Lose", "Lose", JOptionPane.INFORMATION_MESSAGE);
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
