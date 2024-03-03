import Commands.Commands;

public class Main {
    public static void main(String[] args) {
        //System.getProperty("user.dir")
        String option = new String();
        String path = new String();
        if(args.length == 0){
            System.out.println("Enter a command");
        } else if (args.length == 1) {
            path = "C:\\Users\\carpp\\OneDrive\\Desktop\\Programe";
            option = ".";
        }else if(args.length == 2){
            option = args[1];
            path = "C:\\Users\\carpp\\OneDrive\\Desktop\\Programe";
        } else if (args.length == 3) {
            option = args[1];
            path = args[2];
        }
        Commands obj = new Commands(option, path);
        switch (args[0]){
            case "init":
                obj.Create();
                break;
            case "add":
                obj.Add();
                break;
            case "commit":
                obj.Commit();
                break;
        }
    }
}