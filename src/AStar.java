import java.util.PriorityQueue;

public class AStar extends BreadthFirstSearchAlgo {

    /**
     * The open list (frontier), a priority queue.
     * Sorted by f(n) first, then by old-first or new-first.
     */
    private PriorityQueue<Node> priorityQueue = new PriorityQueue<>(nodeCompare);

    /**
     * Constructor.
     *
     * @param clockwise order in which to create the next-step nodes.
     * @param withTime  whether to print runtime or not.
     * @param withOpen  whether to print current open list at every step.
     * @param oldFirst  which node to prefer in case of equal f-value.
     * @param map       the board to search.
     * @param start     start node.
     */
    protected AStar(boolean clockwise, boolean withTime, boolean withOpen,
                    boolean oldFirst, Map map, Node start){
        this.clockwise = clockwise; this.withTime = withTime; this.withOpen = withOpen;
        this.oldFirst = oldFirst; this.map = map; this.start = start;
    }

    /**
     * Runs A* to find an optimal path from start to goal.
     * Uses a priority queue to store created nodes.
     * Ordered by f(n) and then old-first or new-first, ascending.
     *
     * @return A string representing the path, or "no path" if no path exists.
     */
    @Override
    protected String findPath() {
        addToOpenList(start);
        while (!priorityQueue.isEmpty()){
            if (withOpen) printOpenList();  // Option for debugging.
            Node current = removeHeadFromOpenList();
            if (current == null) return "no path";  // Safeguard.
            if (map.goal(current)) {
                pathCost = current.getCost();  // The cost of the path is the cost of reaching the current node.
                return getPath(current);  // Return the path to the node
            }
            addToClosedList(current);
            if (clockwise) {  // Options for CW or CCW order of expansion:
                // Check the option of moving in each direction:
                for (int[] dir : Ex1.clockwiseOrder) checkDir(current, dir);
            } else {
                for (int[] dir : Ex1.counterClockwiseOrder) checkDir(current, dir);
            }
        }
        return "no path";
    }

    /**
     * Helper method, preforms the check and move in given direction from current node.
     * If next node is already in the closed list, skip it.
     * Else, if next already appears in open list (call this one "oldNext"),
     * then compare cost to newNext and keep the cheaper one.
     *
     * @param current Current node.
     * @param dir Direction to move in.
     */
    protected void checkDir(Node current, int[] dir){
        // Check validity of the move and get the results after the move:
        int[] checkNext = map.checkMove(current, dir);
        if (checkNext == null) return;  // If the move is illegal.
        // Parse the next state: next x,y values, etc.
        int nx = checkNext[0], ny = checkNext[1], cost = checkNext[2] + current.getCost();
        char ch = (char)checkNext[3];
        boolean supplied = checkNext[4] == 1;
        if (notInClosedList(nx, ny, supplied) && !inOpenList(nx, ny, supplied)){
            Node next = new Node(nx, ny, cost, ch, dir, current);
            addToOpenList(next);
        }
        else if (inOpenList(nx, ny, supplied)){
            Node oldNext = getFromOpenList(nx, ny, supplied);
            if (map.f(nx, ny, cost) < map.f(oldNext)){
                /*
                 * No need to create a new node: just update the attributes.
                 * We do need to remove and add the node, so that the PQ gets re-sorted.
                 */
                priorityQueue.remove(oldNext);
                oldNext.setParent(current);
                oldNext.setCost(cost);
                oldNext.setDir(dir);
                oldNext.setSupplied(supplied);
                priorityQueue.add(oldNext);
            }
        }
    }

    /**
     * Adds node to open list (priority queue and hashMap).
     * Updates maxSizeOfOpenList.
     *
     * @param n Node to add.
     */
    @Override
    protected void addToOpenList(Node n) {
        priorityQueue.add(n);
        openList.put(n.ID(), n);
        maxSizeOfOpenList = Math.max(maxSizeOfOpenList, openList.size());
    }

    /**
     * Removes and returns node from head of priority queue,
     * and removes it from the open list.
     *
     * @return The first node in the priority queue.
     */
    @Override
    protected Node removeHeadFromOpenList() {
        Node n = priorityQueue.poll();
        if (n == null) return null;
        openList.remove(n.ID());
        return n;
    }

    /**
     * Get a node from the open list, by x,y coordinates.
     *
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @return The requested node.
     */
    protected Node getFromOpenList(int x, int y, boolean supplied){
        return openList.get(x + "," + y + "," + supplied);
    }

    /**
     * Prints open list - iterates over the internal array of the priority queue.
     * Not sorted, but minimum Node is first.
     */
    @Override
    protected void printOpenList() {
        PriorityQueue<Node> tempPQ = new PriorityQueue<>(priorityQueue);
        System.out.print(tempPQ.size());
        while (!tempPQ.isEmpty()) {
            System.out.print("  " + tempPQ.poll().toString());
        }
        System.out.println();
    }
}
