import java.awt.*;
import java.util.*;
import java.util.List;

public class Map {

    private char[][] board;
    private int[][] tunnelPairs;
    private int[] charCounts;
    private int goalX, goalY;
    private HashMap<Point, Integer> distToGoal;
    private List<Point> tunnels = new ArrayList<>();
    protected static final int[] ENTER = {};

    protected Map(char[][] board, int[][] tunnels, int startX, int startY, int goalX, int goalY, int[] charCounts) {
        this.board = board; this.tunnelPairs = tunnels; this.goalX = goalX; this.goalY = goalY;
        this.charCounts = charCounts;
        Point start = new Point(startX, startY), goal = new Point(goalX, goalY);
        tunnelDijkstra(start, goal);
    }

    /**
     * Finds the shortest distance from every tunnel entrance to the goal,
     * by chebyshev distance and using tunnels.
     * The distance between tunnel entrance pairs is min(chebyshev, 2).
     * The distance between entrances to different tunnels is the chebyshev distance (initially).
     * Runs Dijkstra from goal on the set of tunnels, start, and goal.
     * Shortest distances are saved in 'distToGoal'.
     *
     * @param start Start location.
     * @param goal Goal location.
     */
    private void tunnelDijkstra(Point start, Point goal) {
        // Initialize variables and create nodes:
        List<Point> points = new ArrayList<>();
        HashMap<Point, Integer> tunnelNumber = new HashMap<>();
        distToGoal = new HashMap<>();
        for (int i = 0; i < tunnelPairs.length; i++) {
            int[] t = tunnelPairs[i];
            if (t[0] == -1) continue;
            Point p1 = new Point(t[0], t[1]), p2 = new Point(t[2], t[3]);
            points.add(p1); points.add(p2);
            tunnelNumber.put(p1, i); tunnelNumber.put(p2, i);
        }
        tunnels.addAll(points);
        points.add(start); points.add(goal);
        // Run Dijkstra:
        PriorityQueue<Point> pq = new PriorityQueue<>(Comparator.comparingInt(distToGoal::get));
        for (Point p : points) {
            distToGoal.put(p, Integer.MAX_VALUE);
        }
        distToGoal.put(goal, 0);
        pq.add(goal);
        while (!pq.isEmpty()) {
            Point current = pq.poll();
            int cost = distToGoal.get(current);
            for (Point p : points) {
                if (p.equals(current)) continue;
                int weight = chebyshev(p, current);
                if (Objects.equals(tunnelNumber.get(p), tunnelNumber.get(current))) {
                    weight = Math.min(weight, 2);
                }
                if (distToGoal.get(p) > cost + weight){  // Relax edge
                    distToGoal.put(p, cost + weight);
                    pq.add(p);
                }
            }
        }
    }

    protected int[] charCounts() {
        return this.charCounts;
    }

    /**
     * The goals function of the problem.
     * In this problem, the goal is a specific x,y location.
     *
     * @param n A node to check.
     * @return True iff the node is the location of the goal.
     */
    protected boolean goal(Node n) {
        return goal(n.x(), n.y());
    }

    protected boolean goal(int x, int y) {
        return x == goalX && y == goalY;
    }

    /**
     * Chebyshev distance between point A and point B.
     *
     * @param a Point A.
     * @param b Point B.
     * @return The Chebyshev distance between the points.
     */
    private int chebyshev(Point a, Point b) {
        return chebyshev(a.x, a.y, b.x, b.y);
    }

    /**
     * Chebyshev distance from (x1, y1) to (x2, y2).
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
     * The heuristic function.
     * If the location is the goal, heuristic is 0.
     * Else, the minimum of:
     * chebyshev distance to goal + 4
     * (because the last step will always cost 5, the price for stepping onto G),
     * and
     * chebyshev distance to the closest tunnel entrance + heuristic from tunnel to goal + 4.
     *
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @return The heuristic of the given location.
     */
    protected int heuristic(int x, int y) {
        if (goal(x, y)) return 0;
        int costHeuristic = chebyshev(x, y, goalX, goalY);
        for (Point t : tunnels) {
            int tempCost = chebyshev(x, y, t.x, t.y) + distToGoal.get(t);
            costHeuristic = Math.min(costHeuristic, tempCost);
        }
        return costHeuristic + 4;
    }

    /**
     * The 'f' function for the informed searches.
     *
     * @param n A node.
     * @return The f(n) = g(n) + h(n) value of n.
     */
    protected int f(Node n) {
        return n.getCost() + heuristic(n.x(), n.y());
    }

    /**
     * The cost of moving to a space on the grid, defined by the contents of the space ONLY.
     * Does not account for diagonal movement or tunnels.
     *
     * @param x x-coordinate of space.
     * @param y y-coordinate of space.
     * @return The cost of moving to that space if move is legal, else -1.
     */
    protected int cost(int x, int y, boolean diagonal, boolean supplied) {
        char ch = board[y][x];
        switch (ch) {
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
     * @param current The node we're moving from.
     */
    private Node enterTunnel(Node current) {
        int x = current.x(), y = current.y();
        int[] previousDir = current.getDir();
        char ch = board[y][x];
        if (ch >= '0' && ch <= '9' && previousDir.length != 0) {
            int number = Integer.parseInt(String.valueOf(ch));
            int[] pair = tunnelPairs[number];
            if (x == pair[0] && y == pair[1]) {
                x = pair[2]; y = pair[3];
            }
            else if (x == pair[2] && y == pair[3]) {
                x = pair[0]; y = pair[1];
            }
            return new Node(x, y, current.getCost() + 2, ch, ENTER, current);
        }
        return null;
    }

    /**
     * Returns the coordinates after move in requested direction, and the cost.
     * Checks if move is legal: no walls, smooth floor requires being supplied,
     * no going back in opposite of previous direction (unless supplying).
     * If move is illegal, returns null.
     *
     * @return An int array [new x, new y, cost of move, char in next spot, supplied] if valid, else null.
     */
    protected Node move(Node current, int[] dir) {
        if (dir.length == 0) return enterTunnel(current);  // If direction is 'Ent'
        int x = current.x(), y = current.y();
        int[] previousDir = current.getDir();
        boolean supplied = current.isSupplied();
        x += dir[0]; y += dir[1];  // Update coordinates
        // Verify that new coordinates are within borders:
        if (x >= 0 && y >= 0 & y < board.length && x < board[0].length) {
            char ch = board[y][x];
            // Verify that target location is not a wall or '~' without supplies:
            if (ch != '#' && (supplied || ch != '~')) {
                // Verify that target location is not an immediate backtrack (excluding supplying):
                if ((ch == '*' && !supplied) || (previousDir.length != 0 &&
                        !(previousDir[0] + dir[0] == 0 && previousDir[1] + dir[1] == 0))) {
                    boolean diagonal = dir[0] != 0 && dir[1] != 0;
                    int moveCost = cost(x, y, diagonal, supplied);
                    if (moveCost != -1){
                        return new Node(x, y, moveCost + current.getCost(), ch, dir, current);
                    }
                }
            }
        }
        return null;
    }
}
