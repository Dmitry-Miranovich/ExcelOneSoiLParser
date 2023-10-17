package controller;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.ResourceBundle;

import constants.Warnings;
import exceptions.MissingDateException;
import exceptions.MissingFilePathException;
import exceptions.SeasonsException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import models.FieldReaderResponse;
import models.Season;
import models.SeasonResponse;
import modules.AuthModule;
import modules.FieldCreatorModule;
import modules.FieldDataModule;

public class AntelliseController implements Initializable {

    private GregorianCalendar startDate;
    private String filePath;
    private MainController mainController = new MainController();
    private boolean isSeasonListOpened = false;
    private HashMap<Integer, String> selectedSeasonItems = new HashMap<>();
    public static Stage stage;

    @FXML
    private VBox antellisBox;

    @FXML
    private Button antellisGenerateButton;

    @FXML
    private VBox seasonPickerContainer;
    // @FXML
    // private DatePicker startDatePicker;
    // @FXML
    // private DatePicker endDatePicker;
    @FXML
    public static Button antelliseSeasonButton;
    @FXML
    private Button antellisFilePathButton;
    @FXML
    private TextField antellisFilePathTextField;
    @FXML
    private Button antelliseFieldGenerateButton;

    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }
    //actions

    @FXML
    public void onClickSetFilePath(ActionEvent event){
        FileChooser dialogFilePath = new FileChooser();
        dialogFilePath.setInitialFileName("one_soil_file.zns");
        ExtensionFilter excelFilter = new ExtensionFilter("Antellise fields", "*.zns");
        ExtensionFilter modernExcelFilter = new ExtensionFilter("Google Earth file", ".kml");
        dialogFilePath.getExtensionFilters().addAll(excelFilter, modernExcelFilter);
        File excelPrecreatedFile = dialogFilePath.showSaveDialog(stage);
        if (excelPrecreatedFile != null) {
            filePath = excelPrecreatedFile.getAbsolutePath();
            antellisFilePathTextField.setText(filePath);
        }
    }

    // @FXML
    // public void onClickGetStartDate(ActionEvent event){
    //     DatePicker curDate = (DatePicker) event.getTarget();
    //     LocalDate date = curDate.getValue();
    //     startDate = new GregorianCalendar(date.getYear(), date.getMonthValue(), date.getDayOfMonth()); 
    //     System.out.println(MainController.selectedEmail);       
    // }
    // @FXML
    // public void onClickGetEndDate(ActionEvent event){

    // }

    @FXML
    public void onClickSeasonAction(){
        try{
            showSeasons();
        }catch(SeasonsException ex){
            mainController.showWarningMessage(ex.getMessage());
        }
    }

    private void showSeasons() throws SeasonsException{
        if(MainController.seasons == null){
            throw new SeasonsException(Warnings.NO_SEASONS_WARNING);
        }
        Pane pane = new Pane();
        pane.getStyleClass().add("main-pane");
        pane.setPrefHeight(350);
        ListView<String> seasonList = new ListView<>();
        seasonList.getStyleClass().add("antellise-season-list");
        isSeasonListOpened = isSeasonListOpened ? false : true;
        if(isSeasonListOpened){
            HashMap<String, String> map = new HashMap<>();
            ArrayList<String> seasonTitles = new ArrayList<>();
            for (Season season : MainController.seasons.getData()) {
                map.put(season.getTitle(), Integer.toString(season.getId()));
                seasonTitles.add(season.getTitle());
            }
            seasonList.getItems().setAll(seasonTitles);
            seasonList.setOnMouseClicked(itemEvent -> {
                if (selectedSeasonItems.get(seasonList.getSelectionModel().getSelectedIndex()) == null) {
                    selectedSeasonItems.put(seasonList.getSelectionModel().getSelectedIndex(),
                            map.get(seasonList.getSelectionModel().getSelectedItem()));
                } else {
                    selectedSeasonItems.remove(seasonList.getSelectionModel().getSelectedIndex());
                }
                System.out.println(selectedSeasonItems.values());
            });
            pane.getChildren().add(seasonList);
            seasonPickerContainer.getChildren().add(pane);
            System.out.println(seasonPickerContainer.getChildren());
        }else{
            seasonPickerContainer.getChildren().remove(seasonPickerContainer.getChildren().size()-1);
        }
    }
    @FXML
    public void onClickGenerateFieldFile(){
        try{
            generateFieldFile();
        }catch(MissingDateException dateException){
            mainController.showWarningMessage(dateException.getMessage());
        }catch(MissingFilePathException filePathException){
            mainController.showWarningMessage(filePathException.getMessage());
        }
    }

    private void generateFieldFile() throws MissingFilePathException, MissingDateException{
        if(startDate == null){
            throw new MissingDateException(Warnings.NO_DATE_WARNING);
        }
        if(filePath == null){
            throw new MissingFilePathException(Warnings.NO_FILE_WARNING);
        }
        if(MainController.selectedEmail == null){
            throw new MissingFilePathException(Warnings.NO_SEASONS_WARNING);
        }
        try{
        AuthModule module = new AuthModule();
        String token = module.getToken(mainController.getSelectedEmail());
        FieldCreatorModule antelliseModule = new FieldCreatorModule();
        FieldDataModule fieldModule = new FieldDataModule();
        String formattedToken = String.format("Token %s", token);
        SeasonResponse seasons = getSeasonResponse(token);
        ArrayList<FieldReaderResponse> fieldResponses = fieldModule.getFieldResponses(seasons,token);
        System.out.println(fieldResponses.size());
        }
        catch(InterruptedException ex){
            mainController.showWarningMessage(ex.getMessage());
        }
        // fieldModule.getFieldReaderResponse(token, token);
    }

    private SeasonResponse getSeasonResponse(String token){
        FieldDataModule fieldModule = new FieldDataModule();
        return fieldModule.getSeasonResponse(AuthModule.SEASON_URL, token);
    }

}
