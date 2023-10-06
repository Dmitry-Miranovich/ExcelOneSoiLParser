import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.Semaphore;

import javax.net.ssl.HttpsURLConnection;

import constants.Emails;
import modules.AuthModule;

public class TestHTTPSConnection implements Runnable {
    private Semaphore semaphore;
    private String seasonID;

    public TestHTTPSConnection(Semaphore semaphore, String seasonID) {
        this.semaphore = semaphore;
        this.seasonID = seasonID;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        String urlStr = String.format(
                "https://platform-api.onesoil.ai/ru/v1/seasons/%s/fields?with=crops&with=geometry&with=last_ndvi",
                seasonID);
        String json = getJSONFromConnection(urlStr);
        long end = System.currentTimeMillis();
        // System.out.println("Current time is: " + (end - start));
        semaphore.release();
    }

    private String getJSONFromConnection(String urlStr) {
        String response = "";
        try {
            URI uri = new URI(urlStr);
            URL url = uri.toURL();
            AuthModule module = new AuthModule();
            String sampleEmail = Emails.LOSHNICA_EMAIL;
            String formatToken = String.format("Token %s", module.getToken(sampleEmail));
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", formatToken);
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = reader.readLine();
            } else {
                response = Integer.toString(connection.getResponseCode());
            }
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException urlException) {
            urlException.printStackTrace();
        } catch (IOException IOex) {
            IOex.printStackTrace();
        }
        return response;
    }
}
