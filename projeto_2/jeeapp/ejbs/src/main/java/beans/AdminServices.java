package beans;

import data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Stateless
public class AdminServices implements IAdminServices{
    @PersistenceContext(unitName = "BusCompany")
    EntityManager em;

    final Logger logger = LoggerFactory.getLogger(AdminServices.class);

    @Resource(mappedName="java:jboss/mail/Default")
    private Session mailSession;

    public void addAdmin(String username, String password) {
        Manager m = new Manager();
        m.setUsername(username);
        m.setPassword(password);
        em.persist(m);
    }

    public String login(String username, String password) {
        if(username==null || password == null){
            logger.error("login(username: " + username + ", password: " + password + ")");
            logger.error("username and and password should not be null!");
            return null;
        }
        logger.debug("login(username: " + username + ", password: " + password + ")");

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Authentication Token could not be created!");
            return null;
        }


        String token = username + password + new Random().nextInt(1000000);
        md.update(token.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        token = sb.toString();
        logger.debug("Token created: " + token);

        String query = "FROM Manager m " +
                "WHERE m.username = :username AND m.password = :password";


        TypedQuery<Manager> q = em.createQuery(query, Manager.class);
        q.setParameter("username", username);
        q.setParameter("password", password);

        Manager m = null;
        try {
            m = q.getSingleResult();
        }catch (Exception e){
            logger.warn(e.getMessage());
            logger.warn("Manager{username= '" + username + ", password= " + password + "'} not found");
            return null;
        }
        if(m==null){
            logger.debug("Manager{username= '" + username + ", password= " + password + "'} not found");
            return null;
        }
        m.setAuthToken(token);
        logger.debug(m.toString() + " authenticated");
        return token;
    }

    public boolean addPlace(String auth, String name){
        if(name == null){
            logger.error("addPlace(name: " + name + ")");
            logger.error("name should not be null!");
        }
        logger.debug("addPlace(name: " + name + ")");

        Place p = new Place();
        p.setName(name);
        try {
            em.persist(p);
        }catch (Exception e){
            logger.warn(e.getMessage());
            logger.warn("Cant add " + p.toString());
            return false;
        }
        logger.debug(p.toString() + " added");
        return true;
    }

