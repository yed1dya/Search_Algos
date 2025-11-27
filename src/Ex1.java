import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Ex1 {

    public static void main(String[] args) {
        runAlgo("input.txt");
    }

    public static void runAlgo(String fileName){
        int rows, cols, startX = -1, startY = -1, goalX = -1, goalY = -1;
        boolean clockwise, requiresOldFirst = false, oldFirst = false, withTime, withOpen;
        char[][] board;
        String algoName;
        Node start;
        Map map;
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            algoName = reader.readLine();
            String[] lineArr = reader.readLine().split(" ");
            clockwise = lineArr[0].equals("clockwise");
            if (lineArr.length == 2) {
                oldFirst = lineArr[1].equals("old-first");
                requiresOldFirst = true;
            }
            withTime = reader.readLine().equals("with time");
            withOpen = reader.readLine().equals("with open");
            lineArr = reader.readLine().split("x");
            rows = Integer.parseInt(lineArr[0]);
            cols = Integer.parseInt(lineArr[1]);
            String line = reader.readLine();
            board = new char[rows][cols];
            int[][] tunnles = new int[10][4];
            for (int[] t : tunnles){
                Arrays.fill(t, -1);
            }
            int row = rows - 1;
            while (line != null){
                for (int col = 0; col < line.length(); col++) {
                    char c = line.charAt(col);
                    board[row][col] = c;
                    if (c >= '0' && c <= '9'){
                        int[] t = tunnles[c - 48];
                        if (t[0] == -1) { t[0] = col; t[1] = row; }
                        else { t[2] = col; t[3] = row; }
                    }
                    else if (c == 'S') { startX = col; startY = row; }
                    else if (c == 'G') { goalX = col; goalY = row; }
                }
                row--;
                line = reader.readLine();
            }
            start = new Node(startX, startY, 0, null);
            map = new Map(board, tunnles, goalX, goalY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("algo: " + algoName);
        System.out.println("clockwise: " + clockwise);
        if (requiresOldFirst) System.out.println("old first: " + oldFirst);
        System.out.println("with time: " + withTime);
        System.out.println("with open: " + withOpen);
        System.out.println("dimensions: " + rows + "x" + cols);
        System.out.println("board:");
        for (char[] row : board) {
            for (char c : row) {
                System.out.print(c);
            }
            System.out.println();
        }

        SearchAlgo algo;
        switch (algoName){
            case "BFS": {
                algo = new BFS();
            }
        }
    }
}
