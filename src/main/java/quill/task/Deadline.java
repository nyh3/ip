package quill.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import quill.exception.QuillException;

/**
 * The Deadline class represents a task of type "Deadline" in the Quill application.
 * It extends the Task class and includes specific behavior for deadline tasks.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    /**
     * Constructs a Deadline task with the specified task description and completion date.
     *
     * @param input The description of the event task including completion date.
     * @throws QuillException If the task description is empty.
     * @throws DateTimeParseException If the format completion date is incorrect.
     */
    public Deadline(String input) throws QuillException, DateTimeParseException {
        super(input);
        int index = input.indexOf("/by");
        this.description = input.substring(0, index);
        if (this.description.isEmpty()) {
            throw new QuillException("No empty descriptions allowed for deadline. Fill it in!");
        }
        this.by = LocalDateTime.parse(input.substring(index + 3)
                        .replace('T', ' '),
                        DateTimeFormatter.ofPattern(" yyyy-MM-dd HH:mm"));
    }

    /**
     * Returns a string representation of the deadline task for printing.
     *
     * @return A string representation of the deadline task.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a");
        String formattedBy = by.format(formatter);
        return "[D]" + super.toString() + "(by: " + formattedBy + ")";
    }

    /**
     * Returns a string representation of the deadline task for storing.
     *
     * @return A string representation of the deadline task.
     */
    @Override
    public String saveTask() {
        return "D | " + super.saveTask() + "/by " + by;
    }

}
