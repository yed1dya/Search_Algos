import java.io.*;
import java.util.Arrays;

public class Ex1 {

    static String[] clockwiseOrder = {"R", "RD", "D", "LD", "L", "LU", "U", "RU", "Ent"},
            counterClockwiseOrder = {"R", "RU", "U", "LU", "L", "LD", "D", "RD", "Ent"};

    public static void main(String[] args) {
        runAlgo("input.txt");
    }

    public static void runAlgo(String inputFileName){
        int rows, cols, startX = -1, startY = -1, goalX = -1, goalY = -1;
        boolean clockwise, oldFirst = false, withTime, withOpen;
        char[][] board;
        String algoName;
        Node start;
        Map map;
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            algoName = reader.readLine();
            String[] lineArr = reader.readLine().split(" ");
            clockwise = lineArr[0].equals("clockwise");
            if (lineArr.length == 2) {
                oldFirst = lineArr[1].equals("old-first");
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
            start = new Node(startX, startY, 0, "", null);
            map = new Map(board, tunnles, goalX, goalY);
            SearchAlgo algo = switch (algoName) {
                case "BFS" -> new BFS(clockwise, withTime, withOpen, map, start);
                case "DFID" -> new DFID();
                default -> null;
            };
            String output = "";
            if (algo != null) output = algo.output();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
                writer.write(output);
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error opening file: " + e.getMessage());
        }
    }
}
