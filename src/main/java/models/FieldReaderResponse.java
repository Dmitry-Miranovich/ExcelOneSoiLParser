package models;

public class FieldReaderResponse {
    private String success;
    private FieldsData data;
    private String seasonName;

    public FieldReaderResponse(){}
    public FieldReaderResponse(String success, FieldsData data) {
        this.success = success;
        this.data = data;
    }
    public String getSuccess() {
        return success;
    }
    public void setSuccess(String success) {
        this.success = success;
    }
    public FieldsData getData() {
        return data;
    }
    public void setData(FieldsData data) {
        this.data = data;
    }
    public String getSeasonName() {
        return seasonName;
    }
    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }
}
