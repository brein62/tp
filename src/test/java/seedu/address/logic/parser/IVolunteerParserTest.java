package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_VOLUNTEER;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.volunteerCommands.VolunteerClearCommand;
import seedu.address.logic.commands.volunteerCommands.VolunteerCreateCommand;
import seedu.address.logic.commands.volunteerCommands.VolunteerDeleteCommand;
import seedu.address.logic.commands.volunteerCommands.VolunteerEditCommand;
import seedu.address.logic.commands.volunteerCommands.VolunteerEditCommand.EditVolunteerDescriptor;
import seedu.address.logic.commands.volunteerCommands.VolunteerFindCommand;
import seedu.address.logic.commands.volunteerCommands.VolunteerListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.volunteer.NameContainsKeywordsPredicate;
import seedu.address.model.volunteer.Volunteer;
import seedu.address.testutil.EditVolunteerDescriptorBuilder;
import seedu.address.testutil.VolunteerBuilder;
import seedu.address.testutil.VolunteerUtil;

public class IVolunteerParserTest {

    private final IVolunteerParser parser = new IVolunteerParser();

    @Test
    public void parseCommand_add() throws Exception {
        Volunteer volunteer = new VolunteerBuilder().build();
        VolunteerCreateCommand command = (VolunteerCreateCommand) parser.parseCommand(VolunteerUtil
                                            .getAddCommand(volunteer));
        assertEquals(new VolunteerCreateCommand(volunteer), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(VolunteerClearCommand.COMMAND_WORD) instanceof VolunteerClearCommand);
        assertTrue(parser.parseCommand(VolunteerClearCommand.COMMAND_WORD + " 3")
                    instanceof VolunteerClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        VolunteerDeleteCommand command = (VolunteerDeleteCommand) parser.parseCommand(
                VolunteerDeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_VOLUNTEER.getOneBased());
        assertEquals(new VolunteerDeleteCommand(INDEX_FIRST_VOLUNTEER), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Volunteer volunteer = new VolunteerBuilder().build();
        EditVolunteerDescriptor descriptor = new EditVolunteerDescriptorBuilder(volunteer).build();
        VolunteerEditCommand command = (VolunteerEditCommand) parser.parseCommand(
                                    VolunteerEditCommand.COMMAND_WORD + " " + INDEX_FIRST_VOLUNTEER.getOneBased()
                                            + " " + VolunteerUtil.getEditVolunteerDescriptorDetails(descriptor));
        assertEquals(new VolunteerEditCommand(INDEX_FIRST_VOLUNTEER, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        VolunteerFindCommand command = (VolunteerFindCommand) parser.parseCommand(
                VolunteerFindCommand.COMMAND_WORD + " " + keywords.stream()
                        .collect(Collectors.joining(" ")));
        assertEquals(new VolunteerFindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(VolunteerListCommand.COMMAND_WORD) instanceof VolunteerListCommand);
        assertTrue(parser.parseCommand(
                VolunteerListCommand.COMMAND_WORD + " 3") instanceof VolunteerListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, ()
                                    -> parser.parseCommand("unknownCommand"));
    }
}
