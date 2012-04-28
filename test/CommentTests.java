import models.Comment;
import models.Recipe;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CommentTests extends UnitTest {

    @Before
    public void setup() {
        Fixtures.deleteDatabase();
    }

    @Test
    public void createAndRetrieveComment() {
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
        Recipe recipe = new Recipe(trex, "Marmite on Toast", ingredients, steps).save();

        //Create and save a new comment
        new Comment(trex, recipe, "I prefer humans.").save();

        //Retrieve the comment by user
        Comment comment = Comment.find("byRecipe", recipe).first();

        //Test that the comment has been retrieved and matches the one we persisted
        assertNotNull(comment);
        assertEquals(comment.content, "I prefer humans.");
    }
}
