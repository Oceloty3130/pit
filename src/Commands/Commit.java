package Commands;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Commit {

    //attributes
    private String saveTxtDirectory;
    private String saveDatesTxtDirectory;
    private String saveDirectory;
    private String path = null;
    private String scanFile;
    private static Map<Integer,Entries> directoryHashMap = new HashMap<Integer, Entries>();
    private static TreeMap<Integer, Entries> treeMap = new TreeMap<Integer, Entries>();
    private static List<String> date = new ArrayList<>();

    //constructor
    public Commit(String path1) {
        this.path = path1;
    }

    //methods
    public void saveDirectory() {
        if(Path.of(path + "\\.pit\\save\\00").toFile().exists()){
            fullCopy();
        }
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
                Path fullPathPath = Path.of(fullPath);
                if (dataFile.isDirectory()) {
                    Files.createDirectories(fullPathPath);
                } else {
                    Files.createDirectories(fullPathPath.getParent());
                    Files.copy(Path.of(fullPathCopy), fullPathPath, REPLACE_EXISTING);
                }
//              Files.writeString(Path.of(saveDatesTxtDirectory), dataFile.lastModified() + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                if(treeMap.containsKey(data.hashCode())){
                    Entries test = treeMap.get(data.hashCode());
                    if(!Objects.equals(test.getDate(),String.valueOf(dataFile.lastModified()))){
                        test.setDate(String.valueOf(dataFile.lastModified()));
                    }
                }else{
                    if(!dataFile.isDirectory()){
                        Entries test = new Entries(data,String.valueOf(dataFile.lastModified()));
                        treeMap.put(data.hashCode(),test);
                    }
                }
            }
            iPath.close();
//            Files.copy(Path.of(scanFile), Path.of(saveTxtDirectory), REPLACE_EXISTING);
            Create.deleteDirectory(Path.of(path + "\\.pit\\temp").toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(!treeMap.isEmpty()){
            for(Map.Entry<Integer,Entries> s : treeMap.entrySet()){
                Entries test = s.getValue();
                try {
                    Files.writeString(Path.of(saveDatesTxtDirectory), test.getDate() + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                    Files.writeString(Path.of(saveTxtDirectory), test.getName() + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void fullCopy(){
        saveDirectory = Scan.scanSaveDirectory(path + "\\.pit\\save", "current");
        saveTxtDirectory = saveDirectory + "\\.info\\directoryMap.txt";
        saveDatesTxtDirectory = saveDirectory + "\\.info\\datesDirectoryMap.txt";
        scanFile =  path + "\\.pit\\temp\\directoryMap.txt";
        Path fileFile = Path.of(saveDatesTxtDirectory);
        String option = "date";
        readToFile(fileFile,option);
        fileFile = Path.of(saveTxtDirectory);
        option = "name";
        readToFile(fileFile,option);
    }

    private void readToFile(Path fileFile, String optionRead){
        List<String> name = new ArrayList<>();
        try {
            Scanner iPath = new Scanner(fileFile.toFile());
            while(iPath.hasNextLine()){
                String data = iPath.nextLine();
                if(Objects.equals(optionRead, "date")) {
                    date.add(data);
                }
                if(Objects.equals(optionRead, "name")) {
                    if(Path.of(path + data).toFile().isFile()){
                        name.add(data);
                    }
                }
            }
            iPath.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(Objects.equals(optionRead, "name")){
            int i = 0;
            String currentFile;
            while(i < name.size()){
                currentFile = path + name.get(i);
                if(!Path.of(currentFile).toFile().isDirectory()) {
                    directoryHashMap.put(name.get(i).hashCode(), new Entries(name.get(i), date.get(i)));
                    i += 1;
                }
            }
            treeMap.putAll(directoryHashMap);
        }
    }
}
