package Commands;

import java.util.Objects;

public class Commands {

    //attributes
    private String path;
    private String option = null;

    //constructor
    public Commands(String option, String path){
        this.path = path;
        this.option = option;
    }

    //methods
    public void Create(){
        Create.init(path);
    }

    public void Commit(){
        Commit objCommit = new Commit(path,option);
        objCommit.saveDirectory();
    }

    public void Add(){
        if(!Objects.equals(option, ".")){
            Add objAdd = new Add(path,option);
            objAdd.directoryAdd();
        }else{
            Add objAdd = new Add(path);
            objAdd.directoryAdd();
        }

    }

//    public void Satus(){
//        Status obj = new Status(path,"dates",);
//    }

    public void setPath(String path) {
        this.path = path;
    }
}
