import java.util.ArrayList;

public class Node {

    private int x;
    private int y;
    private int cost;
    private Node parent;
    private String ID;
    private static int countCreatedNodes = 0;

    public Node(int x, int y, int cost, Node parent){
        this.x = x; this.y = y; this.parent = parent; this.cost = cost;
        this.ID = x + "," + y;
        countCreatedNodes++;
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

    public String getPath(){
        return "";
    }

    public static int numberOfNodesCreated(){
        return countCreatedNodes;
    }

}
