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
            int[][] directions = clockwise ? Ex1.clockwiseOrder : Ex1.counterClockwiseOrder;
            for (int[] dir : directions) {
                Node next = map.move(current, dir);
                if (next == null) continue;
                if (notInClosedList(next) && !inOpenList(next)){
                    addToOpenList(next);
                }
                else if (inOpenList(next)){
                    Node oldNext = getFromOpenList(next.ID());
                    if (map.f(next) < map.f(oldNext)){
                        priorityQueue.remove(oldNext);
                        priorityQueue.add(next);
                    }
                }
            }
        }
        return "no path";
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
     * Get a node from the open list, by node ID.
     *
     * @return The requested node.
     */
    protected Node getFromOpenList(String ID){
        return openList.get(ID);
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
            System.out.print("  " + tempPQ.poll().toString(map));
        }
        System.out.println();
    }
}
