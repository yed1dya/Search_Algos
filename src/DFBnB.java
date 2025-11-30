import java.util.ArrayList;
import java.util.Collections;

public class DFBnB extends IterativeDepthFirstSearchAlgo{

    private int maxF;

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
                    Map map, Node start){
        this.clockwise = clockwise; this.withTime = withTime; this.withOpen = withOpen;
        this.oldFirst = oldFirst; this.map = map; this.start = new InOutNode(start);
        int height = map.height(), width = map.width();
        maxF = height * width * 10;
    }

    @Override
    protected String findPath() {
        String result = "no path";
        int t = maxF;
        addToOpenList(start);
        while (!stack.empty()){
            //if (withOpen) printOpenList();
            InOutNode current = stack.pop();
            if (current.isOut()) openList.remove(current.ID());
            else{
                current.setOut();
                stack.push(current);
                ArrayList<InOutNode> neighbors = neighbors(current);
                int removeFromIndex = -1;
                for (int i = 0; i < neighbors.size(); i++) {
                    InOutNode next = neighbors.get(i);
                    if (next.f >= t) {
                        removeFromIndex = i;
                        break;
                    }
                    else if (openList.containsKey(next.ID())){
                        InOutNode oldNext = (InOutNode) openList.get(next.ID());
                        if (oldNext.isOut()) neighbors.remove(i--);
                        else if (oldNext.f <= next.f) {
                            neighbors.remove(i--);
                        }
                        else {
                            openList.remove(oldNext.ID());
                            stack.remove(oldNext);
                        }
                    }
                    else if (map.goal(next)) {
                        t = next.f;
                        result = getPath(next);
                        removeFromIndex = i;
                    }
                }
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

    protected ArrayList<InOutNode> neighbors(InOutNode n){
        ArrayList<InOutNode> neighbors = new ArrayList<>();
        if (clockwise){
            for (int[] dir : Ex1.clockwiseOrder){
                int[] checkNext = map.checkMove(n, dir);
                if (checkNext != null) {
                    int nx = checkNext[0], ny = checkNext[1], cost = checkNext[2] + n.getCost();
                    char ch = (char) checkNext[3];
                    neighbors.add(new InOutNode(nx, ny, cost, map.f(nx, ny, cost), ch, dir, n));
                }
            }
        }
        else {
            for (int[] dir : Ex1.counterClockwiseOrder){
                int[] checkNext = map.checkMove(n, dir);
                if (checkNext != null) {
                    int nx = checkNext[0], ny = checkNext[1], cost = checkNext[2] + n.getCost();
                    char ch = (char) checkNext[3];
                    neighbors.add(new InOutNode(nx, ny, cost, map.f(nx, ny, cost), ch, dir, n));
                }
            }
        }
        neighbors.sort(nodeCompare);
        return neighbors;
    }

    @Override
    protected void addToOpenList(Node n) {
        stack.push((InOutNode) n);
        openList.put(n.ID(), n);
        maxSizeOfOpenList = Math.max(maxSizeOfOpenList, openList.size());
    }

    @Override
    protected void printOpenList(){
        System.out.print(stack.size());
        for (InOutNode n : stack){
            System.out.print("  " + n.toString());
        }
        System.out.println();
    }
}
