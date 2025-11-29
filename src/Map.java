public class Map {

    private char[][] board;
    private int[][] tunnels;
    private int goalX, goalY;

    protected Map(char[][] board, int[][] tunnels, int goalX, int goalY){
        this.board = board; this.tunnels = tunnels; this.goalX = goalX; this.goalY = goalY;
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
     * Helper function - Chebyshev distance.
     *
     * @param x1 x-coordinate of point 1.
     * @param y1 y-coordinate of point 1.
     * @param x2 x-coordinate of point 2.
     * @param y2 y-coordinate of point 2.
     * @return The Chebyshev distance between the points.
     */
    private int chebyshev(int x1, int y1, int x2, int y2) {
        return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    /**
     * Helper function - "tunnel distance".
     * Iterates over all tunnel entrances,
     * returns the chebyshev distance from x,y to the closest one.
     *
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @return The chebyshev distance from point to the closest tunnel
     */
    private int tunnelDistance(int x, int y){
        int distance = Integer.MAX_VALUE;
        for (int[] tunnel : tunnels){
            int closestTunnel = Math.min(chebyshev(x, y, tunnel[0], tunnel[1]),
                    chebyshev(x, y, tunnel[2], tunnel[3]));
            distance = Math.min(distance, closestTunnel);
        }
        return distance;
    }

    /**
     * The heuristic function.
     * Minimum of:
     * chebyshev distance to goal, and
     * chebyshev distance to the closest tunnel entrance + 2.
     *
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @return The heuristic of the given location.
     */
    protected int heuristic(int x, int y){
        return Math.min(chebyshev(x, y, goalX, goalY), tunnelDistance(x, y) + 2);
    }

    /**
     * The 'f' function for the informed searches.
     *
     * @param n A node.
     * @return The f(n) value of n.
     */
    protected int f(Node n){
        return f(n.x(), n.y(), n.getCost());
    }

    /**
     * The 'f' function for the informed searches.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param cost Current cost.
     * @return The f(n) value of the location with the given cost.
     */
    protected int f(int x, int y, int cost){
        return cost + heuristic(x, y);
    }

    /**
     * The cost of moving to a space on the grid, defined by the contents of the space ONLY.
     * Does not account for diagonal movement or tunnels.
     *
     * @param x x-coordinate of space.
     * @param y y-coordinate of space.
     * @return The cost of that space.
     */
    private int cost(int x, int y, boolean diagonal, boolean supplied){
        char ch = board[y][x];
        switch (ch){
            case '#': return -1;
            case '-', '*', 'S': return 1;
            case '^': return diagonal ? 10 : 5;
            case 'G': return 5;
            case '~': {
                if (supplied) return 3;
                else return -1;
            }
        }
        if (ch >= '0' && ch <= '9') return 1;
        return -1;
    }

    /**
     * Helper function for "Ent" direction.
     * Checks if space is a tunnel, and finds the other side.
     *
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @param previousDir The previous direction.
     * @return [new x, new y, cost of move, tunnel number] if valid, else null.
     */
    private int[] enterTunnel(int x, int y, int[] previousDir){
        char ch = board[y][x];
        if (ch >= '0' && ch <= '9' && previousDir.length != 0) {
            int number = Integer.parseInt(String.valueOf(ch));
            int[] pair = tunnels[number];
            if (x == pair[0] && y == pair[1]) {
                return new int[]{pair[2], pair[3], 2, ch};
            }
            if (x == pair[2] && y == pair[3]) {
                return new int[]{pair[0], pair[1], 2, ch};
            }
        }
        return null;
    }

    /**
     * Returns the coordinates after move in requested direction, and the cost.
     * Checks if move is legal: no walls, smooth floor requires being supplied,
     * no going back in opposite of previous direction (unless supplying).
     * If move is illegal, returns null.
     *
     * @return An int array [new x, new y, cost of move, char in next spot] if valid, else null.
     */
    protected int[] checkMove(Node current, int[] dir){
        int x = current.x(), y = current.y();
        int[] previousDir = current.getDir();
        if (dir.length == 0) return enterTunnel(x, y, previousDir);
        x += dir[0]; y += dir[1];
        if (x < 0 || y < 0 || y >= board.length || x >= board[0].length) return null;
        char ch = board[y][x];
        if (ch != '*' && previousDir.length != 0){
            int xMoved = previousDir[0] + dir[0], yMoved = previousDir[1] + dir[1];
            if (xMoved == 0 && yMoved == 0) return null;
        }
        boolean supplied = current.isSupplied(),
                diagonal = dir[0] != 0 && dir[1] != 0;
        int cost = cost(x, y, diagonal, supplied);
        if (ch != '#' && (supplied || ch != '~') && cost != -1) {
            return new int[]{x, y, cost, ch};
        }
        return null;
    }
}
