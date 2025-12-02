import java.util.*;

public class BFS extends BreadthFirstSearchAlgo {

    /**
     * The open list (frontier), a queue.
     */
    private ArrayDeque<Node> queue = new ArrayDeque<>();

    /**
     * Constructor.
     *
     * @param clockwise order in which to create the next-step nodes.
     * @param withTime  whether to print runtime or not.
     * @param withOpen  whether to print current open list at every step.
     * @param map       the board to search.
     * @param start     start node.
     */
    protected BFS(boolean clockwise, boolean withTime, boolean withOpen,
               Map map, Node start){
        this.clockwise = clockwise; this.withTime = withTime; this.withOpen = withOpen;
        this.map = map; this.start = start;
    }

    /**
     * Runs BFS to find an optimal path from start to goal.
     * Uses a queue to store created nodes (FIFO).
     *
     * @return A string representing the path, or "no path" if no path exists.
     */
    @Override
    protected String findPath() {
        addToOpenList(start);
        while (!queue.isEmpty()) {
            if (withOpen) printOpenList();  // Option for debugging.
            Node current = removeHeadFromOpenList();
            if (current == null) return "no path";  // Safeguard.
            addToClosedList(current);
            if (clockwise) {  // Options for CW or CCW order of expansion:
                for (int[] dir : Ex1.clockwiseOrder) {
                    // Go to the next state. If it is the goal, the path will be returned.
                    String path = expandTo(current, dir);
                    if (path != null) return path;
                }
            } else {
                for (int[] dir : Ex1.counterClockwiseOrder) {
                    String path = expandTo(current, dir);
                    if (path != null) return path;
                }
            }
        }
        return "no path";
    }

    /**
     * Expand node in given direction.
     * Checks that the move is legal and creates the node.
     * Checks if the created nod is the goal.
     *
     * @param n Node to expand.
     * @param dir Direction to expand in.
     * @return Path to goal, if found. Else, null.
     */
    protected String expandTo(Node n, int[] dir){
        // Check validity of the move and get the results after the move:
        int[] checkNext = map.checkMove(n, dir);
        if (checkNext == null) return null;  // If the move is illegal.
        // Parse the next state: next x,y values, etc.
        int x = checkNext[0], y = checkNext[1], cost = checkNext[2];
        char ch = (char)checkNext[3];
        // If the node is unvisited:
        if (notInClosedList(x, y, n.isSupplied()) && !inOpenList(x, y, n.isSupplied())){
            cost += n.getCost();
            Node next = new Node(x, y, cost, ch, dir, n);
            if (map.goal(next)) return getPath(next);
            else addToOpenList(next);
        }
        return null;
    }

    /**
     * Prints open list - iterates over queue.
     */
    @Override
    protected void printOpenList(){
        System.out.print(queue.size());
        for (Node n : queue){
            System.out.print("  " + n.toString());
        }
        System.out.println();
    }

    /**
     * Adds node to open list (queue and hashMap).
     *
     * @param n Node to add.
     */
    @Override
    protected void addToOpenList(Node n){
        queue.add(n);
        openList.put(n.ID(), n);
        maxSizeOfOpenList = Math.max(maxSizeOfOpenList, openList.size());
    }

    /**
     * Removes and returns node from head of queue, and removes from open list.
     *
     * @return The first node in queue.
     */
    @Override
    protected Node removeHeadFromOpenList(){
        Node n = queue.poll();
        if (n == null) return null;
        openList.remove(n.ID());
        return n;
    }
}
