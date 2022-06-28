package beans;
import data.*;

import java.util.Date;
import java.util.List;

public interface IAdminServices {
    public void addAdmin(String username, String password);

    public String login(String username, String password);

    public boolean addPlace(String auth, String name);

    public List<Place> listPlaces(String auth);

    public Place getPlaceByName( String name);

    public boolean addTrip(String auth, String departure, String destination, Date departureDate, int capacity, float price);

    public List<TripDTO> listTripsByDate(String auth, Date start, Date end);

    public int deleteTrip(String auth, long id);

    public List<UserDTO> listPassengers(String auth, long id);

    public float getRevenue(String auth, Date start, Date end);

    public boolean authentication(String authToken);

    public List<User> Top5();
}
