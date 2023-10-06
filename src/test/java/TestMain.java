
import java.util.concurrent.Semaphore;


public class TestMain {
    public static void main(String[] args) {
        /*
         * 1113794
         * 911015,
         * 744980,
         * 1009578
         * https://platform-api.onesoil.ai/ru/v1/seasons/1113794/fields?with=crops&with=geometry&with=last_ndvi
         */
        String[] seasons = new String[]{
            "1113794",
            "911015",
            "744980",
            "1009578",
            "1009579",
            "834675",
            "1074295",
        };
        Semaphore semaphore = new Semaphore(0);
        int j = 20;
        for(int j1 = 0; j1 < j; j1++){
            for(String season: seasons){
            Thread httpConnection = new Thread(new TestHTTPSConnection(semaphore, season));        
            httpConnection.start();
        }
        }
        for(int i = 0; i<seasons.length*j; i++){
            try{
                semaphore.acquire();
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
        
    }

    // private static String getJSONFromConnection(String urlStr){
    //     String response = "";
    //     try{
    //         URI uri = new URI(urlStr);
    //         URL url = uri.toURL();
    //         AuthModule module = new AuthModule();
    //         String sampleEmail = Emails.LOSHNICA_EMAIL;
    //         String formatToken = String.format("Token %s", module.getToken(sampleEmail));
    //         HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
    //         connection.setRequestMethod("GET");
    //         connection.setRequestProperty("Authorization", formatToken);
    //         if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK){
    //             BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    //             response = reader.readLine();
    //         }else{
    //             response = Integer.toString(connection.getResponseCode());
    //         }
    //     }catch(URISyntaxException ex){
    //         ex.printStackTrace();
    //     }catch(MalformedURLException urlException){
    //         urlException.printStackTrace();
    //     }catch(IOException IOex){
    //         IOex.printStackTrace();
    //     }
    //     return response;
    // }

}
