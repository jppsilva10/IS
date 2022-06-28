package data;

import java.io.Serializable;

public class Balance_last_month implements Serializable{
    private long id;

    private float metric;

    public Balance_last_month() {
        super();
    }

    public Balance_last_month(long id, float metric) {
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
        return "Client: " + id + " Balance for the last month: " + metric;
    }
}
