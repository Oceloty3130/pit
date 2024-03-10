package Commands;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Scan {

    //attributes
    private static List<String> directoryMap = new ArrayList<>();
    private String path = null;
    private String directorySaveName = null;
    private int iv = 0;
    private String option = null;
    private String optionStatus;
    private static HashMap<Integer,Entries> directoryHashMap = new HashMap<Integer, Entries>();
    private static Map<Integer,Entries> treeMap = new TreeMap<>();
    private static List<String> directoryMapDatesS = new ArrayList<>();
    private static List<String> directoryMapNameS = new ArrayList<>();

    //constructor
    public Scan(String path){
        this.path = path;
    }
    public Scan(String path, String directorySaveName, String optionStatus){
        this.path = path;
        this.directorySaveName = directorySaveName;
        this.optionStatus = optionStatus;
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
        String file = path + "\\" + pathOrFile;
        Path pathFile = Path.of(file);
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
                    Files.writeString(pathDirectorySaveName,file.substring(path.length()) + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                    return;
                }
                if(verificationString(fileFile, path)){
                    Files.writeString(pathDirectorySaveName,file.substring(path.length()) + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else if(Objects.equals(control, "directory")){
            //search in a directory
            directoryMap.add(fileFile.getPath().substring(path.length()));
            File[] children = fileFile.listFiles();
            File myDirectory = new File(path + "\\.pit");
            verificationPit(children,myDirectory);
            pitDirectory();
        }
    }

    private void pitDirectory(){
        if(Objects.equals(optionStatus, "add") && (Path.of(path + "\\.pit\\save\\00").toFile().exists())){
            readIntoSave();
        }
        try {
            if(iv != directoryMap.size()){
                while (iv < directoryMap.size()) {
                    if(Objects.equals(optionStatus, "add")){
                        if(!treeMap.isEmpty()){
                            File fileFile = Path.of(path + directoryMap.get(iv)).toFile();
                            if(treeMap.containsKey(directoryMap.get(iv).hashCode())) {
                                Entries test = treeMap.get(directoryMap.get(iv).hashCode());
                                if (!Objects.equals(test.getDate(), String.valueOf(fileFile.lastModified()))) {
                                    Files.writeString(Path.of(directorySaveName), directoryMap.get(iv) + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                                }
                            }else if(!fileFile.isDirectory()){
                                Files.writeString(Path.of(directorySaveName),directoryMap.get(iv) + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                            }
                        }else{
                            Files.writeString(Path.of(directorySaveName),directoryMap.get(iv) + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                        }
                    }else if(Objects.equals(optionStatus, "status")){
                        Files.writeString(Path.of(directorySaveName),directoryMap.get(iv) + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                    }
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
                    if((directoryMap.size() == 0 || !ver) && verificationString(childFile,path)){
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
                    if(verificationString(childFile, path) || directoryMap.size() == 0){
                        directoryMap.add(childFile.getPath().substring(path.length()));
                    }
                    getAllDirectory(childFile);
                }
            }
        }
    }

    private boolean verificationString(File fileFile, String path){
        int i = 0;
        while(i < directoryMap.size() && !(Objects.equals(directoryMap.get(i), fileFile.getPath().substring(path.length())))){
            i = i + 1;
        }
        return i == directoryMap.size();
    }


    //read to save directory
    private void readIntoSave(){
        String pathCurrentDirectory = Scan.scanSaveDirectory(path + "\\.pit\\save" , "current");
        String commitMapName = pathCurrentDirectory + "\\.info\\directoryMap.txt";
        readToFile(Path.of(commitMapName),"name");
        String commitMapDates = pathCurrentDirectory + "\\.info\\datesDirectoryMap.txt";
        readToFile(Path.of(commitMapDates),"date");
        int i = 0;
        int j = 0;
        String currentFile;
        while(i < directoryMapNameS.size()){
            currentFile = path + directoryMapNameS.get(i);
            if(!Path.of(currentFile).toFile().isDirectory()) {
                directoryHashMap.put(directoryMapNameS.get(i).hashCode(), new Entries(directoryMapNameS.get(i), directoryMapDatesS.get(j)));
                i += 1;
                j += 1;
            }
        }
        treeMap.putAll(directoryHashMap);
    }

    private void readToFile(Path pathDirectorySave, String optionRead){
        try {
            Scanner iPath = new Scanner(pathDirectorySave.toFile());
            while (iPath.hasNextLine()) {
                String pathC = iPath.nextLine();
                if(Objects.equals(optionRead, "date")){
                    directoryMapDatesS.add(pathC);
                }else if(Objects.equals(optionRead,"name")) {
                    if (Path.of(path + pathC).toFile().isFile()) {
                        directoryMapNameS.add(pathC);
                    }
                }
            }
            iPath.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
}
