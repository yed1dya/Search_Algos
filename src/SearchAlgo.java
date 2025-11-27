public interface SearchAlgo {

    /**
     * Runs the search algorithm to find an optimal path from start to goal.
     *
     * @param clockwise order in which to create the next-step nodes.
     * @param oldFirst  if two nodes have the same heuristic value, which one to check first.
     * @param withTime  whether to print runtime or not.
     * @param withOpen  whether to print current open list at every step.
     * @param rows      height of board.
     * @param cols      width of board.
     * @param map     the map to search.
     * @param start     start node.
     * @param goalX     x-coordinate of the goal.
     * @param goalY     y-coordinate of the goal.
     */
    public String run(boolean clockwise, boolean oldFirst, boolean withTime, boolean withOpen,
                    int rows, int cols, Map map, Node start, int goalX, int goalY);
}
