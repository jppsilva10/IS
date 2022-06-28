package data;

import java.io.Serializable;

public class ClientDTO implements Serializable{
    private long id;

    private String name;

    private long manager_id;

    public ClientDTO() {
        super();
    }

    public ClientDTO(String name, long manager_id) {
        super();
        this.name = name;
        this.manager_id = manager_id;
    }

    public ClientDTO(Client c) {
        this.id = c.getId();
        this.name = c.getName();
        this.manager_id = c.getManager_id().getId();
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

    public long getManager_id() {
        return manager_id;
    }

    public void setManager_id(long manager_id) {
        this.manager_id = manager_id;
    }
}
