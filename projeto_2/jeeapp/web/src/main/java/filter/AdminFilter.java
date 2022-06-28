package filter;

import beans.IAdminServices;
import beans.IBusinessLogic;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @EJB
    private IAdminServices adminServices;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        System.out.println("Accessing the filter...");


        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession(false);
        String path = httpReq.getRequestURI().substring(httpReq.getContextPath().length()).replaceAll("[/]+$", "");

        System.out.println(path);
        if(path.equals("/admin/login")){
            chain.doFilter(request, response);
        }

        if (session != null && session.getAttribute("admin") != null) {
            if(adminServices.authentication((String)session.getAttribute("admin"))){
                System.out.println("Verified authentication token...");
                chain.doFilter(request, response);
            }
            else{
                System.out.println("admin not authenticated...");
                request.getRequestDispatcher("/admin/Login.jsp").forward(request, response);
            }
        } else {
            System.out.println("admin not authenticated...");
            request.getRequestDispatcher("/admin/Login.jsp").forward(request, response);
        }
    }

}
