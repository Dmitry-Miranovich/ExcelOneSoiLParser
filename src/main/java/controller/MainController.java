package controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.plaf.basic.BasicTabbedPaneUI.MouseHandler;

import constants.Emails;
import constants.ExcelHeaders;
import constants.Tokens;
import constants.Warnings;
import eventHandlers.MultipleItemsEventHandler;
import eventHandlers.SingleItemEventHandler;
import exceptions.MissingFilePathException;
import exceptions.SeasonsException;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.skin.ListViewSkin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.FieldReaderResponse;
import models.MultipleSelectionListView;
import models.NotePoint;
import models.NoteResponse;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import models.Season;
import models.SeasonResponse;
import modules.AuthModule;
import modules.ExcelWriterModule;
import modules.FieldDataModule;
import modules.FieldGuesserController;
import modules.ListViewState;
import modules.ProgressBarController;

public class MainController implements Initializable {

    private boolean isFilePathExist = false;
    private boolean isSeasonsLoaded = false;
    public static boolean isAllSeasonsApplied = false;

    public static String selectedEmail;
    private ToggleButton prevButton;
    private VBox prevBox;
    private boolean isSeasonListOpened = false;
    private boolean isNoteSeasonListOpened = false;
    private ArrayList<String> checkboxMarks = new ArrayList<>();
    public static HashMap<Integer, String> selectedSeasonItems = new HashMap<>();
    public static HashMap<Integer, String> selectedNoteSeasonsItems = new HashMap<>();
    public ArrayList<Integer> selectedIndexes = new ArrayList<>();
    private String token;
    public static Stage stage;
    public static SeasonResponse seasons;
    private String excelFilePath;
    private Pane seasonPane;
    private Pane noteSeasonPane;

    private ListViewState seasonListState = new ListViewState(false);
    private ListViewState noteSeasonListState = new ListViewState(false);
    private String currentSeasonNote;

    @FXML
    private Label emailLabel;
    @FXML
    private Button seasonsButton;
    @FXML
    private Button generatedButton;
    @FXML
    private Button filePathButton;
    @FXML
    private Button customButton;
    @FXML
    private TextField filePathTextField;

    @FXML
    public ToggleButton oneSoilButton;
    @FXML
    public ToggleButton antellisButton;
    // @FXML
    // public ToggleButton gEarthButton;

    // checkboxes

    @FXML
    public CheckBox fieldCheckBox;
    @FXML
    public CheckBox seasonCheckBox;
    @FXML
    public CheckBox notesCheckBox;

    // Panes for switching

    @FXML
    public VBox oneSoilBox;
    @FXML
    public VBox antellisBox;
    @FXML
    public VBox gEarthBox;
    @FXML
    public VBox defaultBox;
    @FXML
    private HBox oneSoilSeasonButtonsBox;
    @FXML
    private HBox listViewBox;

