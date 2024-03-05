package Commands;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Status {

    //attributes
    private String path;
    private String option;
    private String directoryScan;
    private String directorySaveDate;

    public Status (String path1, String option1){
        this.path = path1;
        this.option = option1;
//        this.directoryScan = directoryScan1;
//        this.directorySaveDate = directorySaveDate1;
    }

    public void modificationsStatus(){
        String pathCurrentDirectory = Scan.scanSaveDirectory(path + "\\.pit\\save","current");
        directorySaveDate = pathCurrentDirectory + "\\.info";
        String directorySaveName = path + "\\.pit\\temp\\directoryMap.txt";
        String directorySaveDates = path+"\\.pit\\temp\\datesDirectoryMap.txt";
        String directoryModification= path+"\\.pit\\temp\\datesModification.txt";
        Path directorySaveDatesP = Path.of(directorySaveDates);
        try {
            Files.createFile(Paths.get(directorySaveName));
            Files.createFile(directorySaveDatesP);
            Files.createFile(Path.of(directoryModification));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scan obj = new Scan(path, directorySaveName);
        obj.createDirectoryMap();
        try {
            Scanner iDateTemp = new Scanner(directorySaveDatesP.toFile());
            Path a = Path.of(directorySaveDate + "datesDirectoryMap.txt");
            Scanner iDateSave = new Scanner(a.toFile());
            while(iDateTemp.hasNextLine() && iDateSave.hasNextLine()){
                String dataTemp = iDateTemp.nextLine();
                String dataSave = iDateSave.nextLine();
                //String dataName =
                if(dataTemp != dataSave){
                    //verificare date dintre 2 fisiere
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
