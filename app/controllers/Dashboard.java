package controllers;

import models.Recipe;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

import java.util.ArrayList;
import java.util.List;

@With(Secure.class)
public class Dashboard extends Controller {

    @Before
    static void setConnectedUser() {
        if(Security.isConnected()) {
            User user = User.find("byEmail", Security.connected()).first();
            renderArgs.put("user", user.name);
            renderArgs.put("email", user.email);
        }
    }

    public static void index() {
        String user = Security.connected();
        List<Recipe> recipes = Recipe.find("author.email", user).fetch();
        render(recipes);
    }

    public static void form() {
        render();
    }

    public static void save(String title, String ingredients, String steps) {
        System.out.println(steps);
        User author = User.find("byEmail", Security.connected()).first();
        List<String> ingredientList = new ArrayList<String>();
        for(String ingredient : ingredients.split(",")) {
            ingredientList.add(ingredient);
        }
        Recipe recipe = new Recipe(author, title, ingredientList, steps);

        validation.valid(recipe);
        if(validation.hasErrors()) {
            render("@form", recipe);
        }

        recipe.save();
        index();
    }

}
