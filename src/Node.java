import java.util.ArrayList;

public class Node {

    private int x;
    private int y;
    private int cost;
    private Node parent;
    private String ID, dir;
    private static int countCreatedNodes = 0;

    public Node(int x, int y, int cost, String dir, Node parent){
        this.x = x; this.y = y; this.parent = parent; this.cost = cost;
        this.ID = x + "," + y; this.dir = dir;
        countCreatedNodes++;
        System.out.println(countCreatedNodes + "   " + dir + " (" + this.ID + ")");
    }

    public String ID(){
        return this.ID;
    }

    public int x(){
        return this.x;
    }

    public int y(){
        return this.y;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDir(){
        return this.dir;
    }

    public String getPath(){
        StringBuilder path = new StringBuilder();
        Node n = this;
        if (this.parent != null) path.append(this.dir);
        while (n.parent != null){
            n = n.parent;
            path.insert(0, n.dir + "-");
        }
        return path.substring(1);
    }

    public static int numberOfNodesCreated(){
        return countCreatedNodes;
    }

    @Override
    public String toString(){
        return this.ID;
    }

}
