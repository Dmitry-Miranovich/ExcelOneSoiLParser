package modules;

import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.databind.ObjectMapper;

import models.FieldReaderResponse;
import models.Note;
import models.NoteResponse;
import models.SeasonResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//https://platform-api.onesoil.ai/ru/v1/seasons/{ввести номер поля}/fields?with=crops&with=geometry&with=last_ndvi

public class FieldDataModule {
    private String token = "";
    private String email = "";
    private ExcelWriterModule excelBook;

    public FieldDataModule() {
    }

    public FieldDataModule(String token) {
        this.token = token;
    }

    public FieldDataModule(String token, String email) {
        this.token = token;
        this.email = email;
    }

    public FieldDataModule(ExcelWriterModule excelBook) {
        this.excelBook = excelBook;
    }

    public ArrayList<FieldReaderResponse> sampleConnection(String url, String token) {
        String formattedToken = String.format("Token %s", token);
        ArrayList<FieldReaderResponse> responses = new ArrayList<>();
        try {
            URI seasonURI = new URI(url);
            URL seasonURL = seasonURI.toURL();
            HttpsURLConnection connection = (HttpsURLConnection) seasonURL.openConnection();
            connection.addRequestProperty("Authorization", formattedToken);
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                String readerData = reader.readLine();
                ObjectMapper mapper = new ObjectMapper();
                ArrayList<String> seasons = getSeasons(readerData);
                for (String season : seasons) {
                    try {
                                String fieldUrlStr = String.format(
                                        "https://platform-api.onesoil.ai/ru/v1/seasons/%s/fields?with=crops&with=geometry&with=last_ndvi",
                                        season);
                                URI fieldURI = new URI(fieldUrlStr);
                                URL fieldURL = fieldURI.toURL();
                                HttpsURLConnection secondConnection = (HttpsURLConnection) fieldURL.openConnection();
                                secondConnection.setRequestMethod("GET");
                                secondConnection.setRequestProperty("Authorization", formattedToken);
                                if (secondConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                                    BufferedReader fieldReader = new BufferedReader(new InputStreamReader(
                                            secondConnection.getInputStream(), StandardCharsets.UTF_8));
                                    String fields = fieldReader.readLine();
                                    FieldReaderResponse response = mapper.readValue(fields, FieldReaderResponse.class);
                                    responses.add(response);

                                } else {
                                    System.out.println(secondConnection.getResponseCode());
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (URISyntaxException ex2) {
            System.out.println(ex2.getMessage());
        }
        return responses;
    }

    public SeasonResponse getSeasonResponse(String url, String token) {
        String formattedToken = String.format("Token %s", token);
        SeasonResponse response = null;
        try {
            URI seasonsURI = new URI(url);
            URL seasonURL = seasonsURI.toURL();
            HttpsURLConnection connection = (HttpsURLConnection) seasonURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", formattedToken);
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                ObjectMapper mapper = new ObjectMapper();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String seasonJSON = reader.readLine();
                response = mapper.readValue(seasonJSON, SeasonResponse.class);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (URISyntaxException ex2) {
            System.out.println(ex2.getMessage());
        }
        return response;
    }

    public NoteResponse getNoteResponse(String url, String token) {
        String formattedToken = String.format("Token %s", token);
        NoteResponse response = null;
        try {
            URI noteURI = new URI(url);
            URL noteURL = noteURI.toURL();
            HttpsURLConnection connection = (HttpsURLConnection) noteURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", formattedToken);
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                ObjectMapper mapper = new ObjectMapper();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                String noteJSON = reader.readLine();
                response = mapper.readValue(noteJSON, NoteResponse.class);
                Note[] notes = filterNotes(response);
                response.setData(notes);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (URISyntaxException ex2) {
            System.out.println(ex2.getMessage());
        }
        return response;
    }

    public ArrayList<String> getSeasons(String data) {
        String regex = "\"id\":\\s*(\\d*)";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(data);
        ArrayList<String> seasons = new ArrayList<>();
        while (matcher.find()) {
            seasons.add(matcher.group(1));
        }
        return seasons;
    }

    public ArrayList<String> getSeasonsName(String data) {
        // String regex = "\"title\":\\s*(\".*\")";
        // String regex = "\"title\":\\s*\"?([а-яА-Я0-9]*)\"?[\\,]";
        String regex = "\"title\":\\s[а-яА-Я0-9\"\\s]*";
        // "\"title\":\\s*(".*")"
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(data);
        ArrayList<String> seasons = new ArrayList<>();
        while (matcher.find()) {
            seasons.add(matcher.group(0));
        }
        return seasons;
    }

    private Note[] filterNotes(NoteResponse response) {
        ArrayList<Note> notes = new ArrayList<>();
        for (Note note : response.getData()) {
            if (!note.getText().equals("")) {
                notes.add(note);
            }
        }

        return notes.toArray(new Note[notes.size()]);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ExcelWriterModule getExcelBook() {
        return excelBook;
    }

    public void setExcelBook(ExcelWriterModule excelBook) {
        this.excelBook = excelBook;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
