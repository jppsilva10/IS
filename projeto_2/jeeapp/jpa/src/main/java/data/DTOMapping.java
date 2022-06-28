package data;

import java.util.ArrayList;
import java.util.List;

public class DTOMapping {

    public List<TicketDTO> getTickets(List<Ticket> tickets) {
        List<TicketDTO> list = new ArrayList<TicketDTO>();
        for(int i=0; i<tickets.size(); i++){
            list.add(convertToTicketDTO(tickets.get(i)));
        }
        return list;
    }

    public TicketDTO convertToTicketDTO(Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setPlace(ticket.getPlace());
        ticketDTO.setUser_id(ticket.getUser_id().getId());
        ticketDTO.setTrip_id(convertToTripDTO(ticket.getTrip_id()));
        return ticketDTO;
    }

    public List<UserDTO> getUsers(List<User> users) {
        List<UserDTO> list = new ArrayList<UserDTO>();
        for(int i=0; i<users.size(); i++){
            list.add(convertToUserDTO(users.get(i)));
        }
        return list;
    }

    public List<UserDTO> getUsersByTickets(List<Ticket> tickets) {
        List<UserDTO> list = new ArrayList<UserDTO>();
        for(int i=0; i<tickets.size(); i++){
            list.add(convertToUserDTO(tickets.get(i).getUser_id()));
        }
        return list;
    }

    public UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setWallet(user.getWallet());
        return userDTO;
    }

    public List<TripDTO> getTrips(List<Trip> trips) {
        List<TripDTO> list = new ArrayList<TripDTO>();
        for(int i=0; i<trips.size(); i++){
            list.add(convertToTripDTO(trips.get(i)));
        }
        return list;
    }

    public TripDTO convertToTripDTO(Trip trip) {
        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(trip.getId());
        tripDTO.setDepartureDate(trip.getDepartureDate());
        tripDTO.setCapacity(trip.getCapacity());
        tripDTO.setPrice(trip.getPrice());
        if(trip.getTickets()==null){
            tripDTO.setTickets(0);
        }
        else {
            tripDTO.setTickets(trip.getTickets().size());
        }
        tripDTO.setDeparture(trip.getDeparture().getName());
        tripDTO.setDestination(trip.getDestination().getName());

        return tripDTO;
    }
}
