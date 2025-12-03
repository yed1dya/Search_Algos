public class IDAStar extends IterativeDepthFirstSearchAlgo {

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
        int previousT = -1;
        int t = map.heuristic(start.x(), start.y());  // Equal to f(start), because cost(start) == 0.
        while (t <= maxF && t != previousT){
            previousT = t;
            int minF = maxF;
            start.reset();      // Reset start - not supplied, no direction, no parent.
            addToOpenList(start);
            while (!stack.empty()){
                if (withOpen) printOpenList();
                Node current = stack.pop();
                if (isOut(current)) openList.remove(current.ID());
                else {
                    setOut(current);
                    stack.push(current);
                    int[][] directions = clockwise ? Ex1.clockwiseOrder : Ex1.counterClockwiseOrder;
                    for (int[] dir : directions) {
                        Node next = map.move(current, dir);
                        if (next == null) continue;
                        int nextF = map.f(next);
                        if (nextF > t) minF = Math.min(minF, nextF);
                        else {
                            String nextID = next.ID();
                            if (openList.containsKey(nextID)) {
                                Node oldNext = openList.get(nextID);
                                if (!isOut(oldNext) && map.f(oldNext) > nextF){
                                    openList.remove(nextID);
                                    stack.remove(oldNext);
                                }
                            }
                            if (map.goal(next)) return getPath(next);
                            addToOpenList(next);
                        }
                    }
                }
            }
            t = minF;
            System.out.println(t);
        }
        return "no path";
    }

    @Override
    protected void addToOpenList(Node n) {
        stack.push(n);
        openList.put(n.ID(), n);
        maxSizeOfOpenList = Math.max(maxSizeOfOpenList, openList.size());
    }

    @Override
    protected void printOpenList(){
        System.out.print(stack.size());
        for (Node n : stack){
            System.out.print("  " + n.toString(map) + isOut(n));
        }
        System.out.println();
    }
}
