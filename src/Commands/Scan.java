package Commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Scan {

    //attributes
    private static List<String> directoryMap = new ArrayList<>();
    private String path = null;
    private String option = null;
    private final String directorySaveName;
    private String directorySaveDates = null;
    private String directoryScanIgnor = null;
    private int ver1 = -1;

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
        pitDirectory(storage,myDirectory);
    }

    public void scanPathOrFile(String pathOrFile, String control){
        Path pathFile = Path.of(pathOrFile);
        File fileFile = pathFile.toFile();
        try {
            Path a = Path.of(directorySaveName);
            Scanner iPath = new Scanner(a.toFile());
            while (iPath.hasNextLine()) {
                directoryMap.add(iPath.nextLine());
                ver1 = ver1 + 1;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(control == "file"){
            //add in txt file
            try {
                int i = 0;
                if(ver1 == -1){
                    Files.writeString(Path.of(directorySaveName),pathOrFile.substring(path.length()), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                    return;
                }
                while(i < directoryMap.size() && !(directoryMap.get(i).contains(fileFile.getPath().substring(path.length())))){
                    i = i + 1;
                }
                if(i != directoryMap.size()){
                    return;
                }
                Files.writeString(Path.of(directorySaveName),pathOrFile.substring(path.length()), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else if(control == "path"){
            //search in a directory
            directoryMap.add(fileFile.getPath().substring(path.length()));
            ver1 = ver1 + 1;
            File myDirectory = (Path.of(path + "\\.pit")).toFile();
            pitDirectory(fileFile,myDirectory);
        }
    }

    private void pitDirectory (File storage, File myDirectory){
        int iv = ver1;
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
                        int i = 0;
                        while(!((directoryMap.get(i)==(childFile.getPath().substring(path.length())))) && ver1 != -1){
                            if(i == ver1){
                                break;
                            }
                            i = i + 1;
                        }
                        if(!ver && !(directoryMap.get(i) == (childFile.getPath().substring(path.length()))) && ver1 != -1){
                            ver1 = ver1 + 1;
                            directoryMap.add(childFile.getPath().substring(path.length())); //add in directoryMap a relative path
                        }
                    }

                }
            }
        }
        try {
            if(iv != ver1){
                if(iv == -1){
                    iv = 0;
                }
                while (directoryMap.get(iv) != null && iv != ver1) {
                    iv = iv + 1;
                    Files.writeString(Path.of(directorySaveName),directoryMap.get(iv) + "\n", StandardCharsets.UTF_8,StandardOpenOption.APPEND);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getAllDirectory(File file){
        if(file.isDirectory()){
            File[] children = file.listFiles();
            if(children != null){
                for(File childFile : children){
                    int i = 0;
                    while(!(directoryMap.get(i).contains(childFile.getPath().substring(path.length()))) && ver1 != -1){
                        if(i == ver1){
                            break;
                        }
                        i = i + 1;
                    }
                    if(!directoryMap.get(i).contains(childFile.getPath().substring(path.length())) && ver1 != -1){
                        ver1 = ver1 + 1;
                        directoryMap.add(childFile.getPath().substring(path.length())); //add in directoryMap a relative path
                    }
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
