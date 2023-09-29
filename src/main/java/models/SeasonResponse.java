package models;

public class SeasonResponse {
    public String success;
    public Season[] data;

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

    
}
