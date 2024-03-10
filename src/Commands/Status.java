package Commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.*;

public class Status {

    //attributes
    private static List<String> directoryMapDatesS = new ArrayList<>();
    private static List<String> directoryMapNameS = new ArrayList<>();
    private static HashMap<Integer,String> directoryHashMapStatus = new HashMap<Integer, String>();
    private static Map<Integer,String> treeMapStatus = new TreeMap<>();
    private static HashMap<Integer,String> directoryMapNameA = new HashMap<Integer, String>();
    private static Map<Integer,String> treeMapAdd = new TreeMap<>();
    private static List<String> directoryMapModify = new ArrayList<>();
    private static List<String> directoryMapNotAdded = new ArrayList<>();
    private static List<String> directoryMapAdded = new ArrayList<>();
    private static Map<Integer,Entries> directoryHashMap = new HashMap<Integer, Entries>();
    private static TreeMap<Integer, Entries> treeMap = new TreeMap<Integer, Entries>();
    private String path;
    private String option;

    //constructor
    public Status (String path1){
        this.path = path1;
    }

    //methode
    public void modificationsStatus(){
        String directoryCommit = path + "\\.pit\\save\\00";
        String directoryAdd = path + "\\.pit\\temp\\directoryMap.txt";
        Path pathDirectoryAdd = Path.of(directoryAdd);
        File fileDirectoryAdd = pathDirectoryAdd.toFile();
        //status before first commit
        if(!(Path.of(directoryCommit).toFile()).exists()){
            //status before first add
            if(!fileDirectoryAdd.exists()){
                option = "notCommitNotAdd";
                initReadFile();
                if(directoryHashMapStatus.isEmpty()){
                    System.out.println("Pit is in a directory with 0 files/directories");
                    return;
                }
                System.out.println("These files were not added for commit: ");
                System.out.println(directoryHashMapStatus.values());
                return;
            }
            //status after add
            option = "notCommit";
            initReadFile();
            treeMapStatus.putAll(directoryHashMapStatus);
            directoryMapNotAdded.addAll(treeMapStatus.values());
            for(Map.Entry<Integer,String> s : directoryMapNameA.entrySet()){
                if(treeMapStatus.containsKey(s.hashCode())){
                    directoryMapAdded.add(s.getValue());
                }
            }
            for(String s : directoryMapAdded){
                directoryMapNotAdded.remove(s);
            }
            if(!directoryMapAdded.isEmpty()){
                System.out.println("These files were added for commit: ");
                System.out.println(directoryHashMapStatus.values());
            }
            if(!directoryMapNotAdded.isEmpty()){
                System.out.println("These files were not added for commit: ");
                System.out.println(directoryHashMapStatus.values());
            }
            return;
        }
        //status after at least first commit
        if(!fileDirectoryAdd.exists()){
            //status before add
            option = "Commit";
            initReadFile();
            createTreeMap();
            for (Map.Entry<Integer,String> s : directoryHashMapStatus.entrySet()) {
                if (!Path.of(path + s.getValue()).toFile().isDirectory()) {
                    if (treeMap.containsKey(s.getKey())) {
                        Entries test = treeMap.get(s.getKey());
                        File fileFile = Path.of(path + s.getValue()).toFile();
                        if (!Objects.equals(test.getDate(), String.valueOf(fileFile.lastModified()))) {
                            directoryMapModify.add(s.getValue());
                        }
                    } else {
                        directoryMapNotAdded.add(s.getValue());
                    }
                }
            }
            if(!directoryMapModify.isEmpty()){
                System.out.println("These files were modify: ");
                System.out.println(directoryMapModify);
            }
            if(!directoryMapNotAdded.isEmpty()){
                System.out.println("These files were not added: ");
                System.out.println(directoryMapNotAdded);
            }
            return;
        }
        //status after at lest first commit and after add
        option = "CommitAndAdd";
        initReadFile();
        createTreeMap();
        treeMapStatus.putAll(directoryHashMapStatus);
        treeMapAdd.putAll(directoryMapNameA);
        for(Map.Entry<Integer,String> s : directoryHashMapStatus.entrySet()){
            if(treeMap.containsKey(s.getKey())){
                Entries test = treeMap.get(s.getKey());
                File fileFile = Path.of(path + s.getValue()).toFile();
                if(!Objects.equals(test.getDate(), String.valueOf(fileFile.lastModified()))){
                    if(treeMapAdd.containsKey(s.getKey())){
                        directoryMapAdded.add(s.getValue());
                    }else{
                        directoryMapModify.add(s.getValue());
                    }
                }
            }else{
                if(treeMapAdd.containsKey(s.getKey())){
                    directoryMapAdded.add(s.getValue());
                }else{
                    directoryMapNotAdded.add(s.getValue());
                }
            }
        }
        if(!directoryMapAdded.isEmpty()){
            System.out.println("These files were added for commit: ");
            System.out.println(directoryMapAdded);
        }
        if(!directoryMapModify.isEmpty()){
            System.out.println("These files were modify but not added for commit: ");
            System.out.println(directoryMapModify);
        }
        if(!directoryMapNotAdded.isEmpty()){
            System.out.println("These files were not added for commit: ");
            System.out.println(directoryMapNotAdded);
        }
        return;
    }

    private void createTreeMap(){
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

    private void initReadFile(){
        if(!(Path.of(path + "\\.pit\\temp\\MapStatus.txt").toFile()).exists()){
            Create.directoryTemp(path,"compare");
        }
        if((Objects.equals(option, "Commit")) || (Objects.equals(option, "CommitAndAdd"))){
            String pathCurrentDirectory = Scan.scanSaveDirectory(path + "\\.pit\\save" , "current");
            String commitMapName = pathCurrentDirectory + "\\.info\\directoryMap.txt";
            readToFile(Path.of(commitMapName),"name");
            String commitMapDates = pathCurrentDirectory + "\\.info\\datesDirectoryMap.txt";
            readToFile(Path.of(commitMapDates),"date");
        }
        if(Objects.equals(option,"notCommit") || (Objects.equals(option, "CommitAndAdd"))){
            String addMapName = path + "\\.pit\\temp\\directoryMap.txt";
            readToFile(Path.of(addMapName),"nameAdd");
        }
        Add obj = new Add(path,".","status");
        obj.directoryAdd();
        String pathVerifyMap = path + "\\.pit\\temp\\MapStatus.txt";
        readToFile(Path.of(pathVerifyMap),"nameTemp");
    }

    private void readToFile(Path pathDirectorySave, String optionRead){
        try {
            Scanner iPath = new Scanner(pathDirectorySave.toFile());
            while (iPath.hasNextLine()) {
                String pathC = iPath.nextLine();
                if(Objects.equals(optionRead, "date")){
                    directoryMapDatesS.add(pathC);
                }else if(Objects.equals(optionRead,"name")){
                    if(Path.of(path + pathC).toFile().isFile()){
                        directoryMapNameS.add(pathC);
                    }
                }else if(Objects.equals(optionRead,"nameTemp")){
                    if(Path.of(path + pathC).toFile().isFile()) {
                        directoryHashMapStatus.put(pathC.hashCode(),pathC);
                    }
                }else if(Objects.equals(optionRead,"nameAdd")){
                    if(Path.of(path + pathC).toFile().isFile()) {
                        directoryMapNameA.put(pathC.hashCode(),pathC);
                    }
                }
            }
            iPath.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
