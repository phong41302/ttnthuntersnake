package AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import Mode.Node;
import Mode.Snake;

public class SnakeAI {


    // Sử dụng thuật toán BFS để tìm đường đi cho rắn
    public int BFS(Snake s, Node f) {
        Queue<Node> q = new LinkedList<Node>(); // Hàng đợi phục vụ cho thuật toán BFS
        Set<String> vis = new HashSet<String>();// Lưu trữ các node đã xét
        Map<String, String> path = new HashMap<String, String>(); // Dictionary với key là node hiện tại, value là node parent - dùng để truy vết đường đi
        Stack<String> stack = new Stack<String>();// Đường đi hiện tại

        q.add(s.getFirst());
        while (!q.isEmpty()) {
            Node n = q.remove();
            if (n.getX() == f.getX() && n.getY() == f.getY()) {
                // Nếu tìm thấy đường đi đến mồi, bắt đầu truy vết đường đi
                String state = f.toString();
                while (state != null && !state.equals(s.getFirst().toString())) {
                    stack.push(state);
                    state = path.get(state);
                }

                String[] str;

                //kiểm tra trường hợp mồi ở ngay trước đầu rắn do đó stack sẽ rỗng
                if (stack.isEmpty()) {
                    str = state.split("-");
                } else
                    str = stack.peek().split("-");
                int x = Integer.parseInt(str[0]);
                int y = Integer.parseInt(str[1]);
                if (x > s.getFirst().getX() && y == s.getFirst().getY()) {
                    return 6; //sang phải
                }
                if (x < s.getFirst().getX() && y == s.getFirst().getY()) {
                    return 4; //sang trái
                }
                if (x == s.getFirst().getX() && y > s.getFirst().getY()) {
                    return 2; //đi xuống
                }
                if (x == s.getFirst().getX() && y < s.getFirst().getY()) {
                    return 8; //đi lên
                }
            }
            //Tính toán 4 node kề với node đang xét (trên dưới trái phải)
            Node up = new Node(n.getX(), n.getY() - Snake.size);
            Node right = new Node(n.getX() + Snake.size, n.getY());
            Node down = new Node(n.getX(), n.getY() + Snake.size);
            Node left = new Node(n.getX() - Snake.size, n.getY());

            /**
             * up.getX() >= 10; up.getY() >= 10 là điều kiện biên dưới của tọa X và độ Y ~ snake's body size
             * s.getMap().contains(node.toString()) kiểm tra node có phải là 1 node nào đó thuộc thân rắn hay không? vì rắn không thể ăn chính mình được
             * vis.contains(node.toString()) kiểm tra node đã được xét hay chưa
            */

            if (!s.getMap().contains(up.toString()) && !vis.contains(up.toString()) && up.getX() <= Snake.map_size
                    && up.getX() >= 10 && up.getY() <= Snake.map_size && up.getY() >= 10) {
                q.add(up); //thêm vào hàng đợi
                vis.add(up.toString()); //đánh dấu đã xét
                path.put(up.toString(), n.toString());  //thêm thông tin node xét và node cha của node đó
            }
            if (!s.getMap().contains(right.toString()) && !vis.contains(right.toString())
                    && right.getX() <= Snake.map_size && right.getX() >= 10 && right.getY() <= Snake.map_size
                    && right.getY() >= 10) {
                q.add(right);
                vis.add(right.toString());
                path.put(right.toString(), n.toString());
            }
            if (!s.getMap().contains(down.toString()) && !vis.contains(down.toString()) && down.getX() <= Snake.map_size
                    && down.getX() >= 10 && down.getY() <= Snake.map_size && down.getY() >= 10) {
                q.add(down);
                vis.add(down.toString());
                path.put(down.toString(), n.toString());
            }
            if (!s.getMap().contains(left.toString()) && !vis.contains(left.toString()) && left.getX() <= Snake.map_size
                    && left.getX() >= 10 && left.getY() <= Snake.map_size && left.getY() >= 10) {
                q.add(left);
                vis.add(left.toString());
                path.put(left.toString(), n.toString());
            }
        }
        return -1; //không tìm thấy đường đi đến mồi
    }

