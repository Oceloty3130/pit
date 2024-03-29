package Commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Create {

    //attributes
    private static String directoryPath;

    //methode
    public static void init(String path) {
        directoryPath = path + "\\.pit\\save";
        try{
            Files.createDirectories(Paths.get(directoryPath));
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    //methods
    public static void directoryCreate(String path){
        String option = "new";
        try{
            directoryPath = Scan.scanSaveDirectory(path + "\\.pit\\save",option);
            Files.createDirectories(Paths.get(directoryPath + "\\.info"));
            Files.createFile(Paths.get(directoryPath+"\\.info\\directoryMap.txt"));
            Files.createFile(Paths.get(directoryPath+"\\.info\\datesDirectoryMap.txt"));
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void directoryTemp(String path, String option){
        directoryPath = path + "\\.pit\\temp";
        try{
            Files.createDirectories(Paths.get(directoryPath));
            if(Objects.equals(option, "compare")){
                Files.createFile(Paths.get(directoryPath+"\\MapStatus.txt"));
            }else if(option == null){
                Files.createFile(Paths.get(directoryPath+"\\directoryMap.txt"));
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    static void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        try {
            Files.delete(directoryToBeDeleted.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
