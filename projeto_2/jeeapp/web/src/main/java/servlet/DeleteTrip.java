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

@WebServlet("/admin/deleteTrip")
public class DeleteTrip extends HttpServlet {
    private static final long serialVersionUID = 1L;


    @EJB
    private IAdminServices adminServices;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String trip = request.getParameter("trip");

        Long tripId=(long)0;
        try{
            if(trip!=null)tripId = Long.parseLong(trip);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        HttpSession session = request.getSession(false);

        int out = 0;
        if (session != null && session.getAttribute("admin") != null) {
            out = adminServices.deleteTrip((String)session.getAttribute("admin"), tripId);
        }

        request.setAttribute("out", out);

        request.getRequestDispatcher("/admin/DeleteTripInfo.jsp").forward(request, response);

    }
}
