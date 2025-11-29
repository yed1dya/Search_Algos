import java.util.HashMap;

public abstract class SearchAlgo {

    protected HashMap<String, Node> openList = new HashMap<>();  // the open list (frontier)
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
     * Print the open list to console (the "with-open" option).
     */
    protected abstract void printOpenList();

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
     * Check if location is in the open list.
     *
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @param supplied whether node is after getting supplies.
     * @return True iff location (x,y) is in the open list.
     */
    protected boolean inOpenList(int x, int y, boolean supplied){
        return openList.containsKey(x + "," + y + "," + supplied);
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
