import java.util.HashMap;

public abstract class BreadthFirstSearchAlgo extends SearchAlgo {

    protected HashMap<String, Node> closedList = new HashMap<>();  // C, the closed list (explored set)

    /**
     * Remove and return head node from open list.
     *
     * @return Head node from open list.
     */
    protected abstract Node removeHeadFromOpenList();

    /**
     * Expand node in given direction.
     *
     * @param n Node to expand.
     * @param dir Direction to expand in.
     * @return Path to goal, if found. Else, null.
     */
    protected String expandTo(Node n, int[] dir){
        int[] checkNext = map.checkMove(n, dir);
        if (checkNext == null) return null;
        int x = checkNext[0], y = checkNext[1], cost = checkNext[2];
        char ch = (char)checkNext[3];
        if (notInClosedList(x, y, n.isSupplied()) && !inOpenList(x, y, n.isSupplied())){
            cost += n.getCost();
            Node next = new Node(x, y, cost, map.f(x, y, cost), ch, dir, n);
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
