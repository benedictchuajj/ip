/**
 * The Task Class is a representation of a task that Duke is keeping track of.
 *
 * It contains information relating to the task:
 * - description
 * - isDone
 *
 * @author Benedict Chua
 */
public class Task {
    private String description;
    private boolean isDone;

    /**
     * Constructor for a Task.
     *
     * @param description The name of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status of whether the task has been completed.
     *
     * @return "X" if is completed, " " if not completed
     */
    public String getStatusIcon() {
        return (this.isDone ? "X" : " ");
    }

    /**
     * Checks if task is done and marks task as done if not already completed
     *
     * @return description of task if it has been marked as completed, else message that task is already done previously.
     */
    public String markTaskAsDone() {
        if (!this.isDone) {
            this.isDone = true;
            return "  " + this.toString();
        } else {
            return "Wait...  You've already completed this task before you dummy!";
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", getStatusIcon(), this.description);
    }
}