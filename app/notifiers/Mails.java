package notifiers;

import models.User;
import play.cache.Cache;
import play.libs.Codec;
import play.mvc.Mailer;
import play.mvc.Router;

import java.net.URLEncoder;

public class Mails extends Mailer {

    public static void resetPassword(User user, String recoveryUrl) {
        String randomUUID = Codec.UUID();
        Cache.set(user.email, randomUUID, "30mn");
        String recoveryUrl = Router.getFullUrl("Application.resetPassword")
                + "?"
                + URLEncoder.encode(user.email + "//" + randomUUID);
        setFrom("snackasaurus <do-not-reply@dinosaursareawesome.co.uk>");
        setSubject("How to reset your snackasaurus password.");
        addRecipient(user.email);
        send(user, recoveryUrl);
    }
}
