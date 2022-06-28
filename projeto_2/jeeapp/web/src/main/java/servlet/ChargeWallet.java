package servlet;

import beans.IBusinessLogic;
import data.User;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/secured/chargeWallet")
public class ChargeWallet extends HttpServlet  {
    private static final long serialVersionUID = 1L;


    @EJB
    private IBusinessLogic businessLogic;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String amount = request.getParameter("amount");

        System.out.println(amount);

        float value=0;
        try{
            if(amount!=null)value = Float.parseFloat(amount);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("auth") != null) {
            if(!businessLogic.chargeWallet((String) session.getAttribute("auth"), value)){
                request.getRequestDispatcher("/Login.jsp").forward(request, response);
                return;
            }
        }

        request.setAttribute("amount", value);
        //request.getRequestDispatcher("/secured/chargeWalletPage").forward(request, response);
        request.getRequestDispatcher("/secured/ChargeInfo.jsp").forward(request, response);

    }
}
