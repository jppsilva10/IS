package filter;

import beans.IBusinessLogic;
import data.TicketDTO;
import data.Trip;
import data.TripDTO;
import data.User;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebFilter("/secured/ListUserTrips.jsp")
public class ListUserTrips implements Filter {

    @EJB
    private IBusinessLogic businessLogic;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        /*
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession(false);

        List<TripDTO> list = new ArrayList<TripDTO>();
        if (session != null && session.getAttribute("auth") != null)
            list = businessLogic.listTripsByUser((String) session.getAttribute("auth"));

        request.setAttribute("trips", list);
        request.setAttribute("now",new Date() );
        chain.doFilter(request, response);


         */

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession(false);

        List<TicketDTO> list = new ArrayList<TicketDTO>();
        if (session != null && session.getAttribute("auth") != null)
            list = businessLogic.listTicketsByUser((String) session.getAttribute("auth"));

        request.setAttribute("trips", list);
        request.setAttribute("now",new Date() );
        chain.doFilter(request, response);
    }

}