public class IDAStar extends IterativeDepthFirstSearchAlgo {

    private int t, minF;

    /**
     * Constructor.
     *
     * @param clockwise order in which to create the next-step nodes.
     * @param withTime  whether to print runtime or not.
     * @param withOpen  whether to print current open list at every step.
     * @param map       the board to search.
     * @param start     start node.
     */
    protected IDAStar(boolean clockwise, boolean withTime, boolean withOpen, Map map, Node start) {
        this.clockwise = clockwise; this.withTime = withTime; this.withOpen = withOpen;
        this.map = map; this.start = start;
        this.maxF = maxF(map);
    }

    /**
     * Runs IDA* to find an optimal path from start to goal.
     * Outer loop is the driver,
     * runs while optimistic guess for path cost is less than the max possible path cost.
     * Inner loop is the actual algorithm engine, runs DFS-like (stack based) bounded search,
     * directed by the f-value.
     *
     * @return A string representing the path, or "no path" if no path exists.
     */
    @Override
    protected String findPath() {
        t = map.heuristic(start.x(), start.y());  // Equal to f(start), because cost(start) == 0.
        while (t < maxF){
            minF = maxF;
            start = new InOutNode(start);
            addToOpenList(start);
            while (!stack.empty()){
                if (withOpen) printOpenList();
                InOutNode current = stack.pop();
                if (current.isOut()) openList.remove(current.ID());
                else {
                    current.setOut();
                    stack.push(current);
                    if (clockwise) {
                        for (int[] dir : Ex1.clockwiseOrder) {
                            String path = checkDir(current, dir);
                            if (path != null) return path;
                        }
                    } else {
                        for (int[] dir : Ex1.counterClockwiseOrder) {
                            String path = checkDir(current, dir);
                            if (path != null) return path;
                        }
                    }
                }
            }
            t = minF;
        }
        return "no path";
    }

    protected String checkDir(InOutNode current, int[] dir){
        int[] checkNext = map.checkMove(current, dir);
        if (checkNext == null) return null;
        int x = checkNext[0], y = checkNext[1], cost = checkNext[2] + current.getCost();
        boolean supplied = checkNext[4] == 1;
        int nextF = map.f(x, y, cost);
        if (nextF > t) {
            minF = Math.min(minF, nextF);
            return null;
        }
        String key = x + "," + y + "," + supplied;
        if (openList.containsKey(key)){
            InOutNode oldNext = ((InOutNode) openList.get(key));
            if (!oldNext.isOut() && map.f(oldNext) > nextF){
                openList.remove(key);
                stack.remove(oldNext);
            }
            else return null;
        }
        InOutNode next = new InOutNode(x, y, cost, (char) checkNext[3], dir, current);
        if (map.goal(next)) return getPath(next);
        addToOpenList(next);
        return null;
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
        for (Node n : stack){
            System.out.print("  " + n.toString());
        }
        System.out.println();
    }
}
