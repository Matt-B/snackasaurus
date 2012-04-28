package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        Recipe frontRecipe = Recipe.find("order by posted desc").first();
        render(frontRecipe);
    }

}