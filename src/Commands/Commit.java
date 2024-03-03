package Commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Commit {

    //attributes
    private String saveTxtDirectory;

    private String saveDatesTxtDirectory;
    private String saveDirectory;
    private String path;
    private String option;
    private String scanDirectory;

    //constructor
    public Commit(String path1, String option1) {
        this.path = path1;
        this.option = option1;
    }

    //methods
    public void saveDirectory() {
        Create.directoryCreate(path);
        saveDirectory = Scan.scanSaveDirectory(path + "\\.pit\\save", "current");
        saveTxtDirectory = saveDirectory + "\\.info\\directoryMap.txt";
        saveDatesTxtDirectory = saveDirectory + "\\.info\\datesDirectoryMap.txt";
        scanDirectory = "\\.pit";
        Scan obj = new Scan(path, scanDirectory, saveTxtDirectory, saveDatesTxtDirectory, option);
        obj.createDirectoryMap();
        try {
            Path a = Path.of(saveTxtDirectory);
            Scanner iPath = new Scanner(a.toFile());
            while (iPath.hasNextLine()) {
                String data = iPath.nextLine();
                String fullPath = saveDirectory + data;
                String fullPathCopy = path + data;
                File dataFile = new File(fullPathCopy);
                if (dataFile.isDirectory()) {
                    Files.createDirectories(Path.of(fullPath));
                } else {
                    Files.copy(Path.of(fullPathCopy), Path.of(fullPath), REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
