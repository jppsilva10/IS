package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.BusinessLogic;
import beans.IBusinessLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(urlPatterns={"/login", "/secured/login"})
public class Login  extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private IBusinessLogic businessLogic;

    final Logger logger = LoggerFactory.getLogger(Login.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("passsord could not be encrypted!");
            PrintWriter out=response.getWriter();
            out.println(e);
            return;
        }

        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        password = sb.toString();

        String auth = businessLogic.login(email, password);

        System.out.println("user auth "+ auth);

        if(auth==null){
            request.getRequestDispatcher("/Login.jsp").forward(request, response);
            return;
        }

        request.getSession(true).setAttribute("auth", auth);
        request.getRequestDispatcher("/secured/Menu.jsp").forward(request, response);

    }
}
