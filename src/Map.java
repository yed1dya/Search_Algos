public class Map {
    private char[][] board;
    private int[][] tunnels;
    private int goalX, goalY, width, height;
    private boolean supplied;

    public Map(char[][] board, int[][] tunnels, int goalX, int goalY){
        this.board = board; this.tunnels = tunnels; this.goalX = goalX; this.goalY = goalY;
        this.supplied = false;
        this.height = board.length;
        this.width = board[0].length;
    }

    /**
     * Provides the character at a location on the map.
     *
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @return The char at location x,y on the map.
     */
    public char charAt(int x, int y){
        return board[y][x];
    }

    /**
     * The goals function of the problem.
     * In this problem, the goal is a specific x,y location.
     *
     * @param n A node to check.
     * @return True iff the node is the location of the goal.
     */
    public boolean goal(Node n){
        return n.x() == goalX && n.y() == goalY;
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
    private Node move(int x, int y, boolean diagonal, boolean tunnel, String dir, Node current){
        char c = board[y][x]; Node next = null; int currentCost = current.getCost();
        if (c != '#' && (supplied || c != '~')){
            int extraCost = (diagonal && c == '^') ? 5 : (tunnel ? 1 : 0);
            next = new Node(x, y, currentCost + cost(x, y) + extraCost, dir, current);
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
        int x = current.x(), y = current.y(); Node next = null; String previousDir = current.getDir();
        if (x < width - 1 && !previousDir.equals("L")){
            x++;
            next = move(x, y, false, false, "R", current);
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
        int x = current.x(), y = current.y(); Node next = null; String previousDir = current.getDir();
        if (x < width - 1 && y > 0 && !previousDir.equals("LU")){
            y--; x++;
            next = move(x, y, true, false, "RD", current);
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
        int x = current.x(), y = current.y(); Node next = null; String previousDir = current.getDir();
        if (y > 0 && !previousDir.equals("U")){
            y--;
            next = move(x, y, false, false, "D", current);
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
        int x = current.x(), y = current.y(); Node next = null; String previousDir = current.getDir();
        if (x > 0 && y > 0 && !previousDir.equals("RU")){
            x--; y--;
            next = move(x, y, true, false, "LD", current);
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
        int x = current.x(), y = current.y(); Node next = null; String previousDir = current.getDir();
        if (x > 0 && !previousDir.equals("R")){
            x--;
            next = move(x, y, false, false, "L", current);
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
        int x = current.x(), y = current.y(); Node next = null; String previousDir = current.getDir();
        if (x > 0 && y < height - 1 && !previousDir.equals("RD")){
            x--; y++;
            next = move(x, y, true, false, "LU", current);
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
        int x = current.x(), y = current.y(); Node next = null; String previousDir = current.getDir();
        if (y < height - 1 && !previousDir.equals("D")){
            y++;
            next = move(x, y, false, false, "U", current);
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
        int x = current.x(), y = current.y(); Node next = null; String previousDir = current.getDir();
        if (x < width - 1 && y < height - 1 && !previousDir.equals("LD")){
            x++; y++;
            next = move(x, y, true, false, "RU", current);
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
        int x = current.x(), y = current.y(); Node next = null; String previousDir = current.getDir();
        char c = board[y][x];
        if (c >= '0' && c <= '9' && !previousDir.equals("Ent")) {
            int number = Integer.parseInt("" + c);
            int[] pair = tunnels[number];
            if (x == pair[0] && y == pair[1]){
                x = pair[2]; y = pair[3];
            }
            else if (x == pair[2] && y == pair[3]) {
                x = pair[0]; y = pair[1];
            }
            next = move(x, y, false, true, "Ent", current);
        }
        return next;
    }

    public Node moveToDir(String dir, Node current){
        return switch (dir) {
            case "R" -> right(current);
            case "RD" -> rightDown(current);
            case "D" -> down(current);
            case "LD" -> leftDown(current);
            case "L" -> left(current);
            case "LU" -> leftUp(current);
            case "U" -> up(current);
            case "RU" -> rightUp(current);
            case "Ent" -> enterTunnel(current);
            default -> null;
        };
    }
}
