import Commands.Commands;

public class Main {
    public static void main(String[] args) {
        //System.getProperty("user.dir")
        String option = "";
        String path = "";
        if(args.length == 0){
            System.out.println("Enter a command");
        } else if (args.length == 1) {
            path = System.getProperty("user.dir");
            option = ".";
        }else if(args.length == 2){
            option = args[1];
            path = System.getProperty("user.dir");
        } else if (args.length == 3) {
            option = args[1];
            path = args[2];
        }
        Commands obj = new Commands(option, path);
        System.out.println(path);
        switch (args[0]) {
            case "init" -> obj.Create();
            case "add" -> obj.Add();
            case "commit" -> obj.Commit();
            case "status" -> obj.Status();
            default -> {
                System.out.println("init - initialize the repository");
                System.out.println("add - prepare the files and directories to be committed");
                System.out.println("commit - commit files/directories added");
                System.out.println("status - shows the status of files that are in add or were not included in commit");
            }
        }
    }
}