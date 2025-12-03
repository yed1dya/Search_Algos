import java.util.HashMap;

public class DFID extends SearchAlgo {

    /**
     * Constructor.
     *
     * @param clockwise order in which to create the next-step nodes.
     * @param withTime  whether to print runtime or not.
     * @param withOpen  whether to print current open list at every step.
     * @param map       the board to search.
     * @param start     start node.
     */
    protected DFID(boolean clockwise, boolean withTime, boolean withOpen, Map map, Node start){
        this.clockwise = clockwise; this.withTime = withTime; this.withOpen = withOpen;
        this.map = map; this.start = start;
    }

    /**
     * Driver loop of the algorithm.
     * Calls the actual DFS method.
     *
     * @return A string representing the path, or "no path" if no path exists.
     */
    @Override
    protected String findPath() {
        for (int limit = 1; limit < Integer.MAX_VALUE; limit++) {
            System.out.println("limit: " + limit);
            openList = new HashMap<>();  // Reset the open list between rounds of DFS.
            String result = limitedDFS(start, limit);  // Run DFS and get the result.
            // If DFS found the goal before cutoff, that's the shortest path:
            if (!result.equals("cutoff")) return result;
        }
        return "no path";  // If max depth was reached with no path returned, there is no path.
    }

    /**
     * Actual DFS method, recursive implementation.
     *
     * @param current The current node.
     * @param limit The cutoff limit.
     * @return A string representing the path, or "cutoff" if no path was found before reaching cutoff depth.
     */
    private String limitedDFS(Node current, int limit) {
        // If the current node is the goal, return the path to it and update the cost.
        if (map.goal(current)) {
            pathCost = current.getCost();
            return current.getPath();
        }
        if (limit == 0) return "cutoff";  // If limit was reached.
        // Else, expand the current node:
        addToOpenList(current);
        boolean cutoff = false;
        int[][] directions = clockwise ? Ex1.clockwiseOrder : Ex1.counterClockwiseOrder;
        for (int[] dir : directions) {
            Node next = map.move(current, dir);
            if (next != null) {
                // If next node is valid, recurse from it:
                String result = limitedDFS(next, limit - 1);
                // If the path from that node was cut off, we can ignore that option:
                if (result.equals("cutoff")) cutoff = true;
                // If the result isn't "cutoff" or "fail", it's a path:
                else if (!result.equals("fail")) return result;
            }
        }
        /*
         * If we finished the branch without finding a path, remove it.
         * This frees up the space.
         */
        openList.remove(current.ID());
        if (withOpen) printOpenList();  // Option for debugging
        /*
         * Return status: "cutoff" if we stopped because of depth cutoff,
         * or "fail" if we ran out of where to go:
         */
        return cutoff ? "cutoff" : "fail";
    }

    /**
     * Adds node to open list (hashMap).
     * Updates maxSizeOfOpenList.
     *
     * @param n Node to add.
     */
    @Override
    protected void addToOpenList(Node n) {
        openList.put(n.ID(), n);
        maxSizeOfOpenList = Math.max(maxSizeOfOpenList, openList.size());
    }

    /**
     * Prints open list.
     */
    @Override
    protected void printOpenList() {
        System.out.print(openList.size());
        for (Node n : openList.values()){
            System.out.print("  " + n.toString());
        }
        System.out.println();
    }
}
