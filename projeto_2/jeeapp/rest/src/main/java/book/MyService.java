package book;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import beans.IAdminServices;
import beans.IBusinessLogic;
import data.Student;

@RequestScoped
@Path("/myservice")
@Produces(MediaType.APPLICATION_JSON)
public class MyService {

    @EJB
    private IBusinessLogic businessLogic;

    @GET
    @Path("/adduser")
    public String addUser() {
        System.out.println("add user....");
        businessLogic.addUser("xxx", "xxx", "xxx");

        return "success";
    }

    @GET
    @Path("/login/{email}/{pass}")
    public String login(@PathParam("email") String email, @PathParam("pass") String password) {
        System.out.println("login....");
        return businessLogic.login(email, password);

    }

    @GET
    @Path("/getuser/{auth}")
    public String getUser(@PathParam("auth") String auth) {
        System.out.println("getuser....");
        businessLogic.getUserByAuthToken(auth);

        return "success";
    }

    @GET
    @Path("/auth/{auth}")
    public String auth(@PathParam("auth") String auth) {
        System.out.println("getuser....");
        businessLogic.authentication(auth);

        return "success";
    }

    @EJB
    private IAdminServices adminServices;

    @GET
    @Path("/addadmin/{username}/{password}")
    public String auth(@PathParam("username") String username, @PathParam("password") String password) {
        System.out.println("add admin....");

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "error";
        }

        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        password = sb.toString();

        adminServices.addAdmin(username, password);

        return "success";
    }

}
