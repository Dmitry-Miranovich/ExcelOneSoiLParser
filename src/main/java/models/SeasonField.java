package models;

import java.util.ArrayList;

public class SeasonField {
    private ArrayList<FieldReaderResponse> responses;
    private int season_id;
    
    
    public SeasonField(ArrayList<FieldReaderResponse> responses, int season_id) {
        this.responses = responses;
        this.season_id = season_id;
    }
    public ArrayList<FieldReaderResponse> getResponses() {
        return responses;
    }
    public void setResponses(ArrayList<FieldReaderResponse> responses) {
        this.responses = responses;
    }
    public int getSeason_id() {
        return season_id;
    }
    public void setSeason_id(int season_id) {
        this.season_id = season_id;
    }

    
}