    public List<Place> listPlaces(String auth){
        logger.debug("listPlaces()");
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

    public Place getPlaceByName(String name){
        if(name == null){
            logger.error("getPlaceByName(name= " + name +")");
            logger.error("name should not be null!");
            return null;
        }
        logger.debug("getPlaceByName(name= " + name +")");

        TypedQuery<Place> q = em.createQuery("FROM Place p WHERE p.name = :name", Place.class);
        q.setParameter("name", name);
        Place p = null;
        try {
            p = q.getSingleResult();
        }catch (Exception e){
            logger.error(e.getMessage());
            logger.error("place{name= " + name + "} dont exist!");
            return null;
        }
        return p;
    }

    @Transactional
    public boolean addTrip(String auth, String departure, String destination, Date departureDate, int capacity, float price) {
        if(capacity<=0){
            logger.error("addTrip(departure: "+ departure + ", destination:" + destination + ", departureDate: " + departureDate.toString() + ", capacity: " + capacity + ", price: " + price +")");
            logger.error("capacity should not be 0 or lower!");
            return false;
        }
        if(price<=0){
            logger.error("addTrip(departure: "+ departure + ", destination:" + destination + ", departureDate: " + departureDate.toString() + ", capacity: " + capacity + ", price: " + price +")");
            logger.error("price should not be 0 or lower!");
            return false;
        }
        if(departure==null){
            logger.error("addTrip(departure: "+ departure + ", destination:" + destination + ", departureDate: " + departureDate.toString() + ", capacity: " + capacity + ", price: " + price +")");
            logger.error("departure should not be null!");
            return false;
        }
        if(destination==null){
            logger.error("addTrip(departure: "+ departure + ", destination:" + destination + ", departureDate: " + departureDate.toString() + ", capacity: " + capacity + ", price: " + price +")");
            logger.error("destination should not be null!");
            return false;
        }
        Place departurePlace = getPlaceByName(departure);
        if(departurePlace==null) {
            logger.error("place{name= " + departure + "} dont exist!");
            return false;
        }
        Place destinationPlace = getPlaceByName(destination);
        if(destinationPlace==null) {
            logger.error("place{name= " + departure + "} dont exist!");
            return false;
        }

        Trip t = new Trip();
        t.setDepartureDate(departureDate);
        t.setCapacity(capacity);
        t.setPrice(price);
        departurePlace.getDepartures().add(t);
        destinationPlace.getDestinations().add(t);

        t.setDeparture(departurePlace);
        t.setDestination(destinationPlace);

        try {
            em.persist(t);
        }catch (Exception e){
            logger.warn(e.getMessage());
            logger.warn("Cant add " + t.toString());
            return false;
        }
        logger.debug(t.toString() + " added");
        return true;
    }

    public List<TripDTO> listTripsByDate(String auth, Date start, Date end) {
        logger.debug("listTripsByDate(sart: " + start.toString() + ", end: " + end.toString() +")");
        if(start.after(end)){
            logger.error("listTripsByDate(sart: " + start.toString() + ", end: " + end.toString() +")");
            logger.error("end must be after start");
        }
        Timestamp startTs=new Timestamp(start.getTime());
        Timestamp endTs=new Timestamp(end.getTime());

        String query = "FROM Trip t WHERE t.departureDate BETWEEN :start AND :end ORDER BY t.departureDate";
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

    @Override
    public int deleteTrip(String auth, long id) {
        logger.debug("deleteTrip(id: "+ id +")");

        String query = "FROM Trip t WHERE t.id = :id";
        TypedQuery<Trip> q = em.createQuery(query, Trip.class);

        q.setParameter("id", id);
        Trip t = null;
        try {
            t = q.getSingleResult();
        }catch(Exception e){
            logger.error(e.getMessage());
            return -1;
        }
        if(t ==null) return -1;
        try {
            em.remove(t);
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }

        if(t.getTickets()!=null) {
            logger.debug("working");
            List<Ticket> ticks = new ArrayList<Ticket>(t.getTickets());
            logger.debug("still working");
            for (int i = 0; i < ticks.size(); i++) {
                User u = ticks.get(i).getUser_id();
                u.setWallet(u.getWallet()+t.getPrice());
                sendmail(u.getEmail(), "Trip " + t.getId() + "Canceled", "Trip " + t.getId() + " was canceled");
            }
        }

        return 0;
    }

    @Override
    public List<UserDTO> listPassengers(String auth, long id) {
        logger.debug("listPassengers(id: " + id + ")");

        String query = "FROM Trip t WHERE t.id = :id";
        TypedQuery<Trip> q = em.createQuery(query, Trip.class);

        q.setParameter("id", id);

        Trip t = null;
        try {
            t = q.getSingleResult();
        }catch(Exception e){
            logger.error(e.getMessage());
            return null;
        }
        ArrayList<Ticket> tickets = null;
        if(t.getTickets()!=null){
            tickets = new ArrayList<Ticket>(t.getTickets());
        }
        else{
            return new ArrayList<UserDTO>();
        }

        List<UserDTO> users = new DTOMapping().getUsersByTickets(tickets);
        for(int i=0; i<users.size(); i++){
            logger.debug("email: " + users.get(i).getEmail());
        }

        return users;
    }

    @Override
    public float getRevenue(String auth, Date start, Date end) {
        logger.debug("getRevenue(sart: " + start.toString() + ", end: " + end.toString() +")");
        Timestamp startTs=new Timestamp(start.getTime());
        Timestamp endTs=new Timestamp(end.getTime());

        String query = "FROM Trip t WHERE t.departureDate BETWEEN :start AND :end AND EXISTS(SELECT tick FROM Ticket tick WHERE tick.trip_id = t)";

        //Query q = em.createQuery(query);

        TypedQuery<Trip> q = em.createQuery(query, Trip.class);

        logger.debug(startTs.toString());
        logger.debug(endTs.toString());

        q.setParameter("start", startTs);
        q.setParameter("end", endTs);

        List<Trip> list = null;
        try {
            list = q.getResultList();
            if(list==null)return 0;

            float total=0;
            for (Trip i : list ){
                total += i.getPrice() * i.getTickets().size();
            }
            return total;
        }catch(Exception e){
            logger.error(e.getMessage());
            return 0;
        }
    }

    private boolean sendmail(String To, String subject, String content){
        try    {
            MimeMessage m = new MimeMessage(mailSession);
            Address from = new InternetAddress("projeto.si.2020.2021@gmail.com");
            Address[] to = new InternetAddress[] {new InternetAddress(To) };

            m.setFrom(from);
            m.setRecipients(Message.RecipientType.TO, to);
            m.setSubject(subject);
            m.setSentDate(new java.util.Date());
            m.setContent(content,"text/plain");
            Transport.send(m);
            logger.debug("mail sent");
            return true;
        }
        catch (javax.mail.MessagingException e)
        {
            logger.error(e.getMessage());
            return false;
        }
    }

    public Manager getManagerByAuthToken(String authToken) {
        if(authToken==null){
            logger.error("getManagerByAuthToken(authToken: " + authToken + ")");
            logger.error("authToken should not be null!");
            return null;
        }
        logger.debug("getManagerByAuthToken(authToken: " + authToken + ")");

        String query = "FROM Manager m " +
                "WHERE m.authToken = :authToken";

        TypedQuery<Manager> q = em.createQuery(query, Manager.class);
        q.setParameter("authToken", authToken);

        Manager m = null;
        try {
            m = q.getSingleResult();
        }catch (Exception e){
            logger.warn(e.getMessage());
            logger.warn("User{authToken= '" + authToken + "'} not found");

            return null;
        }
        if(m==null){
            logger.debug("User{authToken= '" + authToken + "'} not found");
            return null;
        }
        logger.debug("found " + m.toString());
        return m;
    }

    public boolean authentication(String authToken) {
        logger.debug("authentication(authToken: " + authToken + ")");
        if(getManagerByAuthToken(authToken) != null){
            logger.debug("User{authToken= '" + authToken + "'} authenticated");
            return true;
        }

        logger.debug("User{authToken= '" + authToken + "'} not authenticated");
        return false;
    }

    @Transactional
    public List<User> Top5(){

        long menor_valor=0, aux_menor_valor=0;
        List<User> Top5_user=null;

        long[][] id_bilhetes={{0,0},{0,0},{0,0},{0,0},{0,0}};

        String query = "Select * FROM user;";
        TypedQuery<User> q = em.createQuery(query, User.class);
        List<User> listUser  = null;

        try {
            listUser = q.getResultList();
        }catch (Exception e){
            logger.warn(em.toString());
            return null;
        }
        if(listUser == null){
            logger.debug("Dont have users");
            return null;
        }

        if(listUser.size()<=5){
            logger.debug("list od Users less than or equals to 5 ");
            return listUser;
        }

        for (User u:listUser) {


            String query2 = "FROM Trip t WHERE EXISTS (SELECT tick.id FROM Ticket tick WHERE tick.trip_id = t AND tick.user_id = :user_id)";
            TypedQuery<Trip> q2 = em.createQuery(query2, Trip.class);
            q2.setParameter("user_id", u);
            List<Trip> list  = null;
            try {
                list = q2.getResultList();
            }catch (Exception e){
                logger.warn(em.toString());
                return null;
            }
            if(list == null){
                logger.debug(u.toString() + " dont have trips");
            }

            if(list.size()>menor_valor){
                aux_menor_valor=menor_valor;
                //procura o indice com o menor valor e subtitui
                //procura o novo menor valor
                menor_valor= id_bilhetes[0][1];
                for (int i = 0; i < 5 ; i++) {
                    if(id_bilhetes[i][1] == aux_menor_valor){
                        id_bilhetes[i][0] = u.getId();
                        id_bilhetes[i][1] = list.size();
                    }
                    if(id_bilhetes[i][1]<menor_valor){
                        menor_valor= id_bilhetes[i][1];
                    }

                }
            }

        }

        for (int i = 0; i <5 ; i++) {
            for (User u:listUser ) {
                if(id_bilhetes[i][0] == u.getId()){
                    Top5_user.add(u);
                }
            }
        }

        return Top5_user;

    }
}
