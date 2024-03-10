package Commands;

import java.util.Objects;

public class Commands {

    //attributes
    private String path = null;
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
        Commit objCommit = new Commit(path);
        objCommit.saveDirectory();
    }

    public void Add(){
        if(!Objects.equals(option, ".")){
            Add objAdd = new Add(path,option,"add");
            objAdd.directoryAdd();
        }else{
            Add objAdd = new Add(path);
            objAdd.directoryAdd();
        }

    }

    public void Status(){
        Status obj = new Status(path);
        obj.modificationsStatus();
    }
}
