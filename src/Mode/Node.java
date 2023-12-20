package Mode;

public class Node {
	private int G = 0; //giá trị hiện tại
	private int H = 0; //giá trị heuristic
	private Node father; //node cha của node này
	private int x, y; //tọa độ node

	public Node() {
	}

	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getF() {
		return G + H;
	}

	public int getG() {
		return G;
	}

	public void setG(int g) {
		G = g;
	}

	public void setH(int h) {
		H = h;
	}

	public Node getFather() {
		return father;
	}

	public void setFather(Node father) {
		this.father = father;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	// overrive toString
	public String toString() {
		return x + "-" + y;
	}

	// override phương thức kiểm tra bằng nhau
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Node) {
			Node antherNode = (Node) obj;
			if (this.x == antherNode.x && this.y == antherNode.y) {
				return true;
			}
		}
		return false;
	}

}
