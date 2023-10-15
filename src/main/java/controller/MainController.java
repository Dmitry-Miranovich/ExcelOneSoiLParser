package controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckComboBox;

import constants.Emails;
import constants.ExcelHeaders;
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
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
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
import models.NoteResponse;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import models.Season;
import models.SeasonResponse;
import modules.AuthModule;
import modules.ExcelWriterModule;
import modules.FieldDataModule;
import modules.ProgressBarController;

public class MainController implements Initializable {

    private boolean isFilePathExist = false;
    private boolean isSeasonsLoaded = false;

    private String selectedEmail;
    private ToggleButton prevButton;
    private VBox prevBox;
    private boolean isSeasonListOpened = false;
    private ArrayList<String> checkboxMarks = new ArrayList<>();
    private HashMap<Integer, String> selectedSeasonItems = new HashMap<>();
    private String token;
    public static Stage stage;
    private SeasonResponse seasons;
    private String excelFilePath;

    @FXML
    private Label emailLabel;
    @FXML
    private Button seasonsButton;
    @FXML
    private Button generatedButton;
    @FXML
    private Button filePathButton;
    @FXML
    private TextField filePathTextField;

    @FXML
    private FlowPane pane;
    @FXML
    public ToggleButton oneSoilButton;
    @FXML
    public ToggleButton antellisButton;
    @FXML
    public ToggleButton gEarthButton;

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
        if (!currBox.isSelected()) {
            checkboxMarks.add(currBox.getId());
        } else {
            checkboxMarks.remove(currBox.getId());
        }
    }

    // Селект бокс для выбора нужного emaila
    @FXML
    public void onSelectEmail(ActionEvent event) {
        ComboBox<String> currBox = (ComboBox) event.getTarget();
        selectedEmail = currBox.getSelectionModel().getSelectedItem();
        AuthModule authModule = new AuthModule();
        token = authModule.getToken(selectedEmail);
        FieldDataModule module = new FieldDataModule(token);
        seasons = module.getSeasonResponse(AuthModule.SEASON_URL, token);
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
        if (seasons == null) {
            throw new SeasonsException(Warnings.NO_SEASONS_WARNING);
        }
        if (selectedSeasonItems.size() < 0) {
            throw new SeasonsException(Warnings.NO_SEASONS_SELECTED_WARNING);
        }
        ExcelWriterModule excelBook = new ExcelWriterModule(excelFilePath);
        FieldDataModule mainModule = new FieldDataModule(excelBook);
        ArrayList<SeasonResponse> seasonsResponsesList = new ArrayList<>();
        ArrayList<ArrayList<FieldReaderResponse>> fieldResponsesList = new ArrayList<>();
        if (seasonCheckBox.isSelected()) {
            // season action
            seasonsResponsesList.add(mainModule.getSeasonResponse(AuthModule.SEASON_URL, token));
        }
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
                        // note action
                        System.out.println("note action");
                    }
                }
            });
            threads[index] = n_Thread;
            threads[index].start();
            index++;
        }
        for(Thread t : threads){
            try{
                t.join();
            }catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
        excelBook.writeAllSeasons(seasonsResponsesList);
        excelBook.writeAllFields(fieldResponsesList);
        excelBook.writeFile(excelBook.getBook());
    }

    private void selectSeasons() throws SeasonsException {
        if (seasons == null) {
            throw new SeasonsException(Warnings.NO_SEASONS_WARNING);
        }
        isSeasonListOpened = isSeasonListOpened ? false : true;
        Pane pane = new Pane();
        ListView<String> seasonList = new ListView<>();
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
                if (selectedSeasonItems.get(seasonList.getSelectionModel().getSelectedIndex()) == null) {
                    selectedSeasonItems.put(seasonList.getSelectionModel().getSelectedIndex(),
                            map.get(seasonList.getSelectionModel().getSelectedItem()));
                } else {
                    selectedSeasonItems.remove(seasonList.getSelectionModel().getSelectedIndex());
                }
                System.out.println(selectedSeasonItems.values());
            });
            pane.getChildren().add(seasonList);
            oneSoilBox.getChildren().add(oneSoilBox.getChildren().size() - 1, pane);
        } else {
            oneSoilBox.getChildren().remove(oneSoilBox.getChildren().size() - 2);
        }
    }

    // todo
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
        prevBox = curBox;
        curBox.setVisible(true);
    }

    public void showWarningMessage(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setHeaderText("Ошибка");
        alert.setContentText(message);
        alert.showAndWait();
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

    public void setSelectedEmail(String selectedEmail) {
        this.selectedEmail = selectedEmail;
    }

    public Stage getStage() {
        return stage;
    }

}
