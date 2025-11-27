import java.util.ArrayList;

public class Node {

    private int x, y, cost;
    private Node parent;
    private String ID;

    public Node(int x, int y, int cost, Node parent){
        this.x = x; this.y = y; this.parent = parent; this.cost = cost; this.c = c;
        this.ID = "(" + x + "," + y + ")";
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
}
