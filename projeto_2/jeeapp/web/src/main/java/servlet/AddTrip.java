package servlet;

import beans.IAdminServices;
import beans.IBusinessLogic;

import javax.ejb.EJB;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@WebServlet("/admin/addTrip")
public class AddTrip  extends HttpServlet {
    private static final long serialVersionUID = 1L;


    @EJB
    private IAdminServices adminServices;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String departure = request.getParameter("departure");
        System.out.println(departure);
        String destination = request.getParameter("destination");
        System.out.println(destination);
        String departureDate = request.getParameter("departureDate");
        System.out.println(departureDate);
        String capacity = request.getParameter("capacity");
        System.out.println(capacity);
        String price = request.getParameter("price");
        System.out.println(price);

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession(false);

        //long departureId=0;
        //long destinationId=0;
        int cap=0;
        float pri=0;
        Date departure_Date=null;
        Calendar departureCal = Calendar.getInstance();
        try {
            if(capacity!=null)cap = Integer.parseInt(capacity);
            if(price!=null)pri = Float.parseFloat(price);
            //if(departure!=null)departureId = Long.parseLong(departure);
            //if(destination!=null)destinationId = Long.parseLong(destination);
            if(departureDate!=null) {
                departureCal.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(departureDate));
                departure_Date = departureCal.getTime();
            }
        } catch (ParseException e) {
        }

        adminServices.addTrip((String)session.getAttribute("admin"), departure, destination, departure_Date, cap, pri);

        String dest= "/admin/Menu.jsp";

        request.getRequestDispatcher(dest).forward(request, response);

    }
}
