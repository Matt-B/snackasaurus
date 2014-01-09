package controllers;

import models.Recipe;
import models.User;
import play.cache.Cache;
import play.libs.Codec;
import play.libs.Images;
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
        List<Recipe> olderRecipes = Recipe.find("order by posted desc").fetch(3);
        if(olderRecipes.size() != 0)
          olderRecipes.remove(0);
        render(frontRecipe, olderRecipes);
    }

    public static void registerUser() {
        String randomID = Codec.UUID();
        render(randomID);
    }

    public static void saveUser(String email, String password, String name, String code, String randomID) {
        validation.equals(code, Cache.get(randomID)).message("Invalid code, please try again.");
        if(validation.hasErrors()) {
            render("Application/registerUser.html", randomID);
        }
        User user = new User(email, password, name);
        user.save();

        Cache.delete(randomID);

        render("Application/registerUserSuccess.html", name);
    }

    public static void show(Long id) {
        Recipe recipe = Recipe.findById(id);
        render(recipe);
    }

    public static void help() {
        render();
    }

    public static void postComment(Long recipeId, String content) {
        User user = User.find("byEmail", Security.connected()).first();
        Recipe recipe = Recipe.findById(recipeId);
        recipe.addComment(user, content);
        show(recipeId);
    }

    public static void captcha(String id) {
        Images.Captcha captcha = Images.captcha();
        String code = captcha.getText("#000000");
        Cache.set(id, code, "10mn");
        renderBinary(captcha);
    }

    public static void search(String searchQuery) {
        if(searchQuery != null && !searchQuery.isEmpty()) {
            List<Recipe> searchResults = Recipe.find("byTitleIlike", "%"+searchQuery+"%").fetch();
            render(searchQuery, searchResults);
        } else {
            render(searchQuery);
        }
    }

}