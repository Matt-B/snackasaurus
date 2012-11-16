import models.Comment;
import models.Recipe;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

import java.util.*;

public class CommentTests extends UnitTest {

    @Before
    public void setup() {
        Fixtures.deleteDatabase();
    }

    @Test
    public void createAndRetrieveComment() {
        //Create a new user to be the author
        User trex = new User("trex@snacksaurus.com", "password", "T. Rex").save();

        List<String> ingredients = new ArrayList<String>();
        ingredients.add("1 piece Bread");
        ingredients.add("20g Marmite");
        String steps = ("Toast Bread. Spread Marmite on toasted bread");

        //Create and save the new recipe
        new Recipe(trex, "Marmite on Toast", ingredients, steps).save();

        //Retrieve the recipe by title
        Recipe recipe = Recipe.find("byTitle", "Marmite on Toast").first();

        //Create and save a new comment
        new Comment(trex, recipe, "I prefer humans.").save();

        //Retrieve the comment by user
        Comment comment = Comment.find("byRecipe", recipe).first();

        //Test that the comment has been retrieved and matches the one we persisted
        assertNotNull(comment);
        assertEquals(comment.content, "I prefer humans.");
    }
}
