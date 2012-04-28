import models.User;
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
        if(User.count() == 0) {
            Fixtures.loadModels("../test/data.yml");
        }
    }


}
