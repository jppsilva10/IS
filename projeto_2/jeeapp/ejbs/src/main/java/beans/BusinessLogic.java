package beans;

import data.*;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.*;
import javax.validation.constraints.Null;
import java.lang.reflect.Executable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class BusinessLogic implements IBusinessLogic {
    @PersistenceContext(unitName = "BusCompany")
    EntityManager em;

    final Logger logger = LoggerFactory.getLogger(BusinessLogic.class);
    //final Logger logger = LoggerFactory.getLogger("org.jboss.as.config");

    @Transactional
    public boolean addUser(String username, String email, String password) {
        logger.debug("addUser(username: " + username + ", email: " + email + ", password: " + password + ")");
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(password);
        try {
            em.persist(u);
        }catch (Exception e){
            logger.warn(e.getMessage());
            logger.warn("Cant add" + u.toString());
            return false;
        }
        logger.debug( u.toString() + "added");
        return true;
    }

    public User getUserByAuthToken(String authToken) {
        if(authToken==null){
            logger.error("getUserByAuthToken(authToken: " + authToken + ")");
            logger.error("authToken should not be null!");
            return null;
        }
        logger.debug("getUserByAuthToken(authToken: " + authToken + ")");

        String query = "FROM User u " +
                "WHERE u.authToken = :authToken";

        TypedQuery<User> q = em.createQuery(query, User.class);
        q.setParameter("authToken", authToken);

        User u = null;
        try {
            u = q.getSingleResult();
        }catch (Exception e){
            logger.warn(e.getMessage());
            logger.warn("User{authToken= '" + authToken + "'} not found");

            return null;
        }
        if(u==null){
            logger.debug("User{authToken= '" + authToken + "'} not found");
            return null;
        }
        logger.debug("found " + u.toString());
        return u;
    }

    @Transactional
    public boolean editInfo(String username, String email, String password , String auth) {
        if(username==null || email==null || password == null || auth == null){
            logger.error("editInfo(username: " + username + ", email: " + email + ", password: " + password + ", auth: " + auth + ")");
            logger.error("username, email, password and auth should not be null!");
            return false;
        }
        logger.debug("editInfo(username: " + username + ", email: " + email + ", password: " + password + ", auth: " + auth + ")");

        User u = getUserByAuthToken(auth);
        if(u==null){
            logger.warn("User{authToken= '" + auth + "'} not authenticated");
            return false;
        }
        u.setPassword(password);
        u.setEmail(email);
        u.setUsername(username);
        if(u==null){
            logger.warn("User{authToken= '" + auth + "'} not authenticated");
            return false;
        }
        logger.debug(u.toString() + " updated");
        return true;
    }

    @Transactional
    public boolean deleteUser(String auth) {
        if( auth == null){
            logger.error("deleteUser(auth: " + auth + ")");
            logger.error("auth should not be null!");
            return false;
        }
        logger.debug("deleteUser(auth: " + auth + ")");

        User u = getUserByAuthToken(auth);
        if (u == null){
            logger.warn("User{authToken= '" + auth + "'} not authenticated");
            return false;
        }

        em.remove(u);
        logger.debug(u.toString() + " deleted");
        return true;
    }

    @Transactional
    public String login(String email, String password) {
        if(email==null || password == null){
            logger.error("login(email: " + email + ", password: " + password + ")");
            logger.error("email and password should not be null!");
            return null;
        }
        logger.debug("login(email: " + email + ", password: " + password + ")");

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Authentication Token could not be created!");
            return null;
        }


        String token = email + password + new Random().nextInt(1000000);
        md.update(token.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        token = sb.toString();
        logger.debug("Token created: " + token);

        String query = "FROM User u " +
                "WHERE u.email = :email AND u.password = :password";


        TypedQuery<User> q = em.createQuery(query, User.class);
        q.setParameter("email", email);
        q.setParameter("password", password);

        User u = null;
        try {
            u = q.getSingleResult();
        }catch (Exception e){
            logger.warn(e.getMessage());
            logger.warn("User{email= '" + email + ", password= " + password + "'} not found");
            return null;
        }
        if (u != null){
            u.setAuthToken(token);
        }
        else {
            logger.debug("User{email= '" + email + ", password= " + password + "'} not found");
            return null;
        }
        logger.debug(u.toString() + " authenticated");
        return token;
    }

    public boolean authentication(String authToken) {
        logger.debug("authentication(authToken: " + authToken + ")");
        if(getUserByAuthToken(authToken) != null){
            logger.debug("User{authToken= '" + authToken + "'} authenticated");
            return true;
        }

        logger.debug("User{authToken= '" + authToken + "'} not authenticated");
        return false;
    }

    @Transactional
    public boolean chargeWallet(String authToken, float amount) {
        if (amount <= 0){
            logger.error("chargeWallet(authToken: " + authToken + ", amount: " + amount + ")");
            logger.error("amount should not be 0 or lower!");
            return false;
        }
        logger.debug("chargeWallet(authToken: " + authToken + ", amount: " + amount + ")");
        User u = getUserByAuthToken(authToken);

        if (u != null){
            u.setWallet(u.getWallet() + amount);
            logger.debug(u.toString() + " updated");
            return true;
        }
        logger.warn("User{authToken= '" + authToken + "'} not authenticated");
        return false;

    }

    public List<Place> listPlaces(String auth){
        logger.debug("listPlaces()");

        if(!authentication(auth)){
            return null;
        }

        TypedQuery<Place> q = em.createQuery("FROM Place p", Place.class);
        List<Place> list = null;
        try {
            list = q.getResultList();
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
        return list;
    }

    private Trip getTripById(long id){
        if(id<0){
            logger.error("getTripById(id: " + id + ")");
            logger.error("id should not be lower than 0!");
            return null;
        }
        logger.debug("getTripById(id: " + id + ")");
        TypedQuery<Trip> q = em.createQuery("FROM Trip t WHERE t.id = :id", Trip.class);
        q.setParameter("id", id);
        Trip t = null;
        try{
            t = q.getSingleResult();
        }catch (Exception e){
            logger.error(e.getMessage());
            logger.error("trip{id= "+ id+ "} dont exist!");
            return null;
        }
        return t;
    }

    /*
    public Trip getAvailableTripById(long id){
        TypedQuery<Trip> q = em.createQuery("SELECT t.* FROM Trip t, (SELECT COUNT(*) AS c FROM Ticket ti WHERE ti.trip_id = :id1) AS tick WHERE t.id = :id2 AND t.capacity > tick.c", Trip.class);
        q.setParameter("id1", id);
        q.setParameter("id2", id);
        Trip t = q.getSingleResult();
        System.out.println("trips: \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println(t);
        return t;
    }

     */


    private Trip getAvailableTripById(long id){
        if(id<0){
            logger.error("getAvailableTripById(id: " + id + ")");
            logger.error("id should not be lower than 0!");
            return null;
        }
        Trip t = getTripById(id);
        if(t == null){
            logger.error("trip{id= "+ id + "} not found!");
            return null;
        }
        if(t.getTickets().size()<t.getCapacity()){
            logger.debug(t + "is still available");
            return t;
        }
        logger.debug(t + "is not available");
        return null;
    }


    @Transactional
    public long purchaseTicket(String auth, long tripId, int place){
        if(auth == null){
            logger.error("purchaseTiket(auth: " + auth + ", tripId: " + tripId + ", place: " + place + ")");
            logger.error("auth should not be null!");
            return -1;
        }
        if(tripId<0){
            logger.error("purchaseTiket(auth: " + auth + ", tripId: " + tripId + ", place: " + place + ")");
            logger.error("tripId should not be lower than 0!");
            return -1;
        }
        if(place<1){
            logger.error("purchaseTiket(auth: " + auth + ", tripId: " + tripId + ", place: " + place + ")");
            logger.error("place should not be lower than 1!");
        }
        logger.debug("purchaseTiket(auth: " + auth + ", tripId: " + tripId + ", place: " + place + ")");

        User u = getUserByAuthToken(auth);
        if(u==null){
            logger.error("user{auth= " + auth + "} not found!");
            return -1;
        }
        Trip t = getAvailableTripById(tripId);
        if(t==null){
            logger.warn("trip{id= " + tripId + "} not available");
            return -1;
        }
        if(u.getWallet()<t.getPrice()){
            logger.warn(u.toString() + "dont have enough money");
            return -2;
        }

        u.setWallet(u.getWallet()-t.getPrice());
        Ticket tick = new Ticket();

        tick.setPlace(place);
        tick.setTrip_id(t);
        tick.setUser_id(u);
        u.getTickets().add(tick);
        t.getTickets().add(tick);

        try {
            em.persist(tick);
        }catch (Exception e){
            logger.error("cant add " + tick.toString());
            return -3;
        }
        logger.debug(tick.toString() + " added");
        return tick.getId();
    }

    @Transactional
    public boolean returnTicket(String auth, long id){
        if(auth==null){
            logger.error("returnTicket(auth: " + auth + ", id: " + id +")");
            logger.error("auth should not be null!");
            return false;
        }
        if(id<0){
            logger.error("returnTicket(auth: " + auth + ", id: " + id +")");
            logger.error("id should not be lower than 0!");
            return false;
        }
        logger.debug("returnTicket(auth: " + auth + ", id: " + id +")");
        User u = getUserByAuthToken(auth);
        if(u == null){
            logger.warn("User{authToken= '" + auth + "'} not authenticated");
            return false;
        }
        String query = "FROM Ticket t WHERE t.id = :id ";
        TypedQuery<Ticket> q = em.createQuery(query, Ticket.class);
        q.setParameter("id", id);

        Ticket t = null;
        try {
            t = q.getSingleResult();
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        if(t==null){
            logger.error("ticket{id= " + id + "} not found!");
            return false;
        }

        u.setWallet(u.getWallet()+t.getTrip_id().getPrice());
        try {
            em.remove(t);
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        logger.debug(t.toString() + " returned");
        return true;
    }

    public List<TripDTO> listTripsByDate(String auth, Date start, Date end) {
        logger.debug("listTripsByDate(auth:"+ auth + "sart: " + start.toString() + ", end: " + end.toString() +")");
        if(start.after(end)){
            logger.error("listTripsByDate(sart: " + start.toString() + ", end: " + end.toString() +")");
            logger.error("end must be after start");
        }

        if(!authentication(auth)){
            return null;
        }


        Timestamp startTs=new Timestamp(start.getTime());
        Timestamp endTs=new Timestamp(end.getTime());

        String query = "FROM Trip t WHERE t.departureDate BETWEEN :start AND :end";
        TypedQuery<Trip> q = em.createQuery(query, Trip.class);

        logger.debug(startTs.toString());
        logger.debug(endTs.toString());

        q.setParameter("start", startTs);
        q.setParameter("end", endTs);

        List<Trip> list = null;
        try {
            list = q.getResultList();
        }catch(Exception e){
            logger.error(e.getMessage());
            return null;
        }
        List<TripDTO> trips = new DTOMapping().getTrips(list);
        return trips;
    }

    @Transactional
    public List<Integer> listTripPlaces(String auth, long id) {
        if(id<0){
            logger.error("listTripPlaces(auth: " + auth + ", id: " + id +")");
            logger.error("id should not be lower than 0!");
        }
        logger.debug("listTripPlaces(auth: " + auth + ", id: " + id +")");

        if(getUserByAuthToken(auth)==null){
            logger.error("User{authToken= '" + auth + "'} not authenticated");
            return null;
        }

        String query = "FROM Trip t WHERE t.id = :id";
        TypedQuery<Trip> q = em.createQuery(query, Trip.class);
        q.setParameter("id", id);

        Trip t = null;
        try {
            t = q.getSingleResult();
        }catch (Exception e){
            logger.error("trip not found");
            logger.error(e.getMessage());
            return null;
        }
        if(t==null){
            logger.warn("trip not found");
            return null;
        }
        List<Integer> list = new ArrayList<Integer>();
        Collection<Ticket> tickets = t.getTickets();
        logger.debug("ticks: ");
        ArrayList<Ticket> ticks = null;
        logger.debug("ticks: ");
        if(tickets!=null) {
            if (tickets.size()>0) ticks = new ArrayList<>(tickets);
        }
        logger.debug("ticks: ");

        for(int i=1; i<=t.getCapacity(); i++){
            list.add(i);
        }

        if (tickets!=null && ticks!=null){
            for(int i=0; i<ticks.size(); i++){
                logger.debug("tick: " + i);
                Ticket tick = ticks.get(i);
                list.removeIf(n -> (n.intValue()==tick.getPlace()) );
            }
        }
        logger.debug("list created");
        return list;
    }

    @Transactional
    public List<TripDTO> listTripsByUser(String auth){
        if(auth==null){
            logger.error("listTripsByUser(auth: " + auth + ")");
            logger.error("auth should not be null!");
            return null;
        }

        User u = getUserByAuthToken(auth);
        if(u==null){
            logger.error("user{auth: " + auth + "} not found");
            return null;
        }
        String query = "FROM Trip t WHERE EXISTS (SELECT tick.id FROM Ticket tick WHERE tick.trip_id = t AND tick.user_id = :user_id)";
        TypedQuery<Trip> q = em.createQuery(query, Trip.class);
        q.setParameter("user_id", u);

        List<Trip> list  = null;
        try {
            list = q.getResultList();
        }catch (Exception e){
            logger.warn(em.toString());
            return null;
        }
        if(list == null){
            logger.debug(u.toString() + " dont have trips");
        }
        List<TripDTO> trips = new DTOMapping().getTrips(list);
        return trips;

    }

    @Transactional
    public List<TicketDTO> listTicketsByUser(String auth){
        if(auth==null){
            logger.error("listTicketsByUser(auth: " + auth + ")");
            logger.error("auth should not be null!");
            return null;
        }

        User u = getUserByAuthToken(auth);
        if(u==null){
            logger.error("user{auth: " + auth + "} not found");
            return null;
        }
        String query = "FROM Ticket t WHERE t.user_id = : user_id";
        TypedQuery<Ticket> q = em.createQuery(query, Ticket.class);
        q.setParameter("user_id", u);

        List<Ticket> list  = null;
        try {
            list = q.getResultList();
        }catch (Exception e){
            logger.warn(em.toString());
            return null;
        }
        if(list == null){
            logger.debug(u.toString() + " dont have trips");
        }
        List<TicketDTO> tickets = new DTOMapping().getTickets(list);
        return tickets;

    }

}
