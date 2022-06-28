package filter;

import beans.IAdminServices;
import beans.IBusinessLogic;
import data.Place;
import data.Student;
import data.User;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebFilter("/admin/AddTrip.jsp")
public class AddTrip implements Filter {

    @EJB
    private IAdminServices adminServices;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //List<Place> list = adminServices.listPlaces();
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession(false);

        List<String> list = adminServices.listPlaces((String)session.getAttribute("admin")).stream().
                map(Place::getName).collect(Collectors.toList());

        request.setAttribute("places", list);
        chain.doFilter(request, response);

    }

}