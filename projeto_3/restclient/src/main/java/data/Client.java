package data;

import java.io.Serializable;

public class Client implements Serializable{
    private long id;

    private String name;

    private Managers manager_id;

    public Client() {
        super();
    }

    public Client(String name, Managers manager_id) {
        super();
        this.name = name;
        this.manager_id = manager_id;
    }

    public ClientDTO DTO(){
        return new ClientDTO(this);
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

    public Managers getManager_id() {
        return manager_id;
    }

    public void setManager_id(Managers manager_id) {
        this.manager_id = manager_id;
    }

    public String toString(){
        return "Id: " + id + " Name: " + name + " Manager: " + manager_id;
    }
}
