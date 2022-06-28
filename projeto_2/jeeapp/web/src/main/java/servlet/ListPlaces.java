package servlet;

import beans.IBusinessLogic;
import data.User;

import javax.ejb.EJB;
import javax.print.DocFlavor;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/secured/listPlaces")
public class ListPlaces extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private IBusinessLogic businessLogic;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String trip = request.getParameter("trip");

        System.out.println(trip);

        Long tripId=(long)0;
        try{
            if(trip!=null)tripId = Long.parseLong(trip);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        HttpSession session = request.getSession(false);

        List<Integer> list = new ArrayList<Integer>();

        if (session != null && session.getAttribute("auth") != null) {
            list = businessLogic.listTripPlaces((String) session.getAttribute("auth"), tripId);
        }

        System.out.println("places: ");
        for(int i=0; i<list.size(); i++){
            System.out.println(list.get(i));
        }

        request.setAttribute("trip", tripId);
        request.setAttribute("places", list);
        request.getRequestDispatcher("/secured/BuyTicket.jsp").forward(request, response);

    }

}