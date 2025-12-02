public class Node {

    protected int x, y, cost, serialNumber;
    protected int[] dir;
    protected Node parent;
    protected String ID;
    protected char ch;
    protected boolean supplied;
    private static int countCreatedNodes = 0;

    /**
     * Constructor.
     *
     * @param x      x-coordinate.
     * @param y      y-coordinate.
     * @param cost   The cost of reaching the node (from start).
     * @param c      The char in the map at location x,y.
     * @param dir    The direction of movement that produced the node.
     * @param parent The previous node.
     */
    protected Node(int x, int y, int cost, char c, int[] dir, Node parent){
        this.x = x; this.y = y; this.parent = parent; this.cost = cost;
        this.dir = dir; this.ch = c;
        this.supplied = (c == '*' || (parent != null && parent.supplied));
        // Each state is defined by: x,y coordinates, and whether the robot is supplied at that point.
        this.ID = x + "," + y + "," + this.supplied;
        countCreatedNodes++;
        this.serialNumber = countCreatedNodes;
    }

    protected String ID(){
        return this.ID;
    }

    protected int x(){
        return this.x;
    }

    protected int y(){
        return this.y;
    }

    protected void setParent(Node parent) {
        this.parent = parent;
        this.supplied = parent.supplied;
    }

    protected int getCost() {
        return cost;
    }

    protected void setCost(int cost) {
        this.cost = cost;
    }

    protected int[] getDir(){
        return this.dir;
    }

    protected void setDir(int[] dir){
        this.dir = dir;
    }

    protected boolean isSupplied(){
        return this.supplied;
    }

    protected void setSupplied(boolean supplied){
        this.supplied = supplied;
    }

    /**
     * Iterate backwards by parents to build the path.
     *
     * @return A String representing the path to the node, or an empty string if there is no path.
     */
    protected String getPath(){
        StringBuilder path = new StringBuilder();
        Node n = this;
        while (n.parent != null){
            System.out.print(n.ch + " ");
            path.insert(0,"-" + dirName(n.dir));
            n = n.parent;
        }
        return !path.isEmpty() ? path.substring(1) : "";
    }

    protected static int numberOfNodesCreated(){
        return countCreatedNodes;
    }

    protected int getSerialNumber(){
        return this.serialNumber;
    }

    /**
     * Provides direction name for path printing.
     *
     * @param dir Query direction.
     * @return The direction's name.
     */
    private String dirName(int[] dir){
        if (dir.length == 0) return "Ent";
        StringBuilder sb = new StringBuilder();
        if (dir[0] == 1) sb.append("R");
        if (dir[0] == -1) sb.append("L");
        if (dir[1] == 1) sb.append("U");
        if (dir[1] == -1) sb.append("D");
        return sb.toString();
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder("[" + this.x + "," + this.y);
        s.append(" | ").append(this.ch);
        s.append(" | ").append(this.cost);
        if (supplied) s.append(" | sup | ");
        else s.append(" | not | ");
        if (parent != null){
            s.append(parent.x).append(",").append(parent.y).append(", ");
            if (parent.supplied) s.append("sup");
            else s.append("not");
        }
        else s.append("null");
        s.append("]");
        return s.toString();
    }

}
