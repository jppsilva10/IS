package servlet;

import beans.IAdminServices;
import beans.IBusinessLogic;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/addPlace")
public class AddPlace extends HttpServlet {
    private static final long serialVersionUID = 1L;


    @EJB
    private IAdminServices adminServices;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String name = request.getParameter("name");
        String destination = "/admin/Menu.jsp";

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession(false);

        if(name!=null) adminServices.addPlace((String)session.getAttribute("admin"), name);

        request.getRequestDispatcher(destination).forward(request, response);

    }
}
