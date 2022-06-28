package data;

import java.io.Serializable;

public class Client implements Serializable{
    private static final long serialVersionUID = 1L;

    private long id;

    private String name;

    private Manager manager_id;

    public Client() {
        super();
    }

    public Client(String name, Manager manager_id) {
        super();
        this.name = name;
        this.manager_id = manager_id;
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

    public Manager getManager_id() {
        return manager_id;
    }

    public void setManager_id(Manager manager_id) {
        this.manager_id = manager_id;
    }
}
