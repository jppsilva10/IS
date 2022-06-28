package servlet;

import beans.IBusinessLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@WebServlet(urlPatterns={"/logout", "/secured/logout"})
public class Logout extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private IBusinessLogic businessLogic;

    final Logger logger = LoggerFactory.getLogger(Logout.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getSession(true).removeAttribute("auth");
        request.getRequestDispatcher("/").forward(request, response);
    }
}
