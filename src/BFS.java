import java.util.*;

public class BFS{

    private ArrayDeque<Node> nextNodes = new ArrayDeque<>();  // L, the queue (frontier)
    private HashMap<String, Node> openList = new HashMap();  // L, the open list (frontier)
    private HashMap<String, Node> closedList = new HashMap<>();  // C, the closed list (explored set)
    private boolean clockwise;
    private boolean withTime;
    private boolean withOpen;
    private Map map;
    private Node start;
    private static int maxSizeOfOpenList = 0, cost = 0;

    /**
     * Constructor.
     *
     * @param clockwise order in which to create the next-step nodes.
     * @param withTime  whether to print runtime or not.
     * @param withOpen  whether to print current open list at every step.
     * @param map       the board to search.
     * @param start     start node.
     */
    public BFS(boolean clockwise, boolean withTime, boolean withOpen,
               Map map, Node start){
        this.clockwise = clockwise;
        this.withTime = withTime; this.withOpen = withOpen;
        this.map = map; this.start = start;
    }

    /**
     * Runs BFS to find an optimal path from start to goal.
     * Uses a queue to store created nodes (FIFO).
     *
     * @return A string to write to output.txt (see assignment instruction).
     */
    public String findPath() {
        addToOpenList(start);  // add start node to queue
        while (!nextNodes.isEmpty()){  // while queue is not empty
            if (withOpen) printOpenList();
            Node current = removeFromOpenList();  // remove head node from queue, denote it 'current'
            closedList.put(current.ID(), current);  // add 'current' tp closed list
            Node next;
            if (clockwise){
                for (String dir : Ex1.clockwiseOrder){  // for every legal neighbor of 'current':
                    next = map.moveToDir(dir, current);  // create neighbor
                    if (next == null) continue;
                    // check that neighbor isn't in the open or closed list
                    if (!openList.containsKey(next.ID()) && !closedList.containsKey(next.ID())){
                        if (map.goal(next)){
                            cost = next.getCost();
                            return next.getPath();
                        }
                        else addToOpenList(next);
                    }
                }
            }
            else{
                for (String dir : Ex1.counterClockwiseOrder){
                    next = map.moveToDir(dir, current);
                    if (next == null) continue;
                    if (!openList.containsKey(next.ID()) && !closedList.containsKey(next.ID())){
                        if (map.goal(next)){
                            cost = next.getCost();
                            return next.getPath();
                        }
                        else addToOpenList(next);
                    }
                }
            }
        }
        return null;
    }

    public String output(){
        StringBuilder output = new StringBuilder();
        long startTime = 0;
        if (withTime) startTime = System.nanoTime();
        String path = findPath();
        long endTime = System.nanoTime();
        output.append(path).append('\n');
        output.append("Num: ").append(Node.numberOfNodesCreated()).append('\n');
        output.append("Max space: ").append(maxSizeOfOpenList).append('\n');
        output.append("Cost: ").append(cost);
        if (withTime){
            double durationSeconds = (double) (endTime - startTime) / 1_000_000_000.0;
            String formattedTime = String.format("%.3f", durationSeconds);
            output.append('\n').append(formattedTime).append(" seconds");
        }
        return output.toString();
    }

    private void printOpenList(){
        for (Node n : nextNodes){
            System.out.print("[" + n.ID() + " | " + map.charAt(n.x(), n.y()) + " | cost: " + n.getCost() + "]");
        }
        System.out.println();
    }

    private void addToOpenList(Node n){
        nextNodes.add(n);
        openList.put(n.ID(), n);
        maxSizeOfOpenList = Math.max(maxSizeOfOpenList, openList.size());
    }

    private Node removeFromOpenList(){
        Node n = nextNodes.poll();
        if (n == null) return null;
        openList.remove(n);
        return n;
    }
}
