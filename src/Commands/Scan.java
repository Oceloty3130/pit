package Commands;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Scan {

    //attributes
    private static List<String> directoryMap = new ArrayList<>();
    private static List<String> datesDirectoryMap = new ArrayList<>();
    private String path = null;
    private String option = null;
    private String directorySaveName;
    private String directorySaveDates = null;
    private String directoryScanIgnor = null;

    //constructor
    public Scan(String path, String directoryScanIgnor, String directorySaveName, String option){
        this.path = path;
        this.option = option;
        this.directoryScanIgnor = directoryScanIgnor;
        this.directorySaveName = directorySaveName;
    }

    public Scan(String path, String directoryScanIgnor, String directorySaveName, String directorySaveDates, String option){
        this.path = path;
        this.directoryScanIgnor = directoryScanIgnor;
        this.directorySaveName = directorySaveName;
        this.directorySaveDates = directorySaveDates;
        this.option = option;
    }

    //methode
    public void createDirectoryMap(){
        var storage = new File(path);
        File myDirectory = new File(path + directoryScanIgnor); // myDirectory is file which is ignored
        if(storage.isDirectory()){
            File[] children = storage.listFiles();
            if(children != null){
                for(File childFile : children){
                    if(childFile.isDirectory()){
                        try {
                            if(!Files.isSameFile(childFile.toPath(), myDirectory.toPath())){
                                getAllDirectory(childFile);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }else{
                        boolean ver = false;
                        try {
                            if(Files.isSameFile(childFile.toPath(), Path.of(path + "\\pit.jar"))){
                                ver = true;
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if(!ver){
                            directoryMap.add(childFile.getPath().substring(path.length())); //add in directoryMap a relative path
                        }
                    }

                }
            }
        }
        try {
            Files.write(Path.of(directorySaveName),directoryMap, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            if(directorySaveDates != null){
                Files.write(Path.of(directorySaveDates),datesDirectoryMap, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void scanPathOrFile(String pathOrFile, String control){
        Path pathFile = Path.of(pathOrFile);
        File fileFile = pathFile.toFile();
        if(control == "file"){
            //add in txt file
            try {
                Files.writeString(Path.of(directorySaveName),pathOrFile.substring(path.length()), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else if(control == "path"){
            //search in a directory
            directoryMap.add(fileFile.getPath().substring(path.length()));
            if(fileFile.isDirectory()){
                File[] children = fileFile.listFiles();
                if(children != null){
                    for(File childFile : children){
                        if(childFile.isDirectory()){
                            try {
                                if(!Files.isSameFile(childFile.toPath(), Path.of(path + "\\.pit"))){
                                    getAllDirectory(childFile);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }else{
                            boolean ver = false;
                            try {
                                if(Files.isSameFile(childFile.toPath(), Path.of(path + "\\pit.jar"))){
                                    ver = true;
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            if(!ver) {
                                directoryMap.add(childFile.getPath().substring(path.length())); //add in directoryMap a relative path
                            }
                        }
                    }
                }
            }
            try {
                Files.write(Path.of(directorySaveName),directoryMap, StandardCharsets.UTF_8,StandardOpenOption.APPEND);
                if(directorySaveDates != null){
                    Files.write(Path.of(directorySaveDates),datesDirectoryMap, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void getAllDirectory(File file){
        if(file.isDirectory()){
            File[] children = file.listFiles();
            if(children != null){
                for(File childFile : children){
                    if(directorySaveDates != null){
                        datesDirectoryMap.add(String.valueOf(childFile.lastModified()));
                    }
                    directoryMap.add(childFile.getPath().substring(path.length())); //add in directoryMap a relative path
                    getAllDirectory(childFile);
                }
            }
        }
    }

    public static String scanSaveDirectory(String path, String option){
        String directoryPath = null;
        var storage = new File(path);
        File[] children = storage.listFiles();
        if(children != null){
            int n = 0;
            for (File childFile : children){
                n = n + 1;
            }
            if(option == "new"){
                String nString = "\\0" + n;
                directoryPath = path + nString;
            }else if (option == "current"){
                String nString = "\\0" + (n-1);
                directoryPath = path + nString;
            }
        }
        return directoryPath;
    }

}
