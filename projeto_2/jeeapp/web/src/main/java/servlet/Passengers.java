package servlet;

import beans.IAdminServices;
import beans.IBusinessLogic;
import data.UserDTO;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/admin/passengers")
public class Passengers extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private IAdminServices adminServices;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String trip = request.getParameter("trip");



        System.out.println(trip);

        Long tripId=(long)0;
        try{
            if(trip!=null)tripId = Long.parseLong(trip);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        HttpSession session = request.getSession(false);

        List<UserDTO> list = null;

        if (session != null && session.getAttribute("admin") != null) {
            list = adminServices.listPassengers((String)session.getAttribute("admin"), tripId);
        }
        if(list ==null){
            list = new ArrayList<UserDTO>();
        }

        for(int i =0; i<list.size(); i++){
            System.out.println("email: "+list.get(i).getEmail());
        }

        request.setAttribute("users", list);
        request.getRequestDispatcher("/admin/Passengers.jsp").forward(request, response);

    }

}