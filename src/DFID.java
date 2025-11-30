import java.util.HashMap;

public class DFID extends SearchAlgo {

    private int maxDepth;

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
        int height = map.height(), width = map.width();
        maxDepth = height * width;
    }

    @Override
    protected String findPath() {
        for (int depth = 1; depth < maxDepth; depth++) {
            openList = new HashMap<>();
            String result = limitedDFS(start, depth);
            if (!result.equals("cutoff")) return result;
        }
        return "no path";
    }

    private String limitedDFS(Node current, int limit) {
        if (map.goal(current)) {
            pathCost = current.getCost();
            return current.getPath();
        }
        if (limit == 0) return "cutoff";
        addToOpenList(current);
        boolean cutoff = false;
        if (clockwise) {
            for (int[] dir : Ex1.clockwiseOrder) {
                Node next = checkDir(current, dir);
                if (next != null) {
                    String result = limitedDFS(next, limit - 1);
                    if (result.equals("cutoff")) cutoff = true;
                    else if (!result.equals("fail")) return result;
                }
            }
        } else {
            for (int[] dir : Ex1.counterClockwiseOrder) {
                Node next = checkDir(current, dir);
                if (next != null) {
                    String result = limitedDFS(next, limit - 1);
                    if (result.equals("cutoff")) cutoff = true;
                    else if (!result.equals("fail")) return result;
                }
            }
        }
        if (withOpen) printOpenList();
        removeFromOpenList(current);
        return cutoff ? "cutoff" : "fail";
    }

    @Override
    protected void addToOpenList(Node n) {
        openList.put(n.ID(), n);
        maxSizeOfOpenList = Math.max(maxSizeOfOpenList, openList.size());
    }

    protected void removeFromOpenList(Node n) {
        openList.remove(n.ID());
    }

    @Override
    protected void printOpenList() {
        System.out.print(openList.size());
        for (Node n : openList.values()){
            System.out.print("  " + n.toString());
        }
        System.out.println();
    }
}
