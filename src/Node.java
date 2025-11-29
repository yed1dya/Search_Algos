public class Node {

    private int x;
    private int y;
    private int cost;
    private int serialNumber;
    private Node parent;
    private String ID, dir;
    private char c;
    private static int countCreatedNodes = 0;

    /**
     * Constructor (if the char at the node is known).
     *
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @param cost The cost of reaching the node (from start).
     * @param c The char in the map at location x,y.
     * @param dir The direction of movement that produced the node.
     * @param parent The previous node.
     */
    protected Node(int x, int y, int cost, char c, String dir, Node parent){
        this.x = x; this.y = y; this.parent = parent; this.cost = cost;
        this.ID = x + "," + y; this.dir = dir; this.c = c;
        countCreatedNodes++;
        this.serialNumber = countCreatedNodes;
    }

    /**
     * Constructor (if the char at the node is unknown).
     * Gets the char from the map.
     *
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @param cost The cost of reaching the node (from start).
     * @param dir The direction of movement that produced the node.
     * @param parent The previous node.
     * @param map The map that the node is on.
     */
    protected Node (int x, int y, int cost, String dir, Node parent, Map map){
        this(x, y, cost, map.charAt(x, y), dir, parent);
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
    }

    protected int getCost() {
        return cost;
    }

    protected void setCost(int cost) {
        this.cost = cost;
    }

    protected String getDir(){
        return this.dir;
    }

    protected void setDir(String dir){
        this.dir = dir;
    }

    protected String getPath(){
        StringBuilder path = new StringBuilder();
        Node n = this;
        if (this.parent != null) path.append(this.dir);
        while (n.parent != null){
            n = n.parent;
            path.insert(0, n.dir + "-");
        }
        return path.substring(1);
    }

    protected static int numberOfNodesCreated(){
        return countCreatedNodes;
    }

    protected int getSerialNumber(){
        return this.serialNumber;
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder("[" + this.ID + " | " + this.c + " | " + this.cost + " | ");
        if (parent != null) s.append(parent.ID);
        else s.append("null");
        s.append("]");
        return s.toString();
    }

}
