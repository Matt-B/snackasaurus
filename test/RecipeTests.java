import models.Recipe;
import models.User;
import org.junit.*;
import play.test.Fixtures;
import play.test.UnitTest;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
        Map<String, String> ingredients = new HashMap<String, String>();
        ingredients.put("1 piece", "Bread");
        ingredients.put("20g", "Marmite");
        Map<Integer, String> steps = new TreeMap<Integer, String>();
        steps.put(1, "Toast Bread.");
        steps.put(2, "Spread Marmite on toasted bread.");

        //Create and save the new recipe
        new Recipe(trex, "Marmite on Toast", ingredients, steps).save();

        //Retrieve the recipe by title
        Recipe marmiteOnToast = Recipe.find("byTitle", "Marmite on Toast").first();

        //Assert the recipe has been retrieved and that the author is the same as the one we saved
        assertNotNull(marmiteOnToast);
        assertEquals(trex, marmiteOnToast.author);
    }

}
