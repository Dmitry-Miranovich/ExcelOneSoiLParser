
import java.io.File;
import java.util.ArrayList;

import constants.Emails;
import constants.ExcelHeaders;
import models.FieldReaderResponse;
import models.SeasonResponse;
import modules.AuthModule;
import modules.ExcelWriterModule;
import modules.FieldDataModule;

public class Main {
    public static void main(String[] args) {
        // String email = "Loshnickijkrai@gmail.com";
        // String password = "TXyu7c";
        AuthModule authModule = new AuthModule();
        // FieldDataModule module = new FieldDataModule(password, email);
        ExcelWriterModule module = new ExcelWriterModule(ExcelHeaders.ONESOIL_FIELDS_OUTPUT);
        FieldDataModule fieldModule = new FieldDataModule(module);
        String seasons = "https://platform-api.onesoil.ai/ru/v1/seasons";
        ArrayList<ArrayList<FieldReaderResponse>> emailFieldResponses = new ArrayList<>();
        ArrayList<SeasonResponse> seasonResponses = new ArrayList<>();
        for(String email: Emails.emails){
            String token = authModule.getToken(email);
            ArrayList<FieldReaderResponse> responses = fieldModule.sampleConnection(seasons, token);
            SeasonResponse seasonResponse = fieldModule.getSeasonResponse(seasons, token);
            emailFieldResponses.add(responses);
            seasonResponses.add(seasonResponse);
        }
        module.writeAllFields(emailFieldResponses);
        module.writeAllSeasons(seasonResponses);
        module.writeFile(module.getBook());
    }
}