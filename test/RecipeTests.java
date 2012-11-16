import models.Recipe;
import models.User;
import org.junit.*;
import play.test.Fixtures;
import play.test.UnitTest;

import java.util.*;

public class RecipeTests extends UnitTest {

    @Before
    public void setup() {
        Fixtures.deleteDatabase();
    }

    @Test
    public void createAndRetrieveRecipe() {

        //Create a new user to be the author
        User trex = new User("trex@snacksaurus.com", "password", "T. Rex").save();

        //Create ingredients and steps
        List<String> ingredients = new ArrayList<String>();
        ingredients.add("1 piece Bread");
        ingredients.add("20g Marmite");
        String steps = ("Toast Bread. Spread Marmite on toasted bread");

        //Create and save the new recipe
        new Recipe(trex, "Marmite on Toast", ingredients, steps).save();

        //Retrieve the recipe by title
        Recipe marmiteOnToast = Recipe.find("byTitle", "Marmite on Toast").first();

        //Assert the recipe has been retrieved and that the author is the same as the one we saved
        assertNotNull(marmiteOnToast);
        assertEquals(trex, marmiteOnToast.author);
    }

}
