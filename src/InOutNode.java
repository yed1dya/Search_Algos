public class InOutNode extends Node{

    private boolean out;

    /**
     * Constructor.
     *
     * @param x      x-coordinate.
     * @param y      y-coordinate.
     * @param cost   The cost of reaching the node (from start).
     * @param f      The f-value of the node (g+h).
     * @param c      The char in the map at location x,y.
     * @param dir    The direction of movement that produced the node.
     * @param parent The previous node.
     */
    protected InOutNode(int x, int y, int cost, int f, char c, int[] dir, Node parent) {
        super(x, y, cost, f, c, dir, parent);
        this.out = false;
    }

    protected InOutNode(Node n){
        super(n.x(), n.y(), n.getCost(), n.f(), n.getCh(), n.getDir(), n.getParent());
        this.out = false;
    }

    protected boolean isOut(){
        return this.out;
    }

    protected void setOut(){
        this.out = true;
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder("[" + this.x + "," + this.y);
        s.append(" | ").append(this.ch);
        s.append(" | ").append(this.cost);
        s.append(" | ").append(this.f);
        if (supplied) s.append(" | sup | ");
        else s.append(" | not | ");
        if (out) s.append("out | ");
        else s.append("in | ");
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
