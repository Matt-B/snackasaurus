package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        Recipe frontRecipe = Recipe.find("order by posted desc").first();
        List<Recipe> olderRecipes = Recipe.find("order by posted desc").from(1).fetch(4);
        render(frontRecipe, olderRecipes);
    }

    public static void show(Long id) {
        Recipe recipe = Recipe.findById(id);
        render(recipe);
    }

    public static void postComment(Long recipeId, Long authorId, String content) {
        User user = User.findById(authorId);
        Recipe recipe = Recipe.findById(recipeId);
        recipe.addComment(user, content);
        show(recipeId);
    }

}