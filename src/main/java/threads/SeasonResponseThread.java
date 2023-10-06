package threads;

import java.util.ArrayList;

import org.apache.xmlbeans.impl.common.Mutex;

import models.SeasonResponse;

public class SeasonResponseThread implements Runnable {
    
    private Mutex mutex;
    private String url;
    private String token;

    public SeasonResponseThread(Mutex mutex, String url, String token, String email, ArrayList<SeasonResponse> seasonResponses){
        this.mutex = mutex;
        this.url = url;
        this.token = token;
    }

    @Override
    public void run(){
        
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Mutex getMutex() {
        return mutex;
    }

    public void setMutex(Mutex mutex) {
        this.mutex = mutex;
    }

    
}
