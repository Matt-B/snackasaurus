package models;

import org.mindrot.jbcrypt.BCrypt;
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
    public String hashedPassword;
    public String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());;
        this.name = name;
    }

    public static User connect(String email, String password) {
        User user = find("byEmail", email).first();
        if(BCrypt.checkpw(password, user.hashedPassword))
            return user;
        else
            return null;
    }

    public void changePassword(String newPassword) {
        this.hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    }

}
