package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.*;

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
    @OneToMany(mappedBy="recipe", cascade=CascadeType.ALL)
    public List<Comment> comments;

    public Recipe(User author, String title, Map<String, String> ingredients, Map<Integer, String> steps) {
        this.author = author;
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.posted = new Date();
    }

    public Recipe addComment(User author, String content) {
        Comment comment = new Comment(author, this, content);
        this.comments.add(comment);
        this.save();
        return this;
    }

    public Recipe previous() {
        return Recipe.find("posted < ? order by posted desc", posted).first();
    }

    public Recipe next() {
        return Recipe.find("posted > ? order by posted asc", posted).first();
    }

}