    public int play(Snake snake, Node f) {
        Snake virSnake = new Snake(snake.getFirst(), snake.getLast(), snake.getFood(), snake.getTail()); // tạo một snake ảo
        virSnake.setS((ArrayList<Node>) snake.getS().clone()); //gán thông tin rắn cho rắn ảo (vị trí thân rắn)
        virSnake.setMap((HashSet<String>) snake.getMap().clone()); //gán thông tin rắn cho rắn ảo (vị trí thân rắn)

        // Xác định hướng đi đến mồi
        int realGoTofoodDir = BFS(snake, f);

        // Nếu xác định được hướng và đường đi đến mồi
        if (realGoTofoodDir != -1) {
            // Gửi rắn ảo đi ăn mồi
            while (!virSnake.getFirst().toString().equals(f.toString())) {
                virSnake.move(BFS(virSnake, f));
            }
            // Xác định sau khi ăn mồi, rắn ảo có thể tìm được đường đi đến đuôi của nó hay không
            int goToDailDir = Asearch(virSnake, virSnake.getTail());
            // Nếu rắn ảo sau khi ăn mồi, nó có thể tìm được đường đi đến đuôi chính nó, thì rắn thật sẽ ăn được mồi
            if (goToDailDir != -1)
                return realGoTofoodDir;
            else {
                /**
                 * Nếu sau khi rắn ảo ăn mồi mà không tìm được đường đi đến đuôi, ta sẽ cho rắn thật từng bước đi theo đuôi của nó
                 * theo đường đi xa nhất có thể cho đến khi tìm được đường đi thích hợp đến mồi,
                 * hoặc trường hợp rắn sẽ đi theo đuôi của mình vô hạn (rắn đã đi theo đuôi theo đường xa nhất nhưng vẫn không tìm được đường đến mồi),
                 * lúc này ta cần giới hạn số lần đi theo đuôi của rắn bằng một biến, nếu biến vượt giới hạn thì ta kết luận
                 * không còn đường đi nào cả, và trò chơi kết thúc
                */
                snake.c++;
                if (snake.c < 100) //giới hạn
                    return Asearch(snake, snake.getTail()); // tiếp tục đi theo đuôi nếu có thể
                else {
                    return realGoTofoodDir; // kết thúc trò chơi
                }
            }
        } else {// Nếu rắn không thể ăn mồi
            // Đi theo đường đi xa nhất đến đuôi rắn
            int realGoToDailDir = Asearch(snake, snake.getTail());
            if (realGoToDailDir == -1) {
                // Không thể đi đến đuôi của rắn, rắn sẽ di chuyển ngẫu nhiên
                realGoToDailDir = randomDir();
                int i = 0; //giới hạn số lần ngẫu nhiên
                while (!snake.canMove(realGoToDailDir)) {
                    // Có thể có vòng tuần hoàn, không thể đi về mọi phía
                    realGoToDailDir = randomDir();
                    i++;
                    if (i > 300)
                        return -1;// vượt quá giới hạn, rắn không thể tiếp tục di chuyển, trò chơi kết thúc
                }
                return realGoToDailDir;
            }
            return realGoToDailDir;
        }
    }

    /**
     * A* tìm đường đi xa nhất (giữa đầu rắn và đuôi rắn)
     */
    public int Asearch(Snake s, Node f) {
        ArrayList<Node> openList = new ArrayList<Node>();
        ArrayList<Node> closeList = new ArrayList<Node>();
        Stack<Node> stack = new Stack<Node>();// Đường đi của rắn
        openList.add(s.getFirst());// Nút bắt đầu trong open list
        s.getFirst().setH(dis(s.getFirst(), f));// Gán giá trị Heristic cho node đầu rắn

        while (!openList.isEmpty()) {
            Node now = null;
            int max = -1;
            for (Node n : openList) {// Tìm F lớn nhất (vì ta đang tìm đường đi xa nhất)
                // Nếu có nhiều node có cùng F, ta sẽ ưu tiên node mới thêm vào (openList thêm node mới vào cuối danh sách)
                if (n.getF() >= max) {
                    max = n.getF();
                    now = n;
                }
            }
            // Xóa nút hiện tại khỏi openList và thêm nó vào closeList
            openList.remove(now);
            closeList.add(now);
            // Xét 4 node kề node hiện tại
            Node up = new Node(now.getX(), now.getY() - Snake.size);
            Node right = new Node(now.getX() + Snake.size, now.getY());
            Node down = new Node(now.getX(), now.getY() + Snake.size);
            Node left = new Node(now.getX() - Snake.size, now.getY());
            ArrayList<Node> temp = new ArrayList<Node>(4);
            temp.add(up);
            temp.add(right);
            temp.add(down);
            temp.add(left);
            for (Node n : temp) {
                // Nếu node kề đang xét không hợp lệ (vị trí vượt ra khỏi biên) hoặc node kề đã nằm trong closeList, ta sẽ bỏ qua và xét node kề tiếp theo
                if (s.getMap().contains(n.toString()) || closeList.contains(n) || n.getX() > Snake.map_size
                        || n.getX() < Snake.size || n.getY() > Snake.map_size || n.getY() < Snake.size)
                    continue;

                // Nếu nút kề đang xét chưa có trong openList, ta sẽ thêm nút này vào openList
                if (!openList.contains(n)) {
                    n.setFather(now);
                    n.setG(now.getG() + 10);
                    n.setH(dis(n, f));
                    openList.add(n);
                    // Nếu node kề vừa thêm vào openList là node đích (đuôi rắn), thuật toán sẽ kết thúc và trả về hướng đi cho rắn
                    if (n.equals(f)) {
                        // Truy vết đường đi đến đầu rắn để tìm hướng đi tiếp theo cho rắn
                        Node node = openList.get(openList.size() - 1);
                        while (node != null && !node.equals(s.getFirst())) {
                            stack.push(node);
                            node = node.getFather();
                        }
                        int x = stack.peek().getX();
                        int y = stack.peek().getY();
                        if (x > s.getFirst().getX() && y == s.getFirst().getY()) {
                            return 6; // Sang phải
                        }
                        if (x < s.getFirst().getX() && y == s.getFirst().getY()) {
                            return 4; // Sang trái
                        }
                        if (x == s.getFirst().getX() && y > s.getFirst().getY()) {
                            return 2; // Đi xuống
                        }
                        if (x == s.getFirst().getX() && y < s.getFirst().getY()) {
                            return 8; // Đi lên
                        }
                    }
                }

                // Nếu nút kề nằm trong openList,
                // Xét nếu nút này có G lớn hơn tổng G của nút cha hiện tại và chi phí từ nút cha đó đến nút kề đang xét
                // Thì ta sẽ cập nhật lại G và nút cha của nút kề đang xét

                if (openList.contains(n)) {
                    if (n.getG() > (now.getG() + Snake.size)) {
                        n.setFather(now);
                        n.setG(now.getG() + Snake.size);
                    }
                }
            }
        }
        // Khi openList rỗng tức là thuật toán không thể tìm được đường đi
        return -1;
    }

    /**
     * Tính khoảng cách Manhattan = |X1-X2|+|Y2-Y1|
     */
    public int dis(Node src, Node des) {
        return Math.abs(src.getX() - des.getX()) + Math.abs(src.getY() - des.getY());
    }

    /**
     * Tạo hướng đi ngẫu nhiên
     */
    public int randomDir() {
        int dir = (int) Math.random() * 4;
        if (dir == 0)
            return 8; //đi lên
        else if (dir == 1)
            return 6; //sang phải
        else if (dir == 2)
            return 2; //đi xuống
        else
            return 4; //sang trái
    }
}
