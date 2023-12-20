package Mode;

import java.util.ArrayList;
import java.util.HashSet;

public class Snake {
	/**
	 * Biến ngăn chặn vòng lặp vô hạn
	 */
	public int c = 0;
	/**
	 * Kích thước thân rắn (mỗi đốt thân rắn có kích thước size x size)
	 */
	public final static int size = 10;
	/**
	 * kích thước Map (map_size x map_size)
	 */
	public final static int map_size = 200;
	/**
	 * Vị trí đầu Snake
	 */
	private Node first;
	/**
	 * Vị trí sao lưu đuôi snake (Để phục vụ tìm đường đi đến đuôn rắn)
	 */
	private Node tail;
	/**
	 * Vị trí đuôi snake (node cuối của snake)
	 */
	private Node last;
	/**
	 * Mảng lưu trữ vị trí từng đốt thân của rắn
	 */
	private ArrayList<Node> s = new ArrayList<Node>();
	/**
	 * Biến dùng để tra cứu nhanh thân rắn - tăng hiệu suất xử lí khi kích thước map lớn, độ dài rắn càng tăng
	 */
	private HashSet<String> map = new HashSet<String>();

	/**
	 * Điều hướng
	 */
	private int dir;// 8 - up | 6 - right | 2 - down | 4 - left
	/**
	 * Mồi
	 */
	private Node food;

	public Snake() {

	}
	public Snake(Node first, Node last, Node food, Node tail) {
		this.first = first;
		this.last = last;
		this.food = food;
		this.tail = tail;
	}

	/**
	 * Thêm node vào rắn (khi rắn di chuyển hoặc tăng chiều dài rắn khi ăn mồi)
	 */
	private void add_Node(Node n) {
		s.add(0, n);
		first = s.get(0);

		// Nếu nút được thêm vào không phải là thức ăn, loại bỏ phần đuôi để đảm bảo kích thước rắn không tăng lên do không phải là mồi
		if (!n.toString().equals(food.toString())) {
			tail = last; //sao lưu lại đuôi rắn cũ
			s.remove(last);
			last = s.get(s.size() - 1); //cập nhật đuôi rắn mới
		} else {// Nếu đúng tạo mồi mới (đồng thời tăng kích thước rắn - do không nhập nhật đuôi rắn nên rắn sẽ dài ra)
			food = RandomFood();
		}
	}

	/**
	 * Di chuyển rắn
	 */
	public void move() {
		// Nếu bằng 8 di chuyển lên
		if (dir == 8) {
			Node n = new Node(first.getX(), first.getY() - size);
			add_Node(n);
		}
		// Nếu bằng 6 di chuyển sang phải
		if (dir == 6) {
			Node n = new Node(first.getX() + size, first.getY());
			add_Node(n);
		}
		// Nếu bằng 2 di chuyển xuống
		if (dir == 2) {
			Node n = new Node(first.getX(), first.getY() + size);
			add_Node(n);
		}
		// Nếu bằng 4 di chuyển sang trái
		if (dir == 4) {
			Node n = new Node(first.getX() - size, first.getY());
			add_Node(n);
		}
		updataMap(s);
	}

	/**
	 * Di chuyển rắn
	 */
	public void move(int dir) {
		this.dir = dir;
		move();
	}

	/**
	 * Xác định hướng có thể di chuyển hay không
	 */
	public boolean canMove(int dir) {
		//map.contains(X + "-" + Y) kiểm tra node sắp đi đến có phải là thân rắn hay không

		// kiểm tra đã đến tường trên
		if (dir == 8) {
			int X = first.getX();
			int Y = first.getY() - size;
			if (Y < 10 || map.contains(X + "-" + Y)) {
				return false;
			} else
				return true;
		}
		// kiểm tra đã đến tường phải
		if (dir == 6) {
			int X = first.getX() + size;
			int Y = first.getY();
			if (X > Snake.map_size || map.contains(X + "-" + Y)) {
				return false;
			} else
				return true;
		}
		// Kiểm tra đã đến tường dưới
		if (dir == 2) {
			int X = first.getX();
			int Y = first.getY() + size;
			if (Y > Snake.map_size || map.contains(X + "-" + Y)) {
				return false;
			} else
				return true;
		}
		// Kiểm tra đã đến tường trái
		if (dir == 4) {
			int X = first.getX() - size;
			int Y = first.getY();
			if (X < 10 || map.contains(X + "-" + Y)) {
				return false;
			} else
				return true;
		}
		return false;
	}

	/**
	 * Đổi chuỗi sang Node
	 */
	public Node StringToNode(String s) {
		String[] str = s.split("-");
		int x = Integer.parseInt(str[0]);
		int y = Integer.parseInt(str[1]);
		return new Node(x, y);
	}

	/**
	 * Cập nhật thông tin rắn
	 */
	public void updataMap(ArrayList<Node> s) {
		map.clear();// Cập nhật HashSet dựa trên nội dung của ArrayList
		for (Node n : s) {
			map.add(n.toString());
		}
	}

	/**
	 * Sự xuất hiện ngẫu nhiên của mồi
	 */
	public Node RandomFood() {
		c = 0;
		while (true) {
			int x = 0, y;
			x = Snake.size * (int) (Math.random() * Snake.map_size / Snake.size) + 10;
			y = Snake.size * (int) (Math.random() * Snake.map_size / Snake.size) + 10;
			Node n = new Node(x, y);
			// Nếu thức ăn không phải là đốt thân của rắn thì trả về vị trí đặt thức ăn đó
			if (!s.contains(n)) {
				return n;
			}
		}
	}

	/**
	 * Trả về độ dài của rắn
	 */
	public int getLen() {
		return s.size();
	}

	public Node getTail() {
		return tail;
	}

	public void setTail(Node tail) {
		this.tail = tail;
	}

	public HashSet<String> getMap() {
		return map;
	}

	public Node getFirst() {
		return first;
	}

	public Node getLast() {
		return last;
	}

	public ArrayList<Node> getS() {
		return s;
	}

	public void setFirst(Node first) {
		this.first = first;
	}

	public void setLast(Node last) {
		this.last = last;
	}

	public void setS(ArrayList<Node> s) {
		this.s = s;
	}

	public void setMap(HashSet<String> map) {
		this.map = map;
	}

	public void setFood(Node food) {
		this.food = food;
	}

	public Node getFood() {
		return food;
	}
}
