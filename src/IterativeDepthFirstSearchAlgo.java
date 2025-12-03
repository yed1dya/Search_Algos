import java.util.Stack;

public abstract class IterativeDepthFirstSearchAlgo extends SearchAlgo{

    protected Stack<InOutNode> stack = new Stack<>();
    protected int maxF;

    /**
     * Max cost would be going over all tiles (and assume we go over '^' diagonally) once.
     * But in a case like this:
     * G~S^^^^^*
     * We would need to go all the way to the right to get supplied, then back to the left,
     * Going over most tiles twice. So we take twice the bound.
     * And if we do need to go there and back, it's because there is at least one '~'
     * between the S and G which we don't go over the first time, which saves us a cost of 3.
     * We add 1 for S and 5 for G.
     *
     * @param map The map.
     * @return Max potential f-value on the map.
     */
    protected int maxF(Map map){
        int[] counts = map.charCounts();
        int totalSum = 0;
        for (int i = 0; i < counts.length - 1; i++) {
            int cost = 1;
            if (i == 12) cost = 3;
            if (i == 13) cost = 10;
            totalSum += counts[i] * cost;
        }
        return totalSum * 2 + 3;
    }

}
