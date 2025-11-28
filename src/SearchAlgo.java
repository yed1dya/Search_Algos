public interface SearchAlgo {
    /**
     * Get the output from running the search algo.
     * (runs the algorithm).
     * @return Output as per assignment instructions.
     */
    public String output();

    /**
     * Check if location was already visited.
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @return True iff location (x,y) is in the closed list.
     */
    public boolean inClosedList(int x, int y);

    /**
     * Check if location is in the open list.
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @return True iff location (x,y) is in the open list.
     */
    public boolean inOpenList(int x, int y);
}
