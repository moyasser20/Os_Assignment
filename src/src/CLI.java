import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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
                "touch <filename> - Create a new file\n" +
                "rm <filename> - Remove a file\n" +
                "mv <source> <destination> - Move or rename a file\n" +
                "cat <filename> - Display the content of a file\n" +
                "exit - Exit the CLI\n" +
                "help - Show this help message\n";
    }

    // touch command to create a new file
    public boolean touch(String filename) {
        File file = new File(currentDirectory, filename);
        try {
            if (file.createNewFile()) {
                return true;
            } else {
                System.out.println("touch: file already exists: " + filename);
                return false;
            }
        } catch (IOException e) {
            System.out.println("touch: error creating file: " + filename);
            return false;
        }
    }

    // rm command to remove a file
    public boolean rm(String filename) {
        File file = new File(currentDirectory, filename);
        if (file.exists()) {
            return file.delete();
        } else {
            System.out.println("rm: no such file: " + filename);
            return false;
        }
    }

    // mv command to move or rename a file
    public boolean mv(String source, String destination) {
        File srcFile = new File(currentDirectory, source);
        File destFile = new File(currentDirectory, destination);
        if (!srcFile.exists()) {
            System.out.println("mv: no such file: " + source);
            return false;
        }
        return srcFile.renameTo(destFile);
    }

    // cat command to display the content of a file
    public String cat(String filename) {
        File file = new File(currentDirectory, filename);
        if (!file.exists()) {
            return "cat: no such file: " + filename;
        }
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            return "cat: error reading file: " + filename;
        }
        return content.toString();
    }

    // Main method to run the CLI
    public static void main(String[] args) {
        CLI cli = new CLI();
        Scanner scanner = new Scanner(System.in);
        String command;

        System.out.println("Welcome to the CLI! Type 'help' for available commands.");

        while (true) {
            System.out.print(cli.pwd() + " $ ");
            command = scanner.nextLine();
            String[] parts = command.split(" ");

            switch (parts[0]) {
                case "pwd":
                    System.out.println(cli.pwd());
                    break;
                case "cd":
                    if (parts.length > 1) {
                        cli.cd(parts[1]);
                    } else {
                        System.out.println("cd: missing argument");
                    }
                    break;
                case "ls":
                    boolean all = false;
                    boolean recursive = false;
                    if (parts.length > 1) {
                        for (int i = 1; i < parts.length; i++) {
                            if ("-a".equals(parts[i])) {
                                all = true;
                            } else if ("-r".equals(parts[i])) {
                                recursive = true;
                            }
                        }
                    }
                    System.out.println(cli.ls(all, recursive));
                    break;
                case "mkdir":
                    if (parts.length > 1) {
                        cli.mkdir(parts[1]);
                    } else {
                        System.out.println("mkdir: missing argument");
                    }
                    break;
                case "rmdir":
                    if (parts.length > 1) {
                        cli.rmdir(parts[1]);
                    } else {
                        System.out.println("rmdir: missing argument");
                    }
                    break;
                case "touch":
                    if (parts.length > 1) {
                        cli.touch(parts[1]);
                    } else {
                        System.out.println("touch: missing argument");
                    }
                    break;
                case "rm":
                    if (parts.length > 1) {
                        cli.rm(parts[1]);
                    } else {
                        System.out.println("rm: missing argument");
                    }
                    break;
                case "mv":
                    if (parts.length == 3) {
                        cli.mv(parts[1], parts[2]);
                    } else {
                        System.out.println("mv: missing arguments");
                    }
                    break;
                case "cat":
                    if (parts.length > 1) {
                        System.out.println(cli.cat(parts[1]));
                    } else {
                        System.out.println("cat: missing argument");
                    }
                    break;
                case "exit":
                    cli.exit();
                    break;
                case "help":
                    System.out.println(cli.help());
                    break;
                default:
                    System.out.println("Invalid command. Type 'help' for available commands.");
                    break;
            }
        }
    }
}

