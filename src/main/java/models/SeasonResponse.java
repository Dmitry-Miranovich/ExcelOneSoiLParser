package models;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SeasonResponse {
    public String success;
    public Season[] data;
    private String email;

    public SeasonResponse(){}

    public SeasonResponse(String success, Season[] data) {
        this.success = success;
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Season[] getData() {
        return data;
    }

    public void setData(Season[] data) {
        this.data = data;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public int getSeasonID(String name){
        int seasonID = 0;
        if(data.length > 0){
            for(Season season: data){
                if(season.getTitle().equals(name)){
                    seasonID = season.getId();
                    break;
                }
            }
        }
        return seasonID;
    }
    
    public ArrayList<Integer> getSeasonID(Date date){
        ArrayList<Integer> seasonsID = new ArrayList<>();
        if(data.length > 0){
            for(Season season : data){
                if(date.before(season.getStart_date())){
                    seasonsID.add(season.getId());
                }
            }
        }
        return seasonsID;
    }
}
