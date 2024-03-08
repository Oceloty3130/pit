package Commands;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Commit {

    //attributes
    private String saveTxtDirectory;

    private String saveDatesTxtDirectory;
    private String saveDirectory;
    private String path;
    private String option;
    private String scanFile;

    //constructor
    public Commit(String path1, String option1) {
        this.path = path1;
        this.option = option1; // add message
    }

    //methods
    public void saveDirectory() {
        Create.directoryCreate(path);
        saveDirectory = Scan.scanSaveDirectory(path + "\\.pit\\save", "current");
        saveTxtDirectory = saveDirectory + "\\.info\\directoryMap.txt";
        saveDatesTxtDirectory = saveDirectory + "\\.info\\datesDirectoryMap.txt";
        scanFile =  path + "\\.pit\\temp\\directoryMap.txt";
        try {
            Path a = Path.of(scanFile);
            Scanner iPath = new Scanner(a.toFile());
            while (iPath.hasNextLine()) {
                String data = iPath.nextLine();
                String fullPath = saveDirectory + data;
                String fullPathCopy = path + data;
                File dataFile = new File(fullPathCopy);
                if (dataFile.isDirectory()) {
                    Files.createDirectories(Path.of(fullPath));
                } else {
                    Files.createDirectories(Path.of(fullPath).getParent());
                    Files.copy(Path.of(fullPathCopy), Path.of(fullPath), REPLACE_EXISTING);
                    Files.writeString(Path.of(saveDatesTxtDirectory), dataFile.lastModified() + "\n", StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                }
            }
            iPath.close();
            Files.copy(Path.of(scanFile), Path.of(saveTxtDirectory), REPLACE_EXISTING);
            Create.deleteDirectory(Path.of(path + "\\.pit\\temp").toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
