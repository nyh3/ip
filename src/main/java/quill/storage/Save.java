package quill.storage;

import quill.exception.QuillException;
import quill.task.*;

import java.io.*;
import java.util.Scanner;

/**
 * The Save Class handles the reading from and writing
 * task data to a file as well as creating a new file.
 */
public class Save {

    /**
     * Creates a memory file at the specified file path.
     */
    private static void createNewFile() {
        File file = new File("./data/quill.txt");
        File directory = new File(file.getParent());
        try {
            if (!directory.exists()) {
                if (directory.mkdirs()) {
                    System.out.println("Directory created: " + directory.getAbsolutePath());
                } else {
                    System.out.println("Failed to create directory: " + directory.getAbsolutePath());
                }
            }
            if (file.createNewFile()) {
                System.out.println("New file created: " + file.getAbsolutePath());
            } else {
                System.out.println("Failed to create new file: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error creating new file: " + e.getMessage());
        }
    }

    /**
     * Reads tasks from a specified file and returns them as a TaskList of Task objects.
     *
     * @return A TaskList of Task of objects read from the file.
     */
    public static TaskList readFile() {
        TaskList tasks = new TaskList();
        File file = new File("./data/quill.txt");
        if (!file.exists()) {
            createNewFile();
            return tasks;
        }
        try {
            Scanner s = new Scanner(file);
            while (s.hasNext()) {
                String line = s.nextLine();
                Task task = getTask(line);
                tasks.addTask(task);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error" + e.getMessage());
        } catch (QuillException e) {
            System.out.println("QuillException while reading file");
        }
        return tasks;
    }

    /**
     * Reformat a string as a Task object.
     *
     * @param line The string to be reformatted.
     * @return The Task object containing the reformatted string.
     * @throws QuillException If the string is of invalid format.
     */
    private static Task getTask(String line) throws QuillException {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            throw new QuillException();
        }
        String taskType = parts[0].trim();
        boolean isDone = Boolean.parseBoolean(parts[1].trim());
        String taskDetails = parts[2].trim();
        Task task;
        switch (taskType) {
        case "T":
            task = new Todo(taskDetails);
            break;
        case "D":
            task = new Deadline(taskDetails);
            break;
        case "E":
            task = new Event(taskDetails);
            break;
        default:
            task = new Task("Error");
            break;
        }
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        return task;
    }

    /**
     * Write the TaskList into the specified file.
     *
     * @param tasks The TaskList to be written into the file.
     */
    public static void writeToFile(TaskList tasks) {
        try {
            PrintWriter fw = new PrintWriter("./data/quill.txt");
            for (int i = 0; i < TaskList.getTotalTasks(); i++) {
                fw.println(tasks.getTask(i).saveTask());
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Error writing file" + e.getMessage());
        }
    }

}
