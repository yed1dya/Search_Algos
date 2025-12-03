import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class TestEx1 {

    /**
     * Iterates over all files in the given directory and returns the names
     * of those that end with ".txt".
     *
     * @param directoryPath The path to the directory to search.
     * @return A list of .txt file names found, or an empty list if the directory
     * is invalid or no .txt files are found.
     */
    public List<String> getTxtFileNames(String directoryPath) {
        // Create a File object for the specified directory
        File directory = new File(directoryPath);

        // Check if the path is a valid directory
        if (!directory.isDirectory()) {
            System.err.println("Error: The path provided is not a directory or does not exist.");
            return new ArrayList<>();
        }

        // Define a filter to accept only files ending with ".txt"
        FilenameFilter txtFilter = (_, name) -> name.endsWith(".txt")
                && !name.contains("AStar") && !name.contains("BFS") && !name.contains("DFBnB")
                && name.contains("DFID") && !name.contains("IDAStar") && !name.contains("beach");

        // List the files that satisfy the filter
        File[] txtFiles = directory.listFiles(txtFilter);

        // Check if any files were found
        if (txtFiles == null || txtFiles.length == 0) {
            return new ArrayList<>();
        }

        // Convert the array of File objects to a list of file names (String)
        List<String> fileNames = new ArrayList<>();
        for (File file : txtFiles) {
            fileNames.add(file.getName());
        }

        return fileNames;
    }

    public static void main(String[] args) {
        TestEx1 lister = new TestEx1();
        String relativeDir = "Sample inputs";
        List<String> names = lister.getTxtFileNames(relativeDir);
        if (names.isEmpty()) {
            System.out.println("No .txt files found in: " + relativeDir);
        } else {
            System.out.println("Found .txt files:");
            for (String fileName : names) {
                System.out.print("running: " + fileName + "...   ");
                long startTime = System.nanoTime();
                Ex1.runAlgo("Sample inputs/" + fileName, "output " + fileName);
                long endTime = System.nanoTime();
                double durationSeconds = (double) (endTime - startTime) / 1_000_000_000.0;
                String formattedTime = String.format("%.3f", durationSeconds);
                System.out.println("  Done! time: " + formattedTime);
            }
        }
    }
}