package duke.util;

import duke.command.*;
import duke.exception.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;

import static java.lang.Integer.parseInt;

/**
 * Class to deal with making sense of the user's inputted command.
 *
 * @author Benedict Chua
 */
public class Parser {
    private TaskList taskList;

    public Parser(TaskList taskList) {
        this.taskList = taskList;
    }

    /**
     * Returns the Command based on the first keyword from the full command (input) given by the user,.
     * Command will contain relevant info from the rest of the input if applicable.
     *
     * @param input the String that the user enters into Duke.
     * @return the corresponding Command based from the input.
     */
    public Command getCommand(String input) {
        switch (input.split(" ")[0]) {
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand(this.taskList, null);
        case "check":
            return new ListCommand(this.taskList, filterTaskDescription(input));
        case "done":
            return new DoneCommand(this.taskList, filterTaskIndex(input));
        case "todo":
            return new AddCommand(this.taskList, filterTaskDescription(input), "ToDo");
        case "deadline":
            return new AddCommand(this.taskList, filterTaskDescription(input), "Deadline");
        case "event":
            return new AddCommand(this.taskList, filterTaskDescription(input), "Event");
        case "delete":
            return new DeleteCommand(this.taskList, filterTaskIndex(input));
        default:
            return new InvalidCommand(input);
        }
    }

    /**
     * Takes in the original inputted command and filters it for the task description.
     * The description is the command string minus the first word which is the command itself.
     *
     * @param command the original command inputted by the user.
     * @return the filtered String that contains only the description of the task.
     * @throws EmptyDescriptionException when description is missing (including if it contains only white space).
     */
    public static String filterTaskDescription(String command) throws EmptyDescriptionException {
        String[] commandItems = command.split(" ", 2);
        if (commandItems.length == 1) {
            throw new EmptyDescriptionException();
        }
        String filteredDescription  = command.split(" ", 2)[1];
        if (filteredDescription.trim().isEmpty()) {
            throw new EmptyDescriptionException();
        }
        return filteredDescription.trim();
    }

    /**
     * Takes in the original inputted command and filters and parses it for the given integer index.
     * The index is the full command string minus the first word which is the command itself.
     *
     * @param command the original command inputted by the user.
     * @return the filtered and parsed int that refers to the index of the task.
     * @throws MissingIndexException when index is missing (including if it contains only white space).
     */
    public static int filterTaskIndex(String command) throws MissingIndexException {
        String[] commandItems = command.split(" ", 2);
        if (commandItems.length == 1) {
            throw new MissingIndexException();
        }
        String indexString  = command.split(" ", 2)[1];
        if (indexString.trim().isEmpty()) {
            throw new MissingIndexException();
        }
        return parseInt(indexString);
    }

    /**
     * Takes in a date in the form of a string and parses it to return a LocalDate object.
     * The date is recognised if given in the form dd/mm/yyyy, dd-mm-yyyy or yyyy-mm-dd.
     *
     * @param dateString String containing the date in the form dd/mm/yyyy, dd-mm-yyyy or yyyy-mm-dd.
     * @return LocalDate that is parsed from the given dateString.
     * @throws InvalidDateInputException if date is in the wrong form or an invalid date is given.
     */
    public static LocalDate parseDate(String dateString) throws InvalidDateInputException {
        try {
            if (dateString.matches("\\d{2}-\\d{2}-\\d{4}")) {
                // in the form dd-mm-yyyy
                String[] d = dateString.split("-", 3);
                return LocalDate.parse(String.format("%s-%s-%s", d[2], d[1], d[0]));
            } else if (dateString.matches("\\d{2}/\\d{2}/\\d{4}")) {
                // in the form dd/mm/yyyy
                String[] d = dateString.split("/", 3);
                return LocalDate.parse(String.format("%s-%s-%s", d[2], d[1], d[0]));
            } else if (dateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
                // in the form yyyy-mm-dd
                return LocalDate.parse(dateString);
            } else {
                throw new InvalidDateInputException(dateString);
            }
        } catch (DateTimeException e) {
            throw new InvalidDateInputException(dateString);
        }
    }

    /**
     * Takes in a time in the form of a string and parses it to return a LocalTime object.
     * The time is recognised if it is in the 24-hour format and given in the form hhmm or hh:mm.
     *
     * @param timeString String containing the time (in 24hr format) in the form hhmm or hh:mm.
     * @return LocalTime that is parsed from the given timeString.
     * @throws InvalidTimeInputException if time is in the wrong form or an invalid time is given.
     */
    public static LocalTime parseTime(String timeString) throws InvalidTimeInputException {
        try {
            if (timeString.indexOf(':')  == -1 && timeString.length() == 4) {
                // in the form hhmm
                String[] t = {timeString.substring(0, 2), timeString.substring(2)};
                return LocalTime.parse(String.format("%s:%s", t[0], t[1]));
            } else if (timeString.indexOf(':')  == 2 && timeString.length() == 5) {
                //in the form hh:mm
                return LocalTime.parse(timeString);
            } else {
                // Maybe throw exception
                throw new InvalidTimeInputException(timeString);
            }
        } catch (DateTimeException e) {
            throw new InvalidTimeInputException(timeString);
        }
    }

    /**
     * Parses the actual task description and date & time given from a full Deadline task description.
     *
     * @param task full description for the given Deadline task.
     * @return String[] containing the actual task description and date & time
     * @throws MissingArgumentException if description is missing the /by argument
     */
    public static String[] parseDeadlineDate(String task) throws MissingArgumentException {
        String[] deadlineDetails = task.split(" /by ");
        if (deadlineDetails.length == 1) {
            throw new MissingArgumentException("'/by'", "Deadline");
        }

        return deadlineDetails;
    }

    /**
     * Parses the actual task description and date & time given from a full Event task description.
     *
     * @param task full description for the given Eveent task.
     * @return String[] containing the actual task description and date & time
     * @throws MissingArgumentException if description is missing the /at argument
     */
    public static String[] parseEventDate(String task) throws MissingArgumentException {
        String[] eventDetails = task.split(" /at ");
        if (eventDetails.length == 1) {
            throw new MissingArgumentException("'/at'", "Event");
        }

        return eventDetails;
    }
}