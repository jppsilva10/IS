package data;

import java.io.Serializable;
import java.util.Collection;

public class Managers implements Serializable{
    private long id;

    private String name;

    private Collection<Client> clients;

    public Managers() {
        super();
    }

    public Managers(String name) {
        super();
        this.name = name;
    }

    public ManagersDTO DTO(){
        return new ManagersDTO(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Client> getClients() {
        return clients;
    }

    public void setClients(Collection<Client> clients) {
        this.clients = clients;
    }

    public String toString(){
        String str = "Id: " + id + " Name: " + name + " Clients:\n";
        for (Client client : clients){
            str +="\t" + client + "\n";
        }
        return str;
    }
}
