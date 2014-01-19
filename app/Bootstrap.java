import models.User;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

/**
 * Loads data from YAML file upon start
 * User: matt
 * Date: 4/28/12
 */
@OnApplicationStart
public class Bootstrap extends Job {

    public void doJob() {
        if(Play.id.equals("dev")) {
            Fixtures.deleteDatabase();
            Fixtures.loadModels("../test/recipeCatalog.yml");
        }
        if(Play.id.equals("prod") && User.count() == 0) {
            Fixtures.deleteDatabase();
            Fixtures.loadModels("../test/recipeCatalog.yml");
        }
    }


}
