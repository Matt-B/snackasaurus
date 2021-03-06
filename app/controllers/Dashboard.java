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

    public static void form(Long id) {
        if(id != null) {
            Recipe recipe = Recipe.findById(id);
            render(recipe);
        }
        render();
    }

    public static void save(Long id, String title, String ingredients, String steps) {
        Recipe recipe;
        if(id == null) {
            User author = User.find("byEmail", Security.connected()).first();
            List<String> ingredientList = new ArrayList<String>();
            for(String ingredient : ingredients.split(",")) {
                ingredientList.add(ingredient);
            }
            recipe = new Recipe(author, title, ingredientList, steps);
        } else {
            recipe = Recipe.findById(id);
            recipe.title = title;
            recipe.steps = steps;
            recipe.ingredients.clear();
            for(String ingredient : ingredients.split(",")) {
                recipe.ingredients.add(ingredient);
            }
        }

        validation.valid(recipe);
        if(validation.hasErrors()) {
            render("@form", recipe);
        }

        recipe.save();
        index();
    }

    public static void delete(Long id) {
        Recipe recipe = Recipe.findById(id);
        recipe.delete();
        index();
    }

}
