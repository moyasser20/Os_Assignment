import java.io.File;
import java.util.Scanner;

public class CLI {
    private File currentDirectory;

    public CLI() {
        this.currentDirectory = new File(System.getProperty("user.dir"));
    }

    public String pwd() {
        return currentDirectory.getAbsolutePath();
    }

    public boolean cd(String path) {
        File newDirectory = new File(path);
        if (newDirectory.exists() && newDirectory.isDirectory()) {
            currentDirectory = newDirectory;
            return true;
        } else {
            System.out.println("cd: no such file or directory: " + path);
            return false;
        }
    }

    public String ls(boolean all, boolean recursive) {
        return listDirectoryContents(currentDirectory, all, recursive);
    }

    private String listDirectoryContents(File dir, boolean all, boolean recursive) {
        StringBuilder output = new StringBuilder();
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (all || !file.isHidden()) {
                    output.append(file.getName()).append("\n");
                    if (recursive && file.isDirectory()) {
                        output.append(listDirectoryContents(file, all, true));
                    }
                }
            }
        }
        return output.toString();
    }

    public boolean mkdir(String dirName) {
        File dir = new File(currentDirectory, dirName);
        if (dir.exists()) {
            System.out.println("mkdir: directory already exists: " + dirName);
            return false;
        }
        return dir.mkdir();
    }

    public boolean rmdir(String dirName) {
        File dir = new File(currentDirectory, dirName);
        if (dir.exists() && dir.isDirectory()) {
            return dir.delete();
        } else {
            System.out.println("rmdir: no such directory: " + dirName);
            return false;
        }
    }

    public void exit() {
        System.out.println("Exiting CLI...");
        System.exit(0);
    }

    public String help() {
        return "Available commands:\n" +
                "pwd - Display current directory\n" +
                "cd <directory> - Change directory\n" +
                "ls [-a] [-r] - List contents of directory\n" +
                "mkdir <directory> - Create new directory\n" +
                "rmdir <directory> - Remove directory\n" +
                "exit - Exit the CLI\n" +
                "help - Show this help message\n";
    }

    public static void main(String[] args) {
        CLI cli = new CLI();
        Scanner scanner = new Scanner(System.in);
        String command;

        System.out.println("Welcome to the CLI. Type 'help' to see the available commands.");

        while (true) {
            System.out.print(cli.pwd() + "> "); // Print the current directory
            command = scanner.nextLine().trim(); // Read user input

            // Parse the command and its arguments
            String[] parts = command.split(" ");
            String cmd = parts[0];
            String arg = parts.length > 1 ? parts[1] : null;

            switch (cmd) {
                case "pwd":
                    System.out.println(cli.pwd());
                    break;
                case "cd":
                    if (arg != null) {
                        cli.cd(arg);
                    } else {
                        System.out.println("cd: missing argument");
                    }
                    break;
                case "ls":
                    boolean all = false;
                    boolean recursive = false;
                    if (arg != null && arg.equals("-a")) {
                        all = true;
                    } else if (arg != null && arg.equals("-r")) {
                        recursive = true;
                    }
                    System.out.println(cli.ls(all, recursive));
                    break;
                case "mkdir":
                    if (arg != null) {
                        cli.mkdir(arg);
                    } else {
                        System.out.println("mkdir: missing directory name");
                    }
                    break;
                case "rmdir":
                    if (arg != null) {
                        cli.rmdir(arg);
                    } else {
                        System.out.println("rmdir: missing directory name");
                    }
                    break;
                case "exit":
                    cli.exit();
                    break;
                case "help":
                    System.out.println(cli.help());
                    break;
                default:
                    System.out.println("Unknown command: " + cmd);
            }
        }
    }
}
