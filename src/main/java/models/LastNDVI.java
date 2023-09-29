package models;

public class LastNDVI {
    private String uuid;
    private String date;
    private Avg avg;
    private Std std;
    private int min;
    private int max;

    public LastNDVI(){}

    public LastNDVI(String uuid, String date, Avg avg, Std std, int min, int max) {
        this.uuid = uuid;
        this.date = date;
        this.avg = avg;
        this.std = std;
        this.min = min;
        this.max = max;
    }

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public Avg getAvg() {
        return avg;
    }
    public void setAvg(Avg avg) {
        this.avg = avg;
    }
    public Std getStd() {
        return std;
    }
    public void setStd(Std std) {
        this.std = std;
    }
    public int getMin() {
        return min;
    }
    public void setMin(int min) {
        this.min = min;
    }
    public int getMax() {
        return max;
    }
    public void setMax(int max) {
        this.max = max;
    }

    
}
