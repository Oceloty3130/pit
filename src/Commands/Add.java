package Commands;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

public class Add {

    //attributes
    private String path;
    private String option = ".";
    private String optionStatus = "add";

    //constructor
    public Add(String path, String option,String optionStatus){
        this.path = path;
        this.option = option;
        this.optionStatus = optionStatus;
    }

    public Add(String path){
        this.path = path;
    }

    //methods
    public void directoryAdd(){
        String ver;
        String directorySaveName;
        if(Objects.equals(optionStatus,"status")){
            directorySaveName = path + "\\.pit\\temp\\MapStatus.txt";
        }else{
            directorySaveName = path + "\\.pit\\temp\\directoryMap.txt";
        }
        Path pathOption = Path.of(option);
        File fileOption = pathOption.toFile();
        Scan objScan = new Scan(path, directorySaveName);
        if(!(Path.of(path + "\\.pit\\temp\\directoryMap.txt").toFile()).exists()){
            Create.directoryTemp(path,null);
        }
        if(Objects.equals(option, ".")){
            //call function scan which give full directory
            objScan.createDirectoryMap();
            return;
        }
        if(fileOption.isDirectory()){
            ver = "path";
        }else{
            ver = "file";
        }
        //search for a path or file
        objScan.scanPathOrFile(option,ver);
    }

}
