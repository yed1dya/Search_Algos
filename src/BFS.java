import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class BFS implements SearchAlgo{

    private Queue<Node> nextNodes = new LinkedList<>();  // L, the queue (frontier)
    private HashMap<String, Node> openList = new HashMap<>();  // L, the open list (frontier)
    // explored set
    private HashMap<String, Node> closedList = new HashMap<>();  // C, the closed list (explored set)

    /**
     * Runs BFS to find an optimal path from start to goal.
     * Uses a queue to store created nodes (FIFO)
     *
     * @param clockwise order in which to create the next-step nodes.
     * @param oldFirst  if two nodes have the same heuristic value, which one to check first.
     * @param withTime  whether to print runtime or not.
     * @param withOpen  whether to print current open list at every step.
     * @param rows      height of board.
     * @param cols      width of board.
     * @param map     the board to search.
     * @param start     start node.
     * @param goalX     x-coordinate of the goal.
     * @param goalY     y-coordinate of the goal.
     */
    @Override
    public String run(boolean clockwise, boolean oldFirst, boolean withTime, boolean withOpen,
                    int rows, int cols, Map map, Node start, int goalX, int goalY) {
        int serialNum = 0;

        addToFrontier(start);
        while (!nextNodes.isEmpty()){
            Node current = removeFromFrontier();
            ArrayList<Node> adjacent = map.adjacents(current, clockwise);
        }

        return null;
    }

    private void addToFrontier(Node n){
        nextNodes.add(n);
        openList.put(n.ID(), n);
    }

    private Node removeFromFrontier(){
        Node n = nextNodes.poll();
        if (n == null) return null;
        openList.remove(n.ID());
        return n;
    }
}
