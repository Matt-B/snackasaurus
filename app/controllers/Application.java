package controllers;

import models.Recipe;
import models.User;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import play.cache.Cache;
import play.libs.Codec;
import play.libs.Images;
import play.libs.Mail;
import play.mvc.Before;
import play.mvc.Controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
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
        List<Recipe> firstThreeRecipes = Recipe.find("order by posted desc").fetch(3);
        render(firstThreeRecipes);
    }

    public static void registerUser() {
        String randomID = Codec.UUID();
        render(randomID);
    }

    public static void recoverUser() {
        String randomID = Codec.UUID();
        render(randomID);
    }

    public static void resetPassword(String token) {
        String code = URLDecoder.decode(request.querystring);
        String[] array = code.split("//");
        String email = array[0];
        if(Cache.get(array[0]).equals(array[1]))
            render(email);
        else
            Controller.notFound();
    }

    public static void changePassword(String emailAddress, String newPassword) {
        User user = User.find("byEmail", emailAddress).first();
        user.changePassword(newPassword);
        index();
    }

    public static void passwordReset(String email, String code, String randomID) {
        validation.equals(code, Cache.get(randomID)).message("Invalid code, please try again.");
        if(validation.hasErrors()) {
            render("Application/recoverUser.html", randomID);
        }
        if(User.find("byEmail", email).first() != null) {
            sendReminderEmail(email);
            Cache.delete(randomID);
            index();
        } else {
            Cache.delete(randomID);
            index();
        }

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

    private static void sendReminderEmail(String emailAddress) {
        String randomUUID = Codec.UUID();
        Cache.set(emailAddress, randomUUID, "30mn");
        String recoverId = URLEncoder.encode(emailAddress + "//" + randomUUID);
        try {
            SimpleEmail email = new SimpleEmail();
            email.setFrom("sender@dinosaursareawesome.co.uk");
            email.addTo(emailAddress);
            email.setSubject("snackasaurus password reset");
            email.setMsg("Is this thing on? http://localhost:9000/user/resetPassword?" + recoverId);
            Mail.send(email);
        } catch (EmailException e) {
            System.out.println("Error - "+ e.getStackTrace().toString());
            Controller.error();
        }
    }

}