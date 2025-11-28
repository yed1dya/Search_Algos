import java.util.*;

public class BFS implements SearchAlgo{

    private ArrayDeque<Node> nextNodes = new ArrayDeque<>();  // L, the queue (frontier)
    private HashSet<String> openList = new HashSet<>();  // L, the open list (frontier)
    private HashSet<String> closedList = new HashSet<>();  // C, the closed list (explored set)
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
    private String findPath() {
        addToOpenList(start);  // add start node to queue.
        while (!nextNodes.isEmpty()){  // while queue is not empty.
            if (withOpen) printOpenList();
            Node current = removeFromOpenList();  // remove head node from queue, denote it 'current'.
            if (current == null) return "no path";
            addToClosedList(current);  // add 'current' to closed list.
            if (withOpen) {
                System.out.println("expanding (" + current.ID() + "), got here by moving: " + current.getDir());
            }
            if (clockwise){
                for (String dir : Ex1.clockwiseOrder){  // for every legal neighbor of 'current':
                    String path = expandTo(current, dir);  // attempt to expand (further checks will happen).
                    if (path != null) return path;  // if a path to goal was found, return it.
                }
            }
            else{
                for (String dir : Ex1.counterClockwiseOrder){
                    String path = expandTo(current, dir);
                    if (path != null) return path;
                }
            }
        }
        return "no path";
    }

    private String expandTo(Node n, String dir){
        Node next = map.moveToDir(dir, n, this);  // attempt to create neighbor (further checks will happen).
        if (next == null) return null;
        if (map.goal(next)) return foundGoal(next);
        else addToOpenList(next);
        return null;
    }

    @Override
    public boolean inClosedList(int x, int y){
        return closedList.contains(x + "," + y);
    }

    @Override
    public boolean inOpenList(int x, int y){
        return openList.contains(x + "," + y);
    }

    private String foundGoal(Node n){
        cost = n.getCost();
        return n.getPath();
    }

    @Override
    public String output(){
        StringBuilder output = new StringBuilder();
        long startTime = 0;
        if (withTime) startTime = System.nanoTime();
        String path = findPath();
        long endTime = System.nanoTime();
        output.append(path).append('\n');
        output.append("Num: ").append(Node.numberOfNodesCreated()).append('\n');
        output.append("Max space: ").append(maxSizeOfOpenList).append('\n');
        output.append("Cost: ");
        if (path.equals("no path")) output.append("inf");
        else output.append(cost);
        if (withTime){
            double durationSeconds = (double) (endTime - startTime) / 1_000_000_000.0;
            String formattedTime = String.format("%.3f", durationSeconds);
            output.append('\n').append(formattedTime).append(" seconds");
        }
        return output.toString();
    }

    private void printOpenList(){
        for (Node n : nextNodes){
            System.out.print("[" + n.ID() + " | " + map.charAt(n.x(), n.y())
                    + " | cost: " + n.getCost() + " | " + n.getParent() + "]  ");
        }
        System.out.println();
    }

    private void addToClosedList(Node n){
        closedList.add(n.ID());
    }

    private void addToOpenList(Node n){
        nextNodes.add(n);
        openList.add(n.ID());
        maxSizeOfOpenList = Math.max(maxSizeOfOpenList, openList.size());
    }

    private Node removeFromOpenList(){
        Node n = nextNodes.poll();
        if (n == null) return null;
        openList.remove(n.ID());
        return n;
    }
}
