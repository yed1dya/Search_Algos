import java.util.ArrayList;
import java.util.Collections;

public class DFBnB extends IterativeDepthFirstSearchAlgo{

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
    protected DFBnB(boolean clockwise, boolean withTime, boolean withOpen, boolean oldFirst,
                    Map map, InOutNode start){
        this.clockwise = clockwise; this.withTime = withTime; this.withOpen = withOpen;
        this.oldFirst = oldFirst; this.map = map; this.start = start;
        this.maxF = maxF(map);
    }

    /**
     * Run DFBnB to find an optimal path from start to goal.
     * Using a stack, runs DFS with increasingly shorter cutoffs.
     *
     * @return A string representing the path, or "no path" if no path exists.
     */
    @Override
    protected String findPath() {
        String result = "no path";  // Initially, we assume no path
        int t = maxF;  // Set initial cutoff to max.
        addToOpenList(start);
        while (!stack.empty()){
            if (withOpen) printOpenList();  // Option for debugging.
            InOutNode current = stack.pop();
            /*
            * If we popped a node that is already marked 'out',
            * it means we already expanded it, and pruned all of its branches.
            */
            if (current.isOut()) openList.remove(current.ID());
            else{  // When we expand a node, mark it 'out':
                current.setOut();
                stack.push(current);
                /*
                 * Get the list of legal neighbors, sorted by f-value and creation time.
                 * Iterate over it; when we reach one that has a higher f-value than the current cutoff,
                 * we want to remove that one and all the ones after it (because they all have a higher f-value).
                 */
                ArrayList<InOutNode> neighbors = neighbors(current);
                int removeFromIndex = -1;
                for (int i = 0; i < neighbors.size(); i++) {
                    InOutNode next = neighbors.get(i);
                    if (map.f(next) >= t) {
                        removeFromIndex = i;
                        break;
                    }
                    /*
                     * If a node has a good f-value, but already in the open list:
                     * We compare the f-value of the new node vs the copy that already exists,
                     * and keep the cheaper one.
                     */
                    else if (openList.containsKey(next.ID())){
                        InOutNode oldNext = (InOutNode) openList.get(next.ID());
                        if (oldNext.isOut()) neighbors.remove(i--);
                        else if (map.f(oldNext) <= map.f(next)) {
                            neighbors.remove(i--);
                        }
                        else {
                            openList.remove(oldNext.ID());
                            stack.remove(oldNext);
                        }
                    }
                    /*
                     * If the node isn't already in the open list, check if it's the goal.
                     * If yes, update the result and remove the neighbors after it.
                     */
                    else if (map.goal(next)) {
                        t = map.f(next);
                        result = getPath(next);
                        removeFromIndex = i;
                        break;
                    }
                }
                // Remove irrelevant neighbors, then reverse the list and insert into the stack:
                if (removeFromIndex != -1) {
                    neighbors.subList(removeFromIndex, neighbors.size()).clear();
                }
                Collections.reverse(neighbors);
                for (InOutNode node : neighbors){
                    addToOpenList(node);
                }
            }
        }
        return result;
    }

    /**
     * Create list of neighbor states, sorted by f-value and "old-first" or "new-first".
     *
     * @param n Current node.
     * @return A sorted list of the relevant neighbors.
     */
    protected ArrayList<InOutNode> neighbors(InOutNode n){
        ArrayList<InOutNode> neighbors = new ArrayList<>();
        int[][] directions = clockwise ? Ex1.clockwiseOrder : Ex1.counterClockwiseOrder;
        for (int[] dir : directions){
            InOutNode next = (InOutNode) map.move(n, dir);
            if (next != null) {  // If the move is legal:
                neighbors.add(next);
            }
        }
        neighbors.sort(nodeCompare);
        return neighbors;
    }

    /**
     * Adds node to open list (stack and hashMap).
     * Updates maxSizeOfOpenList.
     *
     * @param n Node to add.
     */
    @Override
    protected void addToOpenList(Node n) {
        stack.push((InOutNode) n);
        openList.put(n.ID(), n);
        maxSizeOfOpenList = Math.max(maxSizeOfOpenList, openList.size());
    }

    /**
     * Prints open list - iterates over the stack.
     */
    @Override
    protected void printOpenList(){
        System.out.print(stack.size());
        for (InOutNode n : stack){
            System.out.print("  " + n.toString(map));
        }
        System.out.println();
    }
}
