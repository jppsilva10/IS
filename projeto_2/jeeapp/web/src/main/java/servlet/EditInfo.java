package servlet;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.IBusinessLogic;

@WebServlet("/secured/editInfo")
public class EditInfo  extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private IBusinessLogic businessLogic;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        String Newusername = request.getParameter("New_username");
        String Newemail = request.getParameter("New_email");
        String Newpassword = request.getParameter("New_password");


        if(session != null && session.getAttribute("auth") != null){
            if(!businessLogic.editInfo(Newusername, Newemail, Newpassword, (String)session.getAttribute("auth"))){
                request.getRequestDispatcher("Login.jsp").forward(request, response);
                return;
            }
        }
        else{
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("/secured/UpdateInfo.jsp").forward(request, response);

    }

}
