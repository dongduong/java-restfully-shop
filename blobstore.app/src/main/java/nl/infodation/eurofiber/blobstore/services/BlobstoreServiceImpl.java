package nl.infodation.eurofiber.blobstore.services;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Session Bean implementation class BlobstoreServiceImpl
 * @author dongduong
 */
@Stateless
@LocalBean
@Path("/services")
public class BlobstoreServiceImpl {

	@GET
	@Produces("text/plain")
	public String heathChecker() {
		return "Heath Checker !";
	}

}
