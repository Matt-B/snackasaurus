package models;

import play.db.jpa.Model;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User class
 * Model for users of the application
 * User: matt
 * Date: 4/17/12
 */
@Entity
@Table(name="snackasaurus_users")
public class User extends Model {

    public String email;
    public String password;
    public String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static User connect(String email, String password) {
        return find("byEmailAndPassword", email, password).first();
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

}
