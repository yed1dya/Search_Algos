import java.util.HashMap;

public abstract class SearchAlgo {

    protected HashMap<String, Node> openList = new HashMap<>();  // L, the open list (frontier)
    protected HashMap<String, Node> closedList = new HashMap<>();  // C, the closed list (explored set)
    protected boolean clockwise;
    protected boolean withTime;
    protected boolean withOpen;
    protected Map map;
    protected Node start;
    protected static int maxSizeOfOpenList = 0, pathCost = 0;
    protected boolean oldFirst;

    /**
     * The actual workhorse - the specific algorithm implementation.
     *
     * @return A path start --> goal, or "no path".
     */
    protected abstract String findPath();

    /**
     * Add a node to the open list.
     *
     * @param n Node to add.
     */
    protected abstract void addToOpenList(Node n);

    /**
     * Remove and return head node from open list.
     *
     * @return Head node from open list.
     */
    protected abstract Node removeHeadFromOpenList();

    /**
     * Print the open list to console (the "with-open" option).
     */
    protected abstract void printOpenList();

    /**
     * Expand node in given direction.
     *
     * @param n Node to expand.
     * @param dir Direction to expand in.
     * @return Path to goal, if found. Else, null.
     */
    protected String expandTo(Node n, String dir){
        int[] checkNext = map.checkMove(n, dir);  // check neighbor.
        if (checkNext == null) return null;
        int x = checkNext[0], y = checkNext[1], cost = checkNext[2];
        if (notInClosedList(x, y) && !inOpenList(x, y)){
            Node next = new Node(x, y, cost + n.getCost(), dir, n, map);
            if (map.goal(next)) return getPath(next);
            else addToOpenList(next);
        }
        return null;
    }

    /**
     * Add node to closed list (the hashMap).
     *
     * @param n Node to add.
     */
    protected void addToClosedList(Node n){
        closedList.put(n.ID(), n);
    }

    /**
     * Get the output from running the search algo.
     * (runs the algorithm).
     *
     * @return Output as per assignment instructions.
     */
    protected String[] output(){
        String path = findPath();
        String cost = path.equals("no path") ? "inf" : String.valueOf(pathCost);
        return new String[]{path,
                String.valueOf(Node.numberOfNodesCreated()),
                String.valueOf(maxSizeOfOpenList),
                cost};
    }

    /**
     * Check if location was already visited.
     *
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @return True iff location (x,y) is NOT in the closed list.
     */
    protected boolean notInClosedList(int x, int y){
        return !closedList.containsKey(x + "," + y);
    }

    /**
     * Check if location is in the open list.
     *
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @return True iff location (x,y) is in the open list.
     */
    protected boolean inOpenList(int x, int y){
        return openList.containsKey(x + "," + y);
    }

    /**
     * Path to a node (by parents).
     *
     * @param n Node to check.
     * @return Path to node.
     */
    protected String getPath(Node n){
        pathCost = n.getCost();
        return n.getPath();
    }
}
