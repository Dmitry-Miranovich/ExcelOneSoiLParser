package controller;

import java.io.File;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import constants.KmlEnum;
import constants.Warnings;
import eventHandlers.SingleItemEventHandler;
import exceptions.MissingFilePathException;
import exceptions.SeasonsException;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import models.Field;
import models.FieldReaderResponse;
import models.FieldsData;
import models.Season;
import models.SeasonResponse;
import modules.AuthModule;
import modules.FieldCreatorModule;
import modules.FieldDataModule;
import modules.KmlCreatorController;
import modules.ListViewState;

public class AntelliseController implements Initializable {

    // private GregorianCalendar startDate;
    private String filePath;
    private MainController mainController = new MainController();
    // private boolean isSeasonListOpened = false;
    // private HashMap<Integer, String> selectedSeasonItems = new HashMap<>();
    private String fileExtention;
    public static Stage stage;
    public static boolean isAllSeasonSelected = false;
    public static Button proxySeasonButton;
    private ListViewState seasonListViewState = new ListViewState(false);

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
    private Button antelliseSeasonButton;
    @FXML
    private Button antellisFilePathButton;
    @FXML
    private TextField antellisFilePathTextField;
    @FXML
    private Button antelliseFieldGenerateButton;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        antellisBox.visibleProperty().addListener((some) -> {
            BooleanProperty prop = (BooleanProperty) some;
            if (prop.getValue() == true) {
                System.out.println(isAllSeasonSelected);
            }
        });
    }
    // actions

    @FXML
    public void onClickSetFilePath(ActionEvent event) {
        FileChooser dialogFilePath = new FileChooser();
        dialogFilePath.setInitialFileName("one_soil_file.zns");
        ExtensionFilter excelFilter = new ExtensionFilter("Antellise fields", "*.zns");
        ExtensionFilter modernExcelFilter = new ExtensionFilter("Google Earth file", ".kml");
        dialogFilePath.getExtensionFilters().addAll(excelFilter, modernExcelFilter);
        File excelPrecreatedFile = dialogFilePath.showSaveDialog(stage);
        if (excelPrecreatedFile != null) {
            filePath = excelPrecreatedFile.getAbsolutePath();
            fileExtention = getFileExtention(filePath);
            antellisFilePathTextField.setText(filePath);
        }
    }

    private String getFileExtention(String filePath){
        int extentionIndex = 0;
        for(int i = filePath.length()-1; i>=0; i--){
            if(filePath.charAt(i) == '.'){
                extentionIndex = i;
                break;
            }
        }
        return filePath.substring(extentionIndex, filePath.length());
    }

    // @FXML
    // public void onClickGetStartDate(ActionEvent event){
    // DatePicker curDate = (DatePicker) event.getTarget();
    // LocalDate date = curDate.getValue();
    // startDate = new GregorianCalendar(date.getYear(), date.getMonthValue(),
    // date.getDayOfMonth());
    // System.out.println(MainController.selectedEmail);
    // }
    // @FXML
    // public void onClickGetEndDate(ActionEvent event){

    // }

    @FXML
    public void onClickSeasonAction() {
        try {
            if (!seasonListViewState.isListOpened()) {
                ListView<String> listView = getListView(SelectionMode.SINGLE);
                HashMap<String, String> seasonMap = getTitleMap();
                listView.getItems().setAll(getSeasonList());
                seasonListViewState.setCurrPane(new Pane());
                seasonListViewState.getCurrPane().getChildren().add(listView);
                seasonListViewState.setListView(listView);
                showSeasons(new SingleItemEventHandler(seasonListViewState, listView, seasonMap));
            } else {
                seasonPickerContainer.getChildren().remove(seasonListViewState.getCurrPane());
                seasonListViewState.setIsListOpened(false);
            }
        } catch (SeasonsException ex) {
            mainController.showWarningMessage(ex.getMessage());
        }
    }

    private void showSeasons(EventHandler<MouseEvent> handler) throws SeasonsException {
        if (MainController.seasons == null) {
            throw new SeasonsException(Warnings.NO_SEASONS_WARNING);
        }
        seasonListViewState.getCurrPane().getStyleClass().add("antellise-main-pane");
        seasonListViewState.getListView().getStyleClass().add("antellise-season-list");
        seasonListViewState.setIsListOpened(true);
        seasonListViewState.getListView().setOnMouseClicked(handler);
        seasonPickerContainer.getChildren().add(seasonListViewState.getCurrPane());

        // seasonListViewState.getCurrPane().setPre
        // Pane pane = new Pane();
        // pane.getStyleClass().add("main-pane");
        // pane.setPrefHeight(350);
        // ListView<String> seasonList = new ListView<>();
        // seasonList.getStyleClass().add("antellise-season-list");
        // isSeasonListOpened = isSeasonListOpened ? false : true;
        // if(isSeasonListOpened){
        // HashMap<String, String> map = new HashMap<>();
        // ArrayList<String> seasonTitles = new ArrayList<>();
        // for (Season season : MainController.seasons.getData()) {
        // map.put(season.getTitle(), Integer.toString(season.getId()));
        // seasonTitles.add(season.getTitle());
        // }
        // seasonList.getItems().setAll(seasonTitles);
        // seasonList.setOnMouseClicked(itemEvent -> {
        // if
        // (selectedSeasonItems.get(seasonList.getSelectionModel().getSelectedIndex())
        // == null) {
        // selectedSeasonItems.put(seasonList.getSelectionModel().getSelectedIndex(),
        // map.get(seasonList.getSelectionModel().getSelectedItem()));
        // } else {
        // selectedSeasonItems.remove(seasonList.getSelectionModel().getSelectedIndex());
        // }
        // System.out.println(selectedSeasonItems.values());
        // });
        // pane.getChildren().add(seasonList);
        // seasonPickerContainer.getChildren().add(pane);
        // System.out.println(seasonPickerContainer.getChildren());
        // }else{
        // seasonPickerContainer.getChildren().remove(seasonPickerContainer.getChildren().size()-1);
        // }
    }

    @FXML
    public void onClickGenerateFieldFile() {
        try {
            generateFieldFile();
        } catch (SeasonsException dateException) {
            mainController.showWarningMessage(dateException.getMessage());
        } catch (MissingFilePathException filePathException) {
            mainController.showWarningMessage(filePathException.getMessage());
        }
    }

    

    private HashMap<String, String> getTitleMap() throws SeasonsException {
        if (MainController.seasons == null) {
            throw new SeasonsException(Warnings.NO_SEASONS_WARNING);
        }
        HashMap<String, String> map = new HashMap<>();
        for (Season season : MainController.seasons.getData()) {
            map.put(season.getTitle(), Integer.toString(season.getId()));
        }
        return map;
    }

    private ArrayList<String> getSeasonList() throws SeasonsException {
        if (MainController.seasons == null) {
            throw new SeasonsException(Warnings.NO_SEASONS_WARNING);
        }
        ArrayList<String> seasonTitles = new ArrayList<>();
        for (Season season : MainController.seasons.getData()) {
            seasonTitles.add(season.getTitle());
        }
        return seasonTitles;
    }

    private ListView<String> getListView(SelectionMode mode) {
        ListView<String> seasonList = new ListView<>();
        try {
            seasonList.getSelectionModel().setSelectionMode(mode);
            seasonList.getStyleClass().add("season-list");
            seasonList.getItems().setAll(getSeasonList());
        } catch (SeasonsException ex) {
            MainController controller = new MainController();
            controller.showWarningMessage(Warnings.NO_SEASONS_WARNING);
        }
        return seasonList;
    }

    private void generateFieldFile() throws MissingFilePathException, SeasonsException {
        // if (startDate == null) {
        //     throw new MissingDateException(Warnings.NO_DATE_WARNING);
        // }
        if (filePath == null) {
            throw new MissingFilePathException(Warnings.NO_FILE_WARNING);
        }
        if (MainController.selectedEmail == null) {
            throw new MissingFilePathException(Warnings.NO_SEASONS_WARNING);
        }
        if(MainController.seasons == null){
            throw new SeasonsException(Warnings.NO_SEASON_STRING);
        }
        AuthModule module = new AuthModule();
        String token = module.getToken(mainController.getSelectedEmail());
        // FieldCreatorModule antelliseModule = new FieldCreatorModule();
        FieldDataModule fieldModule = new FieldDataModule();
        // SeasonResponse seasons = getSeasonResponse(token);
        ArrayList<FieldReaderResponse> fieldResponses = fieldModule.getFieldReaderResponse(token,
                seasonListViewState.getCurrentNote());
        if(fileExtention.equals(".kml")){
            createKMLFile(fieldResponses);
        }else if(fileExtention.equals(".zns")){
            createZnsFile(fieldResponses);
        }

    }

    private void createKMLFile(ArrayList<FieldReaderResponse> fieldResponses) {
        KmlCreatorController controller = new KmlCreatorController();
        String headerProp = "xmlns=\"http://www.opengis.net/kml/2.2\"";
        String styleURL = "#AREA_FFFFFFFF";
        String styleID = "AREA_FFFFFFFF";
        String styleColorLine = "FFFFFFFF";
        String styleColorPoly = "80FFFFFF";
        String widthStyle = "2";
        String fillStyle = "1";
        String outlineStyle = "1";
        controller.setProp(KmlEnum.KML, headerProp);
        controller.createKmlStyle(styleID, styleColorLine, widthStyle, styleColorPoly, fillStyle, outlineStyle);
        for (FieldReaderResponse response : fieldResponses) {
            FieldsData fieldsData = response.getData();
            for (Field field : fieldsData.getRows()) {
                String[] fieldCoordinates = new String[field.getRealCoordinates().length];
                int index = 0;
                for (float[] coords : field.getRealCoordinates()) {
                    fieldCoordinates[index] = (String.format(
                            (index != fieldCoordinates.length - 1 ? " %s,%s," : " %s,%s"), coords[0],
                            coords[1]));
                    index++;
                }
                controller.createSpecialFieldPlacemark(field.getTitle(), String.format("%s группа", field.getTitle()),
                        styleURL, fieldCoordinates);
            }
        }
        controller.endKML();
        controller.writeFile(controller.getFullKMLString(), filePath);
    }

    private void createZnsFile(ArrayList<FieldReaderResponse> fieldResponses){
        FieldCreatorModule antellisModule = new FieldCreatorModule();
        for (FieldReaderResponse response : fieldResponses) {
            FieldsData fieldsData = response.getData();
            for (Field field : fieldsData.getRows()) {
                antellisModule.writeAntellisField(field);
            }
        }
        antellisModule.writeFile(filePath);
    }

    private SeasonResponse getSeasonResponse(String token) {
        FieldDataModule fieldModule = new FieldDataModule();
        return fieldModule.getSeasonResponse(AuthModule.SEASON_URL, token);
    }

}
