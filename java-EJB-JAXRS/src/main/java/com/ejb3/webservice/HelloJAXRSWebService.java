package com.ejb3.webservice;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Session Bean implementation class HelloJAXRSWebService
 */
@Stateless
@LocalBean
@Path("/hellows")
public class HelloJAXRSWebService {

    /**
     * Default constructor. 
     */
    public HelloJAXRSWebService() {
        // TODO Auto-generated constructor stub
    }
    
    @GET
    @Produces("text/plain")
    public String sayHello() {
        return "Hello";
    }

}
