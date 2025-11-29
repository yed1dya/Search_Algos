import java.util.*;

public class BFS extends SearchAlgo{

    private ArrayDeque<Node> queue = new ArrayDeque<>();  // the open list (frontier), a queue

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
     * @return A string to write to output.txt (as per assignment instruction).
     */
    @Override
    protected String findPath() {
        addToOpenList(start);
        while (!queue.isEmpty()) {
            if (withOpen) printOpenList();
            Node current = removeHeadFromOpenList();
            if (current == null) return "no path";
            addToClosedList(current);
            if (clockwise) {
                for (int[] dir : Ex1.clockwiseOrder) {
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
