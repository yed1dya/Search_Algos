import java.util.Arrays;

public class Node {

    private int x;
    private int y;
    private int cost;
    private int f;
    private int serialNumber;
    private int[] dir;
    private Node parent;
    private String ID;
    private char ch;
    private boolean supplied;
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
    protected Node(int x, int y, int cost, int f, char c, int[] dir, Node parent){
        this.x = x; this.y = y; this.parent = parent; this.cost = cost;
        this.ID = x + "," + y; this.dir = dir; this.ch = c; this.f = f;
        this.supplied = (c == '*' || (parent != null && parent.supplied));
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

    protected String getPath(){
        StringBuilder path = new StringBuilder();
        Node n = this;
        while (n.parent != null){
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
        StringBuilder s = new StringBuilder("[" + this.ID + " | " + this.ch + " | " + this.cost +  " | " + this.f + " | ");
        if (supplied) s.append("sup | ");
        else s.append("not | ");
        if (parent != null) s.append(parent.ID);
        else s.append("null");
        s.append("]");
        return s.toString();
    }

}