    @FXML
    public ComboBox<String> emailBox;
    @FXML
    public ProgressBar oneSoilProgressBar;
    @FXML
    private HBox progressBarHBox;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        emailBox.getItems().setAll(Emails.emails);
        oneSoilButton.setSelected(true);
        prevButton = oneSoilButton;
        prevBox = oneSoilBox;
        filePathTextField.setEditable(false);
        oneSoilSeasonButtonsBox.getStyleClass().add("onesoil-season-button-box");
    }

    @FXML
    public void handleButtonClick(ActionEvent event) {
        ToggleButton curButton = (ToggleButton) event.getTarget();
        curButton.setSelected(true);
        changeBox(curButton.getId());
        if (prevButton != null) {
            prevButton.setSelected(false);
        }
        prevButton = curButton;
    }

    @FXML
    public void handleCheckboxClick(MouseEvent event) {
        CheckBox currBox = (CheckBox) event.getSource();
        if (currBox == notesCheckBox) {
            noteCheckBoxAction(currBox);
        }
        if (!currBox.isSelected()) {
            checkboxMarks.add(currBox.getId());
        } else {
            checkboxMarks.remove(currBox.getId());
        }
    }

    private void noteCheckBoxAction(CheckBox currBox) {
        if (currBox.isSelected()) {
            ToggleButton noteSeasons = new ToggleButton("Выбрать целевой сезон");
            noteSeasons.getStyleClass().add("note-season-button");
            noteSeasons.setOnMouseClicked(event -> {
                // ToggleButton currButton = (ToggleButton) event.getSource();
                try {
                        noteSeasonListState.setIsListOpened(!noteSeasonListState.isListOpened());
                        noteSeasonListState.setListView(getListView());
                        selectSeasons(noteSeasonListState, new SingleItemEventHandler(noteSeasonListState, noteSeasonListState.getListView(), getTitleMap()));
                        // System.out.println(noteSeasonListState.getCurrentNote());
                } catch (SeasonsException ex) {
                    showWarningMessage(ex.getMessage());
                }
            });
            oneSoilSeasonButtonsBox.getChildren().add(noteSeasons);
        } else {
            oneSoilSeasonButtonsBox.getChildren().remove(oneSoilSeasonButtonsBox.getChildren().size() - 1);
        }
    }

    // Селект бокс для выбора нужного emaila
    @FXML
    public void onSelectEmail(ActionEvent event) {
        isAllSeasonsApplied = false;
        selectedSeasonItems.clear();
        ComboBox<String> currBox = (ComboBox) event.getTarget();
        selectedEmail = currBox.getSelectionModel().getSelectedItem();
        AuthModule authModule = new AuthModule();
        token = authModule.getToken(selectedEmail);
        isSeasonListOpened = false;
        if (seasonPane != null) {
            oneSoilBox.getChildren().remove(seasonPane);
        }
        if (token != Tokens.ALL_TOKENS) {
            FieldDataModule module = new FieldDataModule(token);
            seasons = module.getSeasonResponse(AuthModule.SEASON_URL, token);
            seasonsButton.setDisable(false);
            // AntelliseController.antelliseSeasonButton.setDisable(false);
            AntelliseController.isAllSeasonSelected = false;

        } else {
            isAllSeasonsApplied = true;
            seasonsButton.setDisable(true);
            // AntelliseController.antelliseSeasonButton.setDisable(true);
            AntelliseController.isAllSeasonSelected = true;
        }

    }

    @FXML
    public void onClickSeasonButton(ActionEvent event) {
        try {
            seasonListState.setIsListOpened(!seasonListState.isListOpened());
            HashMap<String, String> titleMap = getTitleMap();
            seasonListState.setListView(getListView());
            selectSeasons(seasonListState, new MultipleItemsEventHandler(seasonListState.getListView(), selectedSeasonItems, titleMap));
        } catch (SeasonsException ex) {
            showWarningMessage(ex.getMessage());
        }
    }

    @FXML
    public void onClickGenerateReport(ActionEvent event) {
        try {
            oneSoilProgressBar.setProgress(0.0);
            writeSelectedData();
        } catch (MissingFilePathException ex) {
            showWarningMessage(ex.getMessage());
        } catch (SeasonsException s_exception) {
            showWarningMessage(s_exception.getMessage());
        }
    }

    private void writeSelectedData() throws MissingFilePathException, SeasonsException {
        if (!isSeasonsLoaded) {
            throw new MissingFilePathException(Warnings.NO_FILE_WARNING);
        }
        if (seasons == null && !isAllSeasonsApplied) {
            throw new SeasonsException(Warnings.NO_SEASONS_WARNING);
        }
        if (selectedSeasonItems.size() < 0) {
            throw new SeasonsException(Warnings.NO_SEASONS_SELECTED_WARNING);
        }
        if(noteSeasonListState.getCurrentNote() == null){
            throw new SeasonsException(Warnings.NO_NOTE_SEASON_STRING);
        }
        ExcelWriterModule excelBook = new ExcelWriterModule(excelFilePath);
        FieldDataModule mainModule = new FieldDataModule(excelBook);
        if (isAllSeasonsApplied) {
            getEveryOneSoilData(mainModule, excelBook);
        } else {
            getCertainOneSoilData(mainModule, excelBook);
        }
    }

    public void selectSeasons(ListViewState state, EventHandler<MouseEvent> currHandler)
            throws SeasonsException {
        if (seasons == null) {
            throw new SeasonsException(Warnings.NO_SEASONS_WARNING);
        }
        addSeasonList(state, currHandler);
    }

    private void addSeasonList(ListViewState state, EventHandler<MouseEvent> currEvent) {
        if (state.isListOpened()) {
            // state.setIsListOpened(state.isListOpened() ? false : true);
            state.setCurrPane(new Pane());
            state.getCurrPane().getStyleClass().add("main-pane");
            state.getCurrPane().setPrefHeight(350);

            state.getListView().setOnMouseClicked(currEvent);
            state.getCurrPane().getChildren().add(state.getListView());
            listViewBox.getChildren().add(state.getCurrPane());
        } else {
            listViewBox.getChildren().remove(state.getCurrPane());
        }
    }

    private HashMap<String, String> getTitleMap() throws SeasonsException{
        if(seasons == null){
            throw new SeasonsException(Warnings.NO_SEASONS_WARNING);
        }
        HashMap<String, String> map = new HashMap<>();
            for (Season season : seasons.getData()) {
                map.put(season.getTitle(), Integer.toString(season.getId()));
            }
        return map;
    }
    private ArrayList<String> getSeasonList() throws SeasonsException{
        if(seasons == null){
            throw new SeasonsException(Warnings.NO_SEASONS_WARNING);
        }
            ArrayList<String> seasonTitles = new ArrayList<>();
            for (Season season : seasons.getData()) {
                seasonTitles.add(season.getTitle());
            }
        return seasonTitles;
    }

    private ListView<String> getListView(){
        ListView<String> seasonList = new ListView<>();
        try{
            seasonList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            seasonList.getStyleClass().add("season-list");
            seasonList.getItems().setAll(getSeasonList());
        }catch(SeasonsException ex){
            showWarningMessage(Warnings.NO_SEASONS_WARNING);
        }
        return seasonList;
    }

    @FXML
    public void onClickSearchFilePath() {
        FileChooser dialogFilePath = new FileChooser();
        dialogFilePath.setInitialFileName("one_soil_file.xls");
        ExtensionFilter excelFilter = new ExtensionFilter("Excel files (2003)", "*.xls");
        ExtensionFilter modernExcelFilter = new ExtensionFilter("Excel files (2010)", ".xlsx");
        dialogFilePath.getExtensionFilters().addAll(excelFilter, modernExcelFilter);
        File excelPrecreatedFile = dialogFilePath.showSaveDialog(stage);
        if (excelPrecreatedFile != null) {
            excelFilePath = excelPrecreatedFile.getAbsolutePath();
            isSeasonsLoaded = true;
            filePathTextField.setText(excelFilePath);
        }
    }

    private void changeBox(String id) {
        VBox curBox = null;
        switch (id) {
            case "oneSoilButton": {
                curBox = oneSoilBox;
                break;
            }
            case "antellisButton": {
                curBox = antellisBox;
                break;
            }
            case "gEarthButton": {
                curBox = gEarthBox;
                break;
            }
            default: {
                curBox = defaultBox;
            }
        }
        if (prevBox != null) {
            prevBox.setVisible(false);
        }
        if (seasonPane != null) {
            oneSoilBox.getChildren().remove(seasonPane);
            selectedSeasonItems.clear();
        }
        prevBox = curBox;
        curBox.setVisible(true);
    }

    public void showWarningMessage(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setHeaderText("Ошибка");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void getEveryOneSoilData(FieldDataModule mainModule, ExcelWriterModule writerModule) {
        try {
            AuthModule authModule = new AuthModule();
            ArrayList<SeasonResponse> seasonResponses = new ArrayList<>();
            ArrayList<ArrayList<FieldReaderResponse>> fieldResponses = new ArrayList<>();
            ArrayList<NoteResponse> noteResponses = new ArrayList<>();
            for (int i = 0; i < Emails.emails.length - 1; i++) {
                String token = authModule.getToken(Emails.emails[i]);
                mainModule.setEmail(Emails.emails[i]);
                if (seasonCheckBox.isSelected()) {
                    SeasonResponse response = mainModule.getSeasonResponse(AuthModule.SEASON_URL, token);
                    response.setEmail(Emails.emails[i]);
                    seasonResponses.add(response);
                }
                if (fieldCheckBox.isSelected()) {
                    SeasonResponse currResponse = mainModule.getSeasonResponse(AuthModule.SEASON_URL, token);
                    fieldResponses.add(mainModule.getFieldResponses(currResponse, token));
                }
                if (notesCheckBox.isSelected()) {
                    noteResponses.add(mainModule.getNoteResponse(AuthModule.NOTES_URL, token));
                }
            }
            FieldGuesserController fieldGuesserController = new FieldGuesserController(noteResponses, fieldResponses);
            fieldGuesserController.appendNearestFieldByNote(noteSeasonListState.getCurrentNote());
            writeExcelData(writerModule, seasonResponses, fieldResponses, noteResponses);
        } catch (InterruptedException intException) {
            showWarningMessage(intException.getMessage());
        }
    }

    private void getCertainOneSoilData(FieldDataModule mainModule, ExcelWriterModule writerModule) {
        ArrayList<SeasonResponse> seasonsResponsesList = new ArrayList<>();
        ArrayList<ArrayList<FieldReaderResponse>> fieldResponsesList = new ArrayList<>();
        ArrayList<NoteResponse> noteResponseList = new ArrayList<>();
        if (seasonCheckBox.isSelected()) {
            // season action
            SeasonResponse response = mainModule.getSeasonResponse(AuthModule.SEASON_URL, token);
            response.setEmail(selectedEmail);
            seasonsResponsesList.add(response);
        }
        mainModule.setEmail(selectedEmail);
        Thread[] threads = new Thread[selectedSeasonItems.size()];
        int index = 0;
        for (String seasons : selectedSeasonItems.values()) {
            Thread n_Thread = new Thread(new Runnable() {
                public void run() {
                    if (fieldCheckBox.isSelected()) {
                        fieldResponsesList.add(mainModule.getFieldReaderResponse(token, seasons));
                        ProgressBarController.updateProgressBar(oneSoilProgressBar, 1.0 / selectedSeasonItems.size());
                    }
                    if (notesCheckBox.isSelected()) {
                        if(!fieldCheckBox.isSelected()){
                            fieldResponsesList.add(mainModule.getFieldReaderResponse(token, seasons));
                        } 
                        String seasonNotesURL = String.format("%s?filter%%7Bseason_id%%7D=%s", AuthModule.NOTES_URL,
                                seasons);
                        noteResponseList.add(mainModule.getNoteResponse(seasonNotesURL, token));
                    }
                }
            });
            threads[index] = n_Thread;
            threads[index].start();
            index++;
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        FieldGuesserController fieldGuesserController = new FieldGuesserController(noteResponseList,
                fieldResponsesList);
        fieldGuesserController.appendNearestFieldByNote(noteSeasonListState.getCurrentNote());
        writeExcelData(writerModule, seasonsResponsesList, fieldResponsesList, noteResponseList);
    }

    private void writeExcelData(ExcelWriterModule excelBook, ArrayList<SeasonResponse> seasonsResponsesList,
            ArrayList<ArrayList<FieldReaderResponse>> fieldResponsesList, ArrayList<NoteResponse> noteResponseList) {
        if (fieldCheckBox.isSelected()) {
            excelBook.writeAllFields(fieldResponsesList);
        }
        if (seasonCheckBox.isSelected()) {
            excelBook.writeAllSeasons(seasonsResponsesList);
        }
        if (notesCheckBox.isSelected()) {
            excelBook.writeAllNotes(noteResponseList);
        }
        excelBook.writeFile(excelBook.getBook());
    }

    public ToggleButton getPrevButton() {
        return prevButton;
    }

    public void setPrevButton(ToggleButton prevButton) {
        this.prevButton = prevButton;
    }

    public String getSelectedEmail() {
        return selectedEmail;
    }

    public Stage getStage() {
        return stage;
    }

}
