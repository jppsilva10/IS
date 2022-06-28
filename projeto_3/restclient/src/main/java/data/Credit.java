package data;

import java.io.Serializable;

public class Credit implements Serializable{
    private long id;

    private float metric;

    public Credit() {
        super();
    }

    public Credit(long id, float metric) {
        super();
        this.id = id;
        this.metric = metric;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getMetric() {
        return metric;
    }

    public void setMetric(float metric) {
        this.metric = metric;
    }

    public String toString(){
        return "Client: " + id + " Credit: " + metric;
    }
}
