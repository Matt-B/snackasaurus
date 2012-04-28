package models;

import play.db.jpa.Model;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Recipe class
 * Model for recipe objects
 * User: matt
 * Date: 4/17/12
 */
@Entity
public class Recipe extends Model {

    public String title;
    public Date posted;
    @ManyToOne
    public User author;
    @ElementCollection
    public Map<String, String> ingredients;
    @ElementCollection
    public Map<Integer, String> steps;

    public Recipe(User author, String title, Map<String, String> ingredients, Map<Integer, String> steps) {
        this.author = author;
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.posted = new Date();
    }

}
