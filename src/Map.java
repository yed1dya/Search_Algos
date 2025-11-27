import java.util.ArrayList;
import java.util.Collections;

public class Map {
    private char[][] board;
    private int[][] tunnles;
    private int goalX, goalY, width, height;
    private boolean supplied;

    public Map(char[][] board, int[][] tunnels, int goalX, int goalY){
        this.board = board; this.tunnles = tunnels; this.goalX = goalX; this.goalY = goalY;
        this.supplied = false;
        this.height = board.length;
        this.width = board[0].length;
    }

    /**
     * The heuristic function we use - Manhattan distance.
     *
     * @param n A node.
     * @return The heuristic of the given node.
     */
    public int heuristic(Node n){
        int x = n.x(), y = n.y();
        return Math.abs(x - goalX) + Math.abs(y - goalY);
    }

    /**
     * The 'f' function for the informed searches.
     *
     * @param n A node.
     * @return The f(n) value of n.
     */
    public int f(Node n){
        return n.getCost() + heuristic(n);
    }

    /**
     * Generates the legal adjacent nodes from the current node, in clockwise / counter-clockwise order.
     *
     * @param current The current node.
     * @param clockwise Specifies which order to create the nodes.
     * @return An ArrayList of the adjacent nodes.
     */
    public ArrayList<Node> adjacents(Node current, boolean clockwise){
        ArrayList<Node> adjacents = new ArrayList<>(), middle = new ArrayList<>();
        Node next = null;
        // add right
        next = right(current);
        if (next != null) adjacents.add(next);
        // build middle list:
        next = rightDown(current);
        if (next != null) middle.add(next);
        next = down(current);
        if (next != null) middle.add(next);
        next = leftDown(current);
        if (next != null) middle.add(next);
        next = left(current);
        if (next != null) middle.add(next);
        next = leftUp(current);
        if (next != null) middle.add(next);
        next = up(current);
        if (next != null) middle.add(next);
        next = rightUp(current);
        if (next != null) middle.add(next);
        // if counter-clockwise, reverse it
        if (!clockwise) Collections.reverse(middle);
        // add middle list to adjacent list
        adjacents.addAll(middle);
        // add center
        next = enterTunnel(current);
        if (next != null) adjacents.add(next);
        return adjacents;
    }

    /**
     * The cost of moving to a space on the grid, defined by the contents of the space ONLY.
     * Does not account for diagonal movement or tunnels.
     *
     * @param x x-coordinate of space.
     * @param y y-coordinate of space.
     * @return The cost of that space.
     */
    private int cost(int x, int y){
        char c = board[y][x];
        switch (c){
            case '#': return -1;
            case '-', '*', 'S': return 1;
            case '^', 'G': return 5;
            case '~': return 3;
        }
        if (c >= '0' && c <= '9') return 1;
        return -1;
    }

    /**
     * Creates the node after moving from current node to the space at x,y.
     * If move is illegal, returns null.
     *
     * @param x Target x-coordinate.
     * @param y Target y-coordinate.
     * @param diagonal Whether the move is diagonal or not.
     * @param tunnel Whether the move is through a tunnel or not.
     * @param current The current node.
     * @return The node after the specified movement, or null if move is illegal.
     */
    private Node moveTo(int x, int y, boolean diagonal, boolean tunnel, Node current){
        char c = board[y][x]; Node next = null;
        if (c != '#' && (supplied || c != '~')){
            int extraCost = (diagonal && c == '^') ? 5 : tunnel ? 1 : 0;
            next = new Node(x, y, cost(x, y) + extraCost, current);
        }
        return next;
    }

    /**
     * Creates the node for a right movement.
     * Checks legality of move, then uses moveTo to create the node.
     * If move is illegal, returns null.
     *
     * @param current The current node.
     * @return The node after moving right, or null if move is illegal.
     */
    private Node right(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        if (x < width - 1){
            x++;
            next = moveTo(x, y, false, false, current);
        }
        return next;
    }

    /**
     * Creates the node for a right-down movement.
     * Checks legality of move, then uses moveTo to create the node.
     * If move is illegal, returns null.
     *
     * @param current The current node.
     * @return The node after moving right-down, or null if move is illegal.
     */
    private Node rightDown(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        if (x < width - 1 && y > 0){
            y--; x++;
            next = moveTo(x, y, true, false, current);
        }
        return next;
    }

    /**
     * Creates the node for a down movement.
     * Checks legality of move, then uses moveTo to create the node.
     * If move is illegal, returns null.
     *
     * @param current The current node.
     * @return The node after moving down, or null if move is illegal.
     */
    private Node down(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        if (y > 0){
            y--;
            next = moveTo(x, y, false, false, current);
        }
        return next;
    }

    /**
     * Creates the node for a left-down movement.
     * Checks legality of move, then uses moveTo to create the node.
     * If move is illegal, returns null.
     *
     * @param current The current node.
     * @return The node after moving left-down, or null if move is illegal.
     */
    private Node leftDown(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        if (x > 0 && y > 0){
            x--; y--;
            next = moveTo(x, y, true, false, current);
        }
        return next;
    }

    /**
     * Creates the node for a left movement.
     * Checks legality of move, then uses moveTo to create the node.
     * If move is illegal, returns null.
     *
     * @param current The current node.
     * @return The node after moving left, or null if move is illegal.
     */
    private Node left(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        if (x > 0){
            x--;
            next = moveTo(x, y, false, false, current);
        }
        return next;
    }

    /**
     * Creates the node for a left-up movement.
     * Checks legality of move, then uses moveTo to create the node.
     * If move is illegal, returns null.
     *
     * @param current The current node.
     * @return The node after moving left-up, or null if move is illegal.
     */
    private Node leftUp(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        if (x > 0 && y < height - 1){
            x--; y++;
            next = moveTo(x, y, true, false, current);
        }
        return next;
    }

    /**
     * Creates the node for an up movement.
     * Checks legality of move, then uses moveTo to create the node.
     * If move is illegal, returns null.
     *
     * @param current The current node.
     * @return The node after moving up, or null if move is illegal.
     */
    private Node up(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        if (y < height - 1){
            y++;
            next = moveTo(x, y, false, false, current);
        }
        return next;
    }

    /**
     * Creates the node for a right-up movement.
     * Checks legality of move, then uses moveTo to create the node.
     * If move is illegal, returns null.
     *
     * @param current The current node.
     * @return The node after moving right-up, or null if move is illegal.
     */
    private Node rightUp(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        if (x < width - 1 && y < height - 1){
            x++; y++;
            next = moveTo(x, y, true, false, current);
        }
        return next;
    }

    /**
     * Creates the node for enter-tunnel movement.
     * Checks legality of move, then uses moveTo to create the node.
     * If move is illegal, returns null.
     *
     * @param current The current node.
     * @return The node after moving enter-tunnel, or null if move is illegal.
     */
    private Node enterTunnel(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        char c = board[y][x];
        if (c >= '0' && c <= '9') {
            int number = Integer.parseInt("" + c);
            int[] pair = tunnles[number];
            if (x == pair[0] && y == pair[1]){
                x = pair[2]; y = pair[3];
            }
            else if (x == pair[2] && y == pair[3]) {
                x = pair[0]; y = pair[1];
            }
            next = moveTo(x, y, false, true, current);
        }
        return next;
    }
}
