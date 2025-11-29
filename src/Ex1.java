import java.io.*;
import java.util.Arrays;

public class Ex1 {

    // {"R", "RD", "D", "LD", "L", "LU", "U", "RU", "Ent"}
    protected static int[][] clockwiseOrder = {{1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {}},
            counterClockwiseOrder = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}, {}};
    //{"R", "RU", "U", "LU", "L", "LD", "D", "RD", "Ent"};

    public static void main(String[] args) {
        runAlgo();
    }

    private static void runAlgo(){
        int rows, cols, startX = -1, startY = -1, goalX = -1, goalY = -1;
        boolean clockwise, oldFirst = false, withTime, withOpen;
        char[][] board;
        String algoName;
        Node start;
        Map map;
        try(BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
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
            int[][] tunnels = new int[10][4];
            for (int[] t : tunnels){
                Arrays.fill(t, -1);
            }
            int row = rows - 1;
            while (line != null){
                for (int col = 0; col < line.length(); col++) {
                    char c = line.charAt(col);
                    board[row][col] = c;
                    if (c >= '0' && c <= '9'){
                        int[] t = tunnels[c - 48];
                        if (t[0] == -1) { t[0] = col; t[1] = row; }
                        else { t[2] = col; t[3] = row; }
                    }
                    else if (c == 'S') { startX = col; startY = row; }
                    else if (c == 'G') { goalX = col; goalY = row; }
                }
                row--;
                line = reader.readLine();
            }
            map = new Map(board, tunnels, goalX, goalY);
            start = new Node(startX, startY, 0, map.f(startX, startY, 0), 'S', new int[]{0, 0}, null);
            SearchAlgo algo = switch (algoName) {
                case "BFS" -> new BFS(clockwise, withTime, withOpen, map, start);
                case "A*" -> new AStar(clockwise, withTime, withOpen, oldFirst, map, start);
                case "DFID" -> new DFID(clockwise, withTime, withOpen, map, start);
                default -> null;
            };

            long startTime = System.nanoTime();
            String[] results = algo != null ? algo.output() : new String[4];
            long endTime = System.nanoTime();
            StringBuilder output = new StringBuilder(results[0]).append('\n');
            output.append("Num: ").append(results[1]).append('\n');
            output.append("Max space: ").append(results[2]).append('\n');
            output.append("Cost: ").append(results[3]);
            if (withTime){
                double durationSeconds = (double) (endTime - startTime) / 1_000_000_000.0;
                String formattedTime = String.format("%.3f", durationSeconds);
                output.append('\n').append(formattedTime).append(" seconds");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
                writer.write(output.toString());
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error opening file: " + e.getMessage());
        }
    }
}
