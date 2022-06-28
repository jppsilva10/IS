package servlet;

import beans.IBusinessLogic;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/secured/buyTicket")
public class BuyTicket extends HttpServlet {
    private static final long serialVersionUID = 1L;


    @EJB
    private IBusinessLogic businessLogic;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String trip = request.getParameter("trip");
        String place = request.getParameter("place");
        System.out.println(trip);

        Long tripId=(long)0;
        int placeId=0;
        try{
            if(trip!=null)tripId = Long.parseLong(trip);
            if(place!=null) placeId = Integer.parseInt(place);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        HttpSession session = request.getSession(false);

        Long ticketId =(long)0;
        if (session != null && session.getAttribute("auth") != null) {
            try {
                ticketId = businessLogic.purchaseTicket((String) session.getAttribute("auth"), tripId, placeId);
            }catch (Exception e){
                request.setAttribute("ticket", -3);
                request.getRequestDispatcher("/secured/TicketInfo.jsp").forward(request, response);
                return;

            }
        }

        request.setAttribute("ticket", ticketId);
        request.getRequestDispatcher("/secured/TicketInfo.jsp").forward(request, response);

    }
}
