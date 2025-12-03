import java.io.*;
import java.util.Arrays;

public class Ex1 {

    /**
     * Possible orders of expansion:
     * Clockwise = Right, Right-Down, Down, Left-Down, Left, Left-Up, Up, Right-Up, Enter-tunnel.
     * Counter-clockwise = Right, Right-Up, Up, Left-Up, Left, Left-Down, Down, Right-Down, Enter-tunnel.
     */
    protected static int[][]
            clockwiseOrder = {{1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {}},
            counterClockwiseOrder = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}, {}};
    private static final String INPUT = "input.txt", OUTPUT = "output.txt";

    public static void main(String[] args) {
        runAlgo(INPUT, OUTPUT);
    }

    protected static void runAlgo(String inputFileName, String outputFileName){
        Node.resetNumberOfNodesCreated();
        // Initialize variables and parse file:
        int rows, cols, startX = -1, startY = -1, goalX = -1, goalY = -1;
        boolean oldFirst = false, clockwise, withTime, withOpen;
        char[][] board;
        int[] charCounts;
        String algoName;
        Node start;
        Map map;
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            algoName = reader.readLine();  // Required algorithm
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
            charCounts = new int[15];
            board = new char[rows][cols];
            /*
             * Locations of tunnel entrances, by pairs.
             * the sub-array at tunnels[i] is:
             * {{x1, y1}, {x2, y2}},
             * the coordinates of the first and second entrances.
             */
            int[][] tunnels = new int[10][4];
            for (int[] t : tunnels){
                Arrays.fill(t, -1);
            }
            /*
             * Read from the file and build the map.
             * We insert into the array from the last row,
             * so that the intuitive 'y+1 means going up' logic can be used.
             * The last line read (representing the bottom of the map)
             * will be at the first index of the map array (index 0).
             * If we find a tunnel entrance, we add its location to the 'tunnels' array.
             * Update x,y values of start and goal.
             *
             * Read from line into row, by expected row length.
             * If a line is longer than expected, tail chars will be ignored.
             * If a line is too short, error message will be printed and exit.
             */
            try{
                String line = reader.readLine();
                int row = rows - 1;
                while (line != null){
                    if (line.length() < cols){
                        System.out.println("Line too short.");
                        System.exit(1);
                    }
                    for (int col = 0; col < cols; col++) {
                        char c = line.charAt(col);
                        board[row][col] = c;
                        if (c >= '0' && c <= '9'){
                            charCounts[c - '0']++;
                            int[] t = tunnels[c - '0'];
                            if (t[0] == -1) { t[0] = col; t[1] = row; }
                            else { t[2] = col; t[3] = row; }
                        }
                        else if (c == 'S') {
                            if (startX != -1 || startY != -1){
                                System.out.println("Multiple starts in map.");
                                System.exit(1);
                            }
                            startX = col; startY = row;
                        }
                        else if (c == 'G') {
                            if (goalX != -1 || goalY != -1){
                                System.out.println("Multiple goals in map.");
                                System.exit(1);
                            }
                            goalX = col; goalY = row;
                        }
                        else {
                            if (c == '-') charCounts[10]++;
                            if (c == '*') charCounts[11]++;
                            if (c == '~') charCounts[12]++;
                            if (c == '^') charCounts[13]++;
                            if (c == '#') charCounts[14]++;
                        }
                    }
                    row--;
                    line = reader.readLine();
                }
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }

            if (startX == -1 || startY == -1) {
                System.out.println("Start location not found.");
                System.exit(1);
            }
            if (goalX == -1 || goalY == -1) {
                System.out.println("Goal location not found.");
                System.exit(1);
            }

            map = new Map(board, tunnels, startX, startY, goalX, goalY, charCounts);
            start = new Node(startX, startY, 0, 'S', new int[]{0, 0}, null);
            Node.resetNumberOfNodesCreated();
            SearchAlgo algo = switch (algoName) {
                case "BFS" -> new BFS(clockwise, withTime, withOpen, map, start);
                case "A*" -> new AStar(clockwise, withTime, withOpen, oldFirst, map, start);
                case "DFID" -> new DFID(clockwise, withTime, withOpen, map, start);
                case "IDA*" -> new IDAStar(clockwise, withTime, withOpen, map, start);
                case "DFBnB" -> new DFBnB(clockwise, withTime, withOpen, oldFirst, map, start);
                default -> null;
            };

            // Run algorithm, create output, and record time:
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

            // Write output to file:
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
                writer.write(output.toString());
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error opening file: " + e.getMessage());
        }
    }
}
