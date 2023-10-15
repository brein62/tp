package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalEvents.getTypicalEventStorage;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalVolunteers.getTypicalVolunteerStorage;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.volunteerCommands.VolunteerClearCommand;
import seedu.address.logic.commands.volunteerCommands.VolunteerEditCommand;
import seedu.address.logic.commands.volunteerCommands.VolunteerEditCommand.EditPersonDescriptor;
import seedu.address.model.EventStorage;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.VolunteerStorage;
import seedu.address.model.volunteer.Volunteer;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class VolunteerEditCommandTest {

    private Model model = new ModelManager(getTypicalEventStorage(), getTypicalVolunteerStorage(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Volunteer editedVolunteer = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedVolunteer).build();
        VolunteerEditCommand volunteerEditCommand = new VolunteerEditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(VolunteerEditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                                                                        Messages.format(editedVolunteer));

        Model expectedModel = new ModelManager(new EventStorage(model.getEventStorage()),
                                                new VolunteerStorage(model.getVolunteerStorage()), new UserPrefs());
        expectedModel.setVolunteer(model.getFilteredVolunteerList().get(0), editedVolunteer);

        assertCommandSuccess(volunteerEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredVolunteerList().size());
        Volunteer lastVolunteer = model.getFilteredVolunteerList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastVolunteer);
        Volunteer editedVolunteer = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        VolunteerEditCommand volunteerEditCommand = new VolunteerEditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(VolunteerEditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                                                                        Messages.format(editedVolunteer));

        Model expectedModel = new ModelManager(new EventStorage(model.getEventStorage()),
                                                new VolunteerStorage(model.getVolunteerStorage()), new UserPrefs());
        expectedModel.setVolunteer(lastVolunteer, editedVolunteer);

        assertCommandSuccess(volunteerEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        VolunteerEditCommand volunteerEditCommand = new VolunteerEditCommand(INDEX_FIRST_PERSON,
                                                                        new EditPersonDescriptor());
        Volunteer editedVolunteer = model.getFilteredVolunteerList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(VolunteerEditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                                                                    Messages.format(editedVolunteer));

        Model expectedModel = new ModelManager(new EventStorage(model.getEventStorage()),
                                                new VolunteerStorage(model.getVolunteerStorage()), new UserPrefs());

        assertCommandSuccess(volunteerEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Volunteer volunteerInFilteredList = model.getFilteredVolunteerList().get(INDEX_FIRST_PERSON.getZeroBased());
        Volunteer editedVolunteer = new PersonBuilder(volunteerInFilteredList).withName(VALID_NAME_BOB).build();
        VolunteerEditCommand volunteerEditCommand = new VolunteerEditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(VolunteerEditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                                                                    Messages.format(editedVolunteer));

        Model expectedModel = new ModelManager(new EventStorage(model.getEventStorage()),
                                                new VolunteerStorage(model.getVolunteerStorage()), new UserPrefs());
        expectedModel.setVolunteer(model.getFilteredVolunteerList().get(0), editedVolunteer);

        assertCommandSuccess(volunteerEditCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Volunteer firstVolunteer = model.getFilteredVolunteerList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstVolunteer).build();
        VolunteerEditCommand volunteerEditCommand = new VolunteerEditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(volunteerEditCommand, model, VolunteerEditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Volunteer volunteerInList = model.getVolunteerStorage().getVolunteerList()
                                    .get(INDEX_SECOND_PERSON.getZeroBased());
        VolunteerEditCommand volunteerEditCommand = new VolunteerEditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(volunteerInList).build());

        assertCommandFailure(volunteerEditCommand, model, VolunteerEditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredVolunteerList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        VolunteerEditCommand volunteerEditCommand = new VolunteerEditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(volunteerEditCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getVolunteerStorage().getVolunteerList().size());

        VolunteerEditCommand volunteerEditCommand = new VolunteerEditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(volunteerEditCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final VolunteerEditCommand standardCommand = new VolunteerEditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        VolunteerEditCommand commandWithSameValues = new VolunteerEditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new VolunteerClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new VolunteerEditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new VolunteerEditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        VolunteerEditCommand volunteerEditCommand = new VolunteerEditCommand(index, editPersonDescriptor);
        String expected = VolunteerEditCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, volunteerEditCommand.toString());
    }

}
