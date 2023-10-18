package seedu.address.logic.commands.eventcommands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BUDGET;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE_AND_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MATERIALS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;

/**
 * Adds an event to the EventStorage.
 */
public class EventCreateCommand extends Command {

    public static final String COMMAND_WORD = "ecreate";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an event to the event list. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_ROLE + "ROLE "
            + PREFIX_DATE_AND_TIME + "DATE AND TIME "
            + PREFIX_LOCATION + "LOCATION "
            + PREFIX_DESCRIPTION + "DESCRIPTION "
            + "[" + PREFIX_MATERIALS + "MATERIALS]...\n"
            + "[" + PREFIX_BUDGET + "BUDGET]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Clean up at Orchard "
            + PREFIX_ROLE + "Cleaner "
            + PREFIX_ROLE + "Manager "
            + PREFIX_DATE_AND_TIME + "23/10/2023 1500 "
            + PREFIX_LOCATION + "Orchard Road "
            + PREFIX_DESCRIPTION + "Cleaning up Orchard Road! "
            + PREFIX_MATERIALS + "Trash bag "
            + PREFIX_MATERIALS + "TONGS "
            + PREFIX_BUDGET + "50.00";
    public static final String MESSAGE_SUCCESS = "New EVENT added: %1$s";
    public static final String MESSAGE_DUPLICATE_EVENT = "This event already exists in the event list";

    private final Event toAdd;

    /**
     * Creates an EventCreateCommand to add the specified {@code Event}
     */
    public EventCreateCommand(Event event) {
        requireNonNull(event);
        toAdd = event;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasEvent(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_EVENT);
        }

        model.addEvent(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EventCreateCommand)) {
            return false;
        }

        EventCreateCommand otherEventCreateCommand = (EventCreateCommand) other;
        return toAdd.equals(otherEventCreateCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}