package mjk.ws.rs;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath(value = "/ra")
public class RESTApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set < Class <? > > set = new HashSet < Class <? > >();
		set.add(ShipmentResource.class);
		set.add(NotFoundExceptionMapper.class); // adding custom exception mapper (or actually wrapper)
		return set;
	}

}
