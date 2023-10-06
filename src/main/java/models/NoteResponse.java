package models;

public class NoteResponse {
    private boolean success;
    private Note[] data;

    public NoteResponse(){}

    public NoteResponse(boolean success, Note[] data) {
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Note[] getData() {
        return data;
    }

    public void setData(Note[] data) {
        this.data = data;
    }

    
}
