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

@WebServlet("/secured/returnTicket")
public class ReturnTicket extends HttpServlet {
    private static final long serialVersionUID = 1L;


    @EJB
    private IBusinessLogic businessLogic;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String ticket = request.getParameter("ticket");

        Long ticketId=(long)0;
        try{
            if(ticket!=null)ticketId = Long.parseLong(ticket);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        HttpSession session = request.getSession(false);
        boolean out = false;
        if (session != null && session.getAttribute("auth") != null) {
            out = businessLogic.returnTicket((String) session.getAttribute("auth"), ticketId);
        }

        request.setAttribute("out", out);
        request.getRequestDispatcher("/secured/ReturnTicketInfo.jsp").forward(request, response);

    }
}
