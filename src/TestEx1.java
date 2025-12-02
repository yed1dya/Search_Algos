import java.io.*;

public class TestEx1 {

    public static void main(String[] args) {
        String[] algorithms = {"BFS", "A*", "DFID", "IDA*", "DFBnB"};
        String[] orders = {"clockwise", "counter-clockwise"};
        String[] tieBreakers = {"old-first", "new-first"};

        File mazeDir = new File("sample mazes");
        if (!mazeDir.exists() || !mazeDir.isDirectory()) {
            System.err.println("Directory 'sample mazes' not found.");
            return;
        }
        File[] mazeFiles = mazeDir.listFiles((dir, name) -> dir.equals(mazeDir) && name.endsWith(".txt"));

        if (mazeFiles == null || mazeFiles.length == 0) {
            System.out.println("No maze files found in the maze folder.");
            return;
        }

        int count = 0;
        for (File mazeFile : mazeFiles) {
            String mazeData = readMazeFile(mazeFile);
            if (mazeData == null) continue;

            String originalName = mazeFile.getName();
            String mazeNameClean = originalName.replace(".txt", "");

            for (String algo : algorithms) {
                for (String order : orders) {
                    boolean needsTieBreaker = algo.equals("A*") || algo.equals("DFBnB");
                    if (needsTieBreaker) {
                        for (String tie : tieBreakers) {
                            generateFile(algo, order, tie, mazeNameClean, mazeData);
                        }
                    } else {
                        generateFile(algo, order, null, mazeNameClean, mazeData);
                    }
                    count++;
                }
            }
        }
        System.out.println("Test generation complete. Generated " + count + " files.");
    }

    private static void generateFile(String algo, String order, String tie, String mazeName, String mazeData) {
        String fileName;
        String line1;
        if (algo.equals("A*")) algo = "AStar";
        if (algo.equals("IDA*")) algo = "IDAStar";
        if (tie != null) {
            fileName = String.format("%s %s %s %s.txt", algo, order, tie, mazeName);
            line1 = order + " " + tie;
        } else {
            fileName = String.format("%s %s %s.txt", algo, order, mazeName);
            line1 = order;
        }
        if (algo.equals("AStar")) algo = "A*";
        if (algo.equals("IDAStar")) algo = "IDA*";
        StringBuilder content = new StringBuilder();
        content.append(algo).append("\n");          // Line 0
        content.append(line1).append("\n");         // Line 1
        content.append("with time").append("\n");   // Line 2
        content.append("no open").append("\n");     // Line 3
        content.append(mazeData);                   // Maze dimensions and board

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content.toString());
            System.out.println("Generated: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing file " + fileName + ": " + e.getMessage());
        }
    }

    private static String readMazeFile(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            System.err.println("Error reading maze file " + file.getName() + ": " + e.getMessage());
            return null;
        }
    }
}