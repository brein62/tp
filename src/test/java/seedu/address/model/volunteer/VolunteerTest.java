package seedu.address.model.volunteer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalVolunteers.ALICE;
import static seedu.address.testutil.TypicalVolunteers.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.VolunteerBuilder;

public class VolunteerTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Volunteer volunteer = new VolunteerBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> volunteer.getSkills().remove(0));
    }

    @Test
    public void isSameVolunteer() {
        // same object -> returns true
        assertTrue(ALICE.isSameVolunteer(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameVolunteer(null));

        // same name, all other attributes different -> returns true
        Volunteer editedAlice = new VolunteerBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withSkills(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameVolunteer(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new VolunteerBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameVolunteer(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Volunteer editedBob = new VolunteerBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSameVolunteer(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new VolunteerBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSameVolunteer(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Volunteer aliceCopy = new VolunteerBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different volunteer -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Volunteer editedAlice = new VolunteerBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new VolunteerBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new VolunteerBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new VolunteerBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new VolunteerBuilder(ALICE).withSkills(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Volunteer.class.getCanonicalName() + "{name=" + ALICE.getName()
                            + ", phone=" + ALICE.getPhone() + ", email=" + ALICE.getEmail() + ", address="
                            + ALICE.getAddress() + ", skills=" + ALICE.getSkills() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
