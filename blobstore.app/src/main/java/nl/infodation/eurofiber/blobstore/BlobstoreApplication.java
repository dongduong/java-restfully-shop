package nl.infodation.eurofiber.blobstore;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import nl.infodation.eurofiber.blobstore.services.BlobstoreServiceImpl;


@ApplicationPath("/blobstore")
public class BlobstoreApplication extends Application {

	@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(BlobstoreServiceImpl.class);
        return classes;
    }
}
