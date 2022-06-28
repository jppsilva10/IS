package servlet;

import beans.IAdminServices;
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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@WebServlet("/admin/login")
public class AdminLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;


    @EJB
    private IAdminServices adminServices;

    final Logger logger = LoggerFactory.getLogger(AdminLogin.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
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

        String admin = adminServices.login(username, password);

        System.out.println("user auth "+ admin);

        if(admin==null){
            request.getRequestDispatcher("/admin/Login.jsp").forward(request, response);
            return;
        }

        request.getSession(true).setAttribute("admin", admin);
        request.getRequestDispatcher("/admin/Menu.jsp").forward(request, response);

    }
}
