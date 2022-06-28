package servlet;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import data.Trip;
import data.TripDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/secured/listTripsByDate")
public class ListTripsByDate extends HttpServlet {
    private static final long serialVersionUID = 1L;


    @EJB
    private IBusinessLogic businessLogic;

    final Logger logger = LoggerFactory.getLogger(Login.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);

        Date startDate = null;
        Calendar startCal = Calendar.getInstance();
        Date endDate = null;
        Calendar endCal = Calendar.getInstance();

        String start = request.getParameter("start");
        String end = request.getParameter("end");

        System.out.println(request.getParameter("start"));
        try {
            if(start!=null) startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(start);
            if(end!=null) endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(end);
        } catch (ParseException e) {
        }
        request.setAttribute("start", start);
        request.setAttribute("end", end);

        if(start==null || end ==null){
            request.getRequestDispatcher("/secured/ListTripsByDate.jsp").forward(request, response);
        }

        List<TripDTO> list = null;

        if(start!=null && end!=null){
            if(session != null && session.getAttribute("auth") != null) {
                list = businessLogic.listTripsByDate((String) session.getAttribute("auth"), startDate, endDate);
            }
        }

        if(list == null){
            logger.warn("list is null");
            list = new ArrayList<TripDTO>();
        }

        request.setAttribute("myList", list);
        request.getRequestDispatcher("/secured/ListTripsByDate.jsp").forward(request, response);

    }
}
