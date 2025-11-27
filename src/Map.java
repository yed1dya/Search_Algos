import java.util.ArrayList;
import java.util.HashMap;

public class Map {
    private char[][] board;
    private int[][] tunnles;
    private int goalX, goalY, width, height;
    private boolean supplied;

    public Map(char[][] board, int[][] tunnles, int goalX, int goalY){
        this.board = board; this.tunnles = tunnles; this.goalX = goalX; this.goalY = goalY;
        this.supplied = false;
        this.height = board.length;
        this.width = board[0].length;
    }

    public Node[] adjacents(Node current){
        ArrayList<Node> adjacents = new ArrayList<>(), middle = new ArrayList<>();

        // build middle list
        // if counter-clockwise, reverse it
        // add right
        // add middle list
        // add center
        char c;
        // right

    }

    private int cost(int x, int y){
        char c = board[y][x];
        switch (c){
            case '#': return -1;
            case '-', '*', 'S': return 1;
            case '^', 'G': return 5;
            case '~': return 3;
        }
        if (c >= '0' && c <= '9') return 1;
        return -1;
    }

    private Node moveTo(int x, int y, boolean diag, Node current){
        char c = board[y][x]; Node next = null;
        if (c != '#' && (supplied || c != '~')){
            int extraCost = (diag && c == '^') ? 5 : 0;
            next = new Node(x, y, cost(x, y) + extraCost, current);
        }
        return next;
    }

    private Node right(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        if (x < width - 1){
            x++;
            next = moveTo(x, y, false, current);
        }
        return next;
    }

    private Node rightDown(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        if (x < width - 1 && y > 0){
            y--; x++;
            next = moveTo(x, y, true, current);
        }
        return next;
    }

    private Node down(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        if (y > 0){
            y--;
            next = moveTo(x, y, false, current);
        }
        return next;
    }

    private Node leftDown(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        if (x > 0 && y > 0){
            x--; y--;
            next = moveTo(x, y, true, current);
        }
        return next;
    }

    private Node left(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        if (x > 0){
            x--;
            next = moveTo(x, y, false, current);
        }
        return next;
    }

    private Node leftUp(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        if (x > 0 && y < height - 1){
            x--; y++;
            next = moveTo(x, y, true, current);
        }
        return next;
    }

    private Node up(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        if (y < height - 1){
            y++;
            next = moveTo(x, y, false, current);
        }
        return next;
    }

    private Node rightUp(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        if (x < width - 1 && y < height - 1){
            x++; y++;
            next = moveTo(x, y, true, current);
        }
        return next;
    }

    private Node enterTunnle(Node current){
        int x = current.x(), y = current.y(); Node next = null;
        char c = board[y][x];
        if (c >= '0' && c <= '9') {
            int number = Integer.parseInt("" + c);
            int[] pair = tunnles[number];
            if (x == pair[0] && y == pair[1]){
                x = pair[2]; y = pair[3];
            }
            else if (x == pair[2] && y == pair[3]) {
                x = pair[0]; y = pair[1];
            }
            next = moveTo(x, y, false, current);
        }
        return next;
    }
}
