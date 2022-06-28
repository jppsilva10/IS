package filter;

import beans.IAdminServices;
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

@WebFilter("/admin/Top5.jsp")
public class Top5 implements Filter {

    @EJB
    private IAdminServices adminServices;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession(false);

        List<User> list = new ArrayList<User>();
        list = adminServices.Top5();



        request.setAttribute("top_5", list);
        chain.doFilter(request, response);
    }

}