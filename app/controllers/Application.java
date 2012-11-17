package controllers;

import models.Recipe;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;

import java.util.List;

public class Application extends Controller {

    @Before
    static void setConnectedUser() {
        if(Security.isConnected()) {
            User user = User.find("byEmail", Security.connected()).first();
            renderArgs.put("user", user.name);
            renderArgs.put("email", user.email);
        }
    }

    public static void index() {
        Recipe frontRecipe = Recipe.find("order by posted desc").first();
        List<Recipe> olderRecipes = Recipe.find("order by posted desc").fetch(2);
        render(frontRecipe, olderRecipes);
    }

    public static void show(Long id) {
        Recipe recipe = Recipe.findById(id);
        render(recipe);
    }

    public static void postComment(Long recipeId, String content) {
        User user = User.find("byEmail", Security.connected()).first();
        Recipe recipe = Recipe.findById(recipeId);
        recipe.addComment(user, content);
        show(recipeId);
    }

}