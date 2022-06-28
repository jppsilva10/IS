package filter;

import beans.IBusinessLogic;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.Filter;
 import javax.servlet.FilterChain;
 import javax.servlet.ServletException;
 import javax.servlet.ServletRequest;
 import javax.servlet.ServletResponse;
 import javax.servlet.annotation.WebFilter;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpSession;

@WebFilter("/secured/*")
public class SecurityFilter implements Filter {

    @EJB
    private IBusinessLogic businessLogic;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        System.out.println("Accessing the filter...");
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession(false);

        if (session != null && session.getAttribute("auth") != null) {
            if(businessLogic.authentication((String) session.getAttribute("auth"))){
                System.out.println("Valid token...");
                chain.doFilter(request, response);
            }
            else{
                System.out.println("Invalid token...");
                request.getRequestDispatcher("/Login.jsp").forward(request, response);
            }
        } else {
            System.out.println("Token not found...");
            request.getRequestDispatcher("/Login.jsp").forward(request, response);
        }
    }
}
