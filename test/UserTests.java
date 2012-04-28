import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class UserTests extends UnitTest {

    @Before
    public void setup() {
        Fixtures.deleteDatabase();
    }

    @Test
    public void createAndRetrieveUser() {
        // Create a new user and save it
        new User("trex@snacksaurus.com", "password", "T. Rex").save();

        // Retrieve the user by e-mail address
        User tRex = User.find("byName", "T. Rex").first();

        // Test that the user object is not null and test it has the correct name attribute
        assertNotNull(tRex);
        assertEquals("T. Rex", tRex.name);
    }

}
