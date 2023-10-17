package controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections4.iterators.ArrayListIterator;
import org.controlsfx.control.CheckComboBox;

import constants.Emails;
import constants.ExcelHeaders;
import constants.Tokens;
import constants.Warnings;
import exceptions.MissingFilePathException;
import exceptions.SeasonsException;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import modules.ProgressBarController;

public class MainController implements Initializable {

    private boolean isFilePathExist = false;
    private boolean isSeasonsLoaded = false;
    public static boolean isAllSeasonsApplied = false;

    public static String selectedEmail;
    private ToggleButton prevButton;
    private VBox prevBox;
    private boolean isSeasonListOpened = false;
    private ArrayList<String> checkboxMarks = new ArrayList<>();
    public static HashMap<Integer, String> selectedSeasonItems = new HashMap<>();
    public ArrayList<Integer> selectedIndexes = new ArrayList<>();
    private String token;
    public static Stage stage;
    public static SeasonResponse seasons;
    private String excelFilePath;
    private Pane seasonPane;

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
    private FlowPane pane;
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
        if(currBox == notesCheckBox){
            noteCheckBoxAction(currBox);
        }
        if (!currBox.isSelected()) {
            checkboxMarks.add(currBox.getId());
        } else {
            checkboxMarks.remove(currBox.getId());
        }
    }
    private void noteCheckBoxAction(CheckBox currBox){
        if(currBox.isSelected()){     
            ToggleButton noteSeasons = new ToggleButton("Выбрать целевой сезон");
            noteSeasons.getStyleClass().add("note-season-button");
            oneSoilSeasonButtonsBox.getChildren().add(noteSeasons);
        }else{
            oneSoilSeasonButtonsBox.getChildren().remove(oneSoilSeasonButtonsBox.getChildren().size()-1);
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
        } else {
            isAllSeasonsApplied = true;
            seasonsButton.setDisable(true);
            // AntelliseController.antelliseSeasonButton.setDisable(true);
        }

    }

    @FXML
    public void onClickSeasonButton(ActionEvent event) throws SeasonsException {
        try {
            selectSeasons();
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
        ExcelWriterModule excelBook = new ExcelWriterModule(excelFilePath);
        FieldDataModule mainModule = new FieldDataModule(excelBook);
        if (isAllSeasonsApplied) {
            getEveryOneSoilData(mainModule, excelBook);
        } else {
            getCertainOneSoilData(mainModule, excelBook);
        }
    }

    public void selectSeasons() throws SeasonsException {
        if (seasons == null) {
            throw new SeasonsException(Warnings.NO_SEASONS_WARNING);
        }
        isSeasonListOpened = isSeasonListOpened ? false : true;
        seasonPane = new Pane();
        seasonPane.getStyleClass().add("main-pane");
        ListView<String> seasonList = new MultipleSelectionListView<>();
        seasonPane.setPrefHeight(350);
        seasonList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        seasonList.getStyleClass().add("season-list");
        if (isSeasonListOpened) {
            HashMap<String, String> map = new HashMap<>();
            ArrayList<String> seasonTitles = new ArrayList<>();
            for (Season season : seasons.getData()) {
                map.put(season.getTitle(), Integer.toString(season.getId()));
                seasonTitles.add(season.getTitle());
            }
            seasonList.getItems().setAll(seasonTitles);
            seasonList.setOnMouseClicked(itemEvent -> {
                int selectedIndex = seasonList.getSelectionModel().getSelectedIndex();
                if (selectedSeasonItems.get(selectedIndex) == null) {
                    selectedSeasonItems.put(selectedIndex,
                            map.get(seasonList.getSelectionModel().getSelectedItem()));
                } else {
                    selectedSeasonItems.remove(seasonList.getSelectionModel().getSelectedIndex());
                    seasonList.getSelectionModel().clearSelection(selectedIndex);
                }
            });
            seasonPane.getChildren().add(seasonList);
            oneSoilBox.getChildren().add(oneSoilBox.getChildren().size() - 1, seasonPane);
        } else {
            oneSoilBox.getChildren().remove(oneSoilBox.getChildren().size() - 2);
        }
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
            fieldGuesserController.appendNearestFieldByNote();
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
                        fieldResponsesList.add(mainModule.getFieldReaderResponse(token, seasons));
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
        fieldGuesserController.appendNearestFieldByNote();
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
