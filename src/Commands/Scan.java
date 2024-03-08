package Commands;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Scan {

    //attributes
    private static List<String> directoryMap = new ArrayList<>();
    private String path = null;
    private String directorySaveName = null;
    private int iv = 0;
    private String option = null;

    //constructor
    public Scan(String path){
        this.path = path;
    }
    public Scan(String path, String directorySaveName){
        this.path = path;
        this.directorySaveName = directorySaveName;
    }

    //methode
    public void createDirectoryMap(){
        var storage = new File(path);
        File myDirectory = new File(path + "\\.pit"); // myDirectory is file which is ignored
        if(option == null){
            try {
                Scanner iPath = new Scanner(Path.of(directorySaveName).toFile());
                while (iPath.hasNextLine()) {
                    directoryMap.add(iPath.nextLine());
                }
                iPath.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            iv = directoryMap.size();
        }
        if(storage.isDirectory()){
            File[] children = storage.listFiles();
            verificationPit(children,myDirectory);
        }
        if(option == null){
            pitDirectory();
        }
    }

    public void scanPathOrFile(String pathOrFile, String control){
        Path pathFile = Path.of(pathOrFile);
        File fileFile = pathFile.toFile();
        Path pathDirectorySaveName = Path.of(directorySaveName);
        try {
            Scanner iPath = new Scanner(pathDirectorySaveName.toFile());
            while (iPath.hasNextLine()) {
                directoryMap.add(iPath.nextLine());
            }
            iPath.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        iv = directoryMap.size();
        if(Objects.equals(control, "file")){
            //add in txt file
            try {
                if(directoryMap.size() == 0){
                    Files.writeString(pathDirectorySaveName,pathOrFile.substring(path.length()), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                    return;
                }
                if(verificationString(fileFile, path) != 0){
                    Files.writeString(pathDirectorySaveName,pathOrFile.substring(path.length()), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else if(Objects.equals(control, "path")){
            //search in a directory
            directoryMap.add(fileFile.getPath().substring(path.length()));
            File myDirectory = (Path.of(path + "\\.pit")).toFile();
            pitDirectory();
        }
    }

    private void pitDirectory(){
        try {
            if(iv != directoryMap.size()){
                while (iv < directoryMap.size()) {
                    Files.writeString(Path.of(directorySaveName),directoryMap.get(iv) + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                    iv = iv + 1;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void verificationPit(File[] children, File myDirectory){
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
                    if((directoryMap.size() == 0 || !ver) && verificationString(childFile,path) != 0){
                        directoryMap.add(childFile.getPath().substring(path.length())); //add in directoryMap a relative path
                    }
                }

            }
        }
    }

    private void getAllDirectory(File file){
        if(file.isDirectory()){
            File[] children = file.listFiles();
            if(children != null){
                for(File childFile : children){
                    if(verificationString(childFile, path) != 0 || directoryMap.size() == 0){
                        directoryMap.add(childFile.getPath().substring(path.length()));
                    }
                    getAllDirectory(childFile);
                }
            }
        }
    }

    private static int verificationString(File fileFile, String path){
        int i = 0;
        while(i < directoryMap.size() && !(Objects.equals(directoryMap.get(i), fileFile.getPath().substring(path.length())))){
            i = i + 1;
        }
        if(i != directoryMap.size()){
            return 0;
        }
        return i;
    }

    //methode Create
    public static String scanSaveDirectory(String path, String option){
        String directoryPath = null;
        var storage = new File(path);
        File[] children = storage.listFiles();
        if(children != null){
            int n = 0;
            for (File ignored : children){
                n = n + 1;
            }
            if(Objects.equals(option, "new")){
                String nString = "\\0" + n;
                directoryPath = path + nString;
            }else if (Objects.equals(option, "current")){
                String nString = "\\0" + (n-1);
                directoryPath = path + nString;
            }
        }
        return directoryPath;
    }

    //Set
    public void setOption(String option) {
        this.option = option;
    }
}
