package Commands;

import java.io.File;
import java.nio.file.Path;

public class Add {

    //attributes
    private String path;
    private String option = ".";

    //constructor
    public Add(String path, String option){
        this.path = path;
        this.option = option;
    }

    public Add(String path){
        this.path = path;
    }

    //methods
    public void directoryAdd(){
        String ver;
        String directorySaveName = path + "\\.pit\\temp\\directoryMap.txt";
        Path pathOption = Path.of(option);
        File fileOption = pathOption.toFile();
        if(!(Path.of(path + "\\.pit\\temp").toFile()).exists()){
            Create.directoryTemp(path,"name");
        }
        if(fileOption.isDirectory()){
            ver = "path";
        }else{
            ver = "file";
        }
        Scan objScan = new Scan(path, "\\.pit", directorySaveName, option);
        if(option == "."){
            //call function scan which give full directory
            objScan.createDirectoryMap();
        }else if(option != "."){
            //search for a path or file
            objScan.scanPathOrFile(option,ver);
        }
    }
}
