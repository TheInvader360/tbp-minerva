import models.*;
import play.jobs.*;
import play.test.*;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {
		if(Book.count() == 0) {
			Fixtures.loadModels("bootstrap-data.yml");
		}
	}
}
