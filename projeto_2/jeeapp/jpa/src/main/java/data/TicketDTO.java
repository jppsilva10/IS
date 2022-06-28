package data;

import javax.persistence.*;
import java.io.Serializable;

public class TicketDTO implements Serializable{

    private long id;

    private int place;

    private TripDTO trip_id;
    private long user_id;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public TripDTO getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(TripDTO trip_id) {
        this.trip_id = trip_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

}
