import java.util.HashMap;

public abstract class BreadthFirstSearchAlgo extends SearchAlgo {

    /**
     * The closed list (explored set), a HashMap.
     */
    protected HashMap<String, Node> closedList = new HashMap<>();

    /**
     * Remove and return head node from open list.
     *
     * @return Head node from open list.
     */
    protected abstract Node removeHeadFromOpenList();

    /**
     * Add node to closed list (the hashMap).
     *
     * @param n Node to add.
     */
    protected void addToClosedList(Node n){
        closedList.put(n.ID(), n);
    }

    /**
     * Check if location was already visited.
     *
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @param supplied whether node is after getting supplies.
     * @return True iff location (x,y) is NOT in the closed list.
     */
    protected boolean notInClosedList(int x, int y, boolean supplied){
        return !closedList.containsKey(x + "," + y + "," + supplied);
    }
}
