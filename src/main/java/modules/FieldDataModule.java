package modules;

import javax.net.ssl.HttpsURLConnection;
import com.fasterxml.jackson.databind.ObjectMapper;

import controller.MainController;
import models.FieldReaderResponse;
import models.Note;
import models.Field;
import models.NoteResponse;
import models.Season;
import models.SeasonResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//https://platform-api.onesoil.ai/ru/v1/seasons/{ввести номер поля}/fields?with=crops&with=geometry&with=last_ndvi

public class FieldDataModule {
    private String token = "";
    private String email = "";
    private final int REQUEST_TIMEOUT_VALUE = 10000;
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

    public ArrayList<FieldReaderResponse> getFieldResponses(SeasonResponse collectedSeason, String token)
            throws InterruptedException {
        String formattedToken = String.format("Token %s", token);
        ArrayList<FieldReaderResponse> responses = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        String[] seasons = getSeasonsId(collectedSeason);
        Thread[] threads = new Thread[seasons.length];
        int index = 0;
        for (String season : seasons) {
            Thread fieldThread = new Thread(new Runnable() {
                public void run() {
                    getFields(formattedToken, season, mapper, responses);
                }
            });
            threads[index] = fieldThread;
            threads[index].start();
            index++;
        }
        for (Thread thread : threads) {
            thread.join();
        }
        // Thread.currentThread().join();
        return responses;
    }

    public ArrayList<FieldReaderResponse> getFieldReaderResponse(String token, String season){
        String formattedToken = String.format("Token %s", token);
        ArrayList<FieldReaderResponse> fieldReaderResponses = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        getFields(formattedToken, season, mapper, fieldReaderResponses);
        return fieldReaderResponses;
    }

    private void getFields(String formattedToken, String season, ObjectMapper mapper,
            ArrayList<FieldReaderResponse> responses) {
        try {
            String fieldUrlStr = String.format(
                    "https://platform-api.onesoil.ai/ru/v1/seasons/%s/fields?with=crops&with=geometry&with=last_ndvi",
                    season);
            URI fieldURI = new URI(fieldUrlStr);
            URL fieldURL = fieldURI.toURL();
            HttpsURLConnection secondConnection = (HttpsURLConnection) fieldURL.openConnection();
            secondConnection.setRequestMethod("GET");
            secondConnection.setRequestProperty("Authorization", formattedToken);
            secondConnection.setConnectTimeout(REQUEST_TIMEOUT_VALUE);
            int responseCode = secondConnection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader fieldReader = new BufferedReader(new InputStreamReader(
                        secondConnection.getInputStream(), StandardCharsets.UTF_8));
                String fields = fieldReader.readLine();
                FieldReaderResponse response = mapper.readValue(fields, FieldReaderResponse.class);
                response.setSeasonID(Integer.parseInt(season));
                if (response.getData() != null) {
                    convertMultidimensionalArray(response);
                }
                responses.add(response);
            } else {
                alert("Запрос обработался с полученем полей", responseCode);
            }
            secondConnection.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }

    private String[] getSeasonsId(SeasonResponse collectedSeason) {
        Season[] seasonsObj = collectedSeason.getData();
        String[] seasons = new String[seasonsObj.length];
        for (int i = 0; i < seasonsObj.length; i++) {
            seasons[i] = Integer.toString(seasonsObj[i].getId());
        }
        return seasons;
    }

    public SeasonResponse getSeasonResponse(String url, String token) {
        String formattedToken = String.format("Token %s", token);
        MainController controller = new MainController();
        SeasonResponse response = null;
        try {
            URI seasonsURI = new URI(url);
            URL seasonURL = seasonsURI.toURL();
            HttpsURLConnection connection = (HttpsURLConnection) seasonURL.openConnection();
            connection.setReadTimeout(REQUEST_TIMEOUT_VALUE);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", formattedToken);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                ObjectMapper mapper = new ObjectMapper();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String seasonJSON = reader.readLine();
                response = mapper.readValue(seasonJSON, SeasonResponse.class);
            }else{
                alert("Запрос обработался с полученем сезонов", responseCode);
            }
            connection.disconnect();
        } catch (IOException ex) {
            controller.showWarningMessage("Не удалось подключится к серверам OneSoil");
        } catch (URISyntaxException ex2) {
            controller.showWarningMessage("Указанный url больше не доступен");
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
            connection.setReadTimeout(REQUEST_TIMEOUT_VALUE);
            connection.setRequestProperty("Authorization", formattedToken);
            int responceCode = connection.getResponseCode();
            if (responceCode == HttpsURLConnection.HTTP_OK) {
                ObjectMapper mapper = new ObjectMapper();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                String noteJSON = reader.readLine();
                response = mapper.readValue(noteJSON, NoteResponse.class);
                Note[] notes = filterNotes(response);
                response.setData(notes);
            }else{
                alert("Запрос обработался с получением заметок", responceCode);
            }
            connection.disconnect();
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

    private void convertMultidimensionalArray(FieldReaderResponse response) {
        for (Field field : response.getData().getRows()) {
            field.setSeasonID(response.getSeasonID());
            ArrayList<float[]> coordinates = new ArrayList<>();
            for (float[][][] thirdDim : field.getGeometry().getCoordinates()) {
                float[][] mainSecondDim = thirdDim[0];
                try {
                    for (float[] realDim : mainSecondDim) {
                        coordinates.add(new float[] { realDim[0], realDim[1] });
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
            field.setRealCoordinates(coordinates.toArray(new float[coordinates.size()][2]));
        }
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

    private void alert(String text, int responseCode){
        System.out.println(String.format("%s, с кодом %d", text, responseCode));
    }
}
