package data;

import java.io.Serializable;

public class Highest_metric implements Serializable{
    private String id;

    private long metric;

    public Highest_metric() {
        super();
    }

    public Highest_metric(String id, long metric) {
        super();
        this.id = id;
        this.metric = metric;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getMetric() {
        return metric;
    }

    public void setMetric(long metric) {
        this.metric = metric;
    }

    public String toString(){
        return "" + metric;
    }
}
