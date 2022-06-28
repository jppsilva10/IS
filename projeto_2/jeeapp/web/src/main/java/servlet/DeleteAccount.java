package servlet;

import beans.IBusinessLogic;
import data.User;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/secured/delete")
public class DeleteAccount extends HttpServlet  {
    private static final long serialVersionUID = 1L;


    @EJB
    private IBusinessLogic businessLogic;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("auth") != null) {
            if(!businessLogic.deleteUser((String) session.getAttribute("auth"))){
                request.getRequestDispatcher("/Login.jsp").forward(request, response);
                return;
            }

        }
        else{
            request.getRequestDispatcher("/Login.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("/").forward(request, response);

    }
}
