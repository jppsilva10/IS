package servlet;

import beans.IAdminServices;
import data.TripDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@WebServlet("/admin/listTripsInDate")
public class ListTripsInDate extends HttpServlet {
    private static final long serialVersionUID = 1L;


    @EJB
    private IAdminServices adminServices;

    final Logger logger = LoggerFactory.getLogger(Login.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Date startDate = null;
        Date endDate = null;

        String date = request.getParameter("date");

        logger.debug(request.getParameter("date"));

        request.setAttribute("date", date);

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession(false);

        if(date==null){
            request.getRequestDispatcher("/admin/ListTripsInDate.jsp").forward(request, response);
            return;
        }

        String end = date.substring(0, 8);
        logger.debug(end);

        try {
            end = end + (Integer.parseInt(date.substring(8)) + 1);
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(end);
        } catch (ParseException e) {
        }


        logger.debug("working");

        List<TripDTO> list = null;
        list = adminServices.listTripsByDate((String)session.getAttribute("admin"), startDate, endDate);

        logger.debug("still working");

        if(list == null){
            logger.warn("list is null");
            list = new ArrayList<TripDTO>();
        }

        request.setAttribute("myList", list);
        request.setAttribute("now", new Date());
        logger.debug("success");
        request.getRequestDispatcher("/admin/ListTripsInDate.jsp").forward(request, response);

    }
}
