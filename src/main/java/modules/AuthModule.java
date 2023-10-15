package modules;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;

import constants.Emails;
import constants.Tokens;

import java.net.URISyntaxException;


public class AuthModule {

    private String email = "";
    private String password = "";
    public static final String SEASON_URL = "https://platform-api.onesoil.ai/ru/v1/seasons";
    public static final String NOTES_URL = "https://platform-api.onesoil.ai/en/v2/notes";

    public AuthModule(String email, String password){
        this.email = email;
        this.password = password;
    }
    public AuthModule(){}    

    public String authorization(){
        String url = "https://platform-api.onesoil.ai/ru/v1/auth/login";
        String response = "No response";
        try{
            URI uri = new URI(url);
            URL authURL = uri.toURL();
            HttpsURLConnection connection = (HttpsURLConnection) authURL.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
            // connection.addRequestProperty("Login", "Loshnickijkrai@gmail.com");
            // connection.setRequestProperty("login", email);
            // connection.addRequestProperty("Password", "TXyu7c");
            // connection.setRequestProperty("Content-Type", "multipart/form-data");

            String json = String.format("{\"login\":\"%s\", \"password\":\"%s\"}", email, password);
            writer.print(json);
            writer.flush();
            writer.close();
            if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK){
                BufferedReader authReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = authReader.readLine();
                // System.out.println(response);
                String token = parseToken(response);
                response = (token != null)?token: "";
            }
            System.out.println(connection.getResponseCode());
        }catch(URISyntaxException ex){
            System.out.println(ex.getMessage());
        }
        catch(MalformedURLException ex){
            System.out.println(ex.getMessage());
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        return response;
    }

    public String getToken(String email){
        switch(email){
            case Emails.LOSHNICA_EMAIL:{
                return Tokens.LOSHNICKIJ_KRAI;
            }
            case Emails.NOVOSELKI_EMAIL:{
                    return Tokens.BOLSHIENOVOSELKI;
            }
            case Emails.BORISOV_SOUZ_AGRO_EMAIL:{
                    return Tokens.BORISOVSOUZAGRO;
            }
            case Emails.ZACHISTEAGRO_EMAIL:{
                    return Tokens.ZACHISTEAGRO;
            }
            default:{
                return Tokens.LOSHNICKIJ_KRAI;
            }
        }
    }


    private String parseToken(String data){
        String regex = "\"token\":\\s\"([\\w\\.]*)\"";
        //"\"token\":\\s\"([\\w\\.]*)\""
        Pattern p = Pattern.compile(regex);
        Matcher match = p.matcher(data);
        match.find();
        String token = match.group(1);
        return token;
    }
}
