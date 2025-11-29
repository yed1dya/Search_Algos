public class Map {

    private char[][] board;
    private int[][] tunnels;
    private int goalX, goalY, width, height;
    private boolean supplied;

    protected Map(char[][] board, int[][] tunnels, int goalX, int goalY){
        this.board = board; this.tunnels = tunnels; this.goalX = goalX; this.goalY = goalY;
        this.supplied = false; this.height = board.length; this.width = board[0].length;
    }

    /**
     * Provides the character at a location on the map.
     *
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @return The char at location x,y on the map.
     */
    protected char charAt(int x, int y){
        return board[y][x];
    }

    /**
     * The goals function of the problem.
     * In this problem, the goal is a specific x,y location.
     *
     * @param n A node to check.
     * @return True iff the node is the location of the goal.
     */
    protected boolean goal(Node n){
        return n.x() == goalX && n.y() == goalY;
    }

    /**
     * The heuristic function we use - Manhattan distance.
     *
     * @param n A node.
     * @return The heuristic of the given node.
     */
    protected int heuristic(Node n){
        int x = n.x(), y = n.y();
        return Math.abs(x - goalX) + Math.abs(y - goalY);
    }

    /**
     * The 'f' function for the informed searches.
     *
     * @param n A node.
     * @return The f(n) value of n.
     */
    protected int f(Node n){
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
    private int cost(int x, int y, boolean diagonal){
        char c = board[y][x];
        switch (c){
            case '#': return -1;
            case '-', '*', 'S': return 1;
            case '^': return diagonal ? 10 : 5;
            case 'G': return 5;
            case '~': return 3;
        }
        if (c >= '0' && c <= '9') return 1;
        return -1;
    }

    /**
     * Returns the coordinates after move in requested direction, and the cost.
     * Checks if move is legal: no walls, smooth floor requires being supplied,
     * no going back in opposite of previous direction.
     * If move is illegal, returns null
     *
     * @return An int array [new x, new y, cost]
     */
    protected int[] checkMove(Node current, String dir){
        int x = current.x(), y = current.y();
        String previousDir = current.getDir();
        switch (dir){
            case "R": {
                if (x < width - 1 && !previousDir.equals("L")) x++;
                break;
            }
            case "RD": {
                if (x < width - 1 && y > 0 && !previousDir.equals("LU")){ y--; x++; }
                break;
            }
            case "D": {
                if (y > 0 && !previousDir.equals("U")) y--;
                break;
            }
            case "LD": {
                if (x > 0 && y > 0 && !previousDir.equals("RU")){ x--; y--; }
                break;
            }
            case "L": {
                if (x > 0 && !previousDir.equals("R")) x--;
                break;
            }
            case "LU": {
                if (x > 0 && y < height - 1 && !previousDir.equals("RD")){ x--; y++; }
                break;
            }
            case "U": {
                if (y < height - 1 && !previousDir.equals("D")) y++;
                break;
            }
            case "RU": {
                if (x < width - 1 && y < height - 1 && !previousDir.equals("LD")){ x++; y++; }
                break;
            }
            case "Ent": {
                char c = board[y][x];
                if (c >= '0' && c <= '9' && !previousDir.equals("Ent")) {
                    int number = Integer.parseInt(String.valueOf(c));
                    int[] pair = tunnels[number];
                    if (x == pair[0] && y == pair[1]) {
                        return new int[]{pair[2], pair[3], 2};
                    }
                    if (x == pair[2] && y == pair[3]) {
                        return new int[]{pair[0], pair[1], 2};
                    }
                } break;
            }
            default: return null;
        }
        char c = board[y][x];
        if (c != '#' && (supplied || c != '~')) {
            return new int[]{x, y, cost(x, y, dir.length() == 2)};
        }
        return null;
    }
}
