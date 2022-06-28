package beans;

import data.*;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public interface IBusinessLogic {
    public boolean addUser(String username, String email, String password);

    public User getUserByAuthToken(String authToken);

    public boolean editInfo(String username, String email, String password , String auth);

    public boolean deleteUser(String auth);

    public String login(String email, String password);

    public boolean authentication(String authToken);

    public boolean chargeWallet(String authToken, float amount);

    public List<Place> listPlaces(String auth);

    public List<TripDTO> listTripsByDate(String auth, Date start, Date end);

    public List<Integer> listTripPlaces(String auth, long id);

    public long purchaseTicket(String auth, long tripId, int place);

    public boolean returnTicket(String auth, long id);

    public List<TripDTO> listTripsByUser(String auth);

    public List<TicketDTO> listTicketsByUser(String auth);

}
