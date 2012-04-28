package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Comment class
 * Model for comment objects
 * User: matt
 * Date: 4/28/12
 */
@Entity
public class Comment extends Model {

    public Date postedAt;
    @ManyToOne
    public User author;
    @ManyToOne
    public Recipe recipe;
    @Lob
    public String content;

    public Comment(User author, Recipe recipe, String content) {
        this.postedAt = new Date();
        this.author = author;
        this.recipe = recipe;
        this.content = content;
    }
}
