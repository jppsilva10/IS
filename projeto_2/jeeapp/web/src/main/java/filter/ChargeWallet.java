package filter;

        import beans.IBusinessLogic;
        import data.User;

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

@WebFilter("/secured/ChargeWallet.jsp")
public class ChargeWallet implements Filter {

    @EJB
    private IBusinessLogic businessLogic;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession(false);

        User u = null;

        if (session != null && session.getAttribute("auth") != null) {
            u = businessLogic.getUserByAuthToken((String) session.getAttribute("auth"));
        }

        if(u!=null)request.setAttribute("wallet", u.getWallet());
        else{
            request.getRequestDispatcher("/Login.jsp").forward(request, response);
            return;
        }
        chain.doFilter(request, response);

    }

}