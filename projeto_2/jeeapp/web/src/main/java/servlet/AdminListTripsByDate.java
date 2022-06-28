package servlet;

import beans.IAdminServices;
import beans.IBusinessLogic;
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

@WebServlet("/admin/listTripsByDate")
public class AdminListTripsByDate extends HttpServlet {
    private static final long serialVersionUID = 1L;


    @EJB
    private IAdminServices adminServices;

    final Logger logger = LoggerFactory.getLogger(Login.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession(false);

        Date startDate = null;
        Calendar startCal = Calendar.getInstance();
        Date endDate = null;
        Calendar endCal = Calendar.getInstance();

        String start = request.getParameter("start");
        String end = request.getParameter("end");

        logger.debug(request.getParameter("start"));

        request.setAttribute("start", start);
        request.setAttribute("end", end);

        try {
            if(start!=null) startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(start);
            if(end!=null) endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(end);
        } catch (ParseException e) {
        }

        if(start==null || end ==null){
            request.getRequestDispatcher("/admin/ListTripsByDate.jsp").forward(request, response);
        }

        logger.debug("working");

        List<TripDTO> list = null;
        if(start!=null && end!=null) list = adminServices.listTripsByDate((String)session.getAttribute("admin"), startDate, endDate);

        logger.debug("still working");

        if(list == null){
            logger.warn("list is null");
            list = new ArrayList<TripDTO>();
        }

        request.setAttribute("myList", list);
        request.setAttribute("now", new Date());
        logger.debug("success");
        request.getRequestDispatcher("/admin/ListTripsByDate.jsp").forward(request, response);

    }
}
