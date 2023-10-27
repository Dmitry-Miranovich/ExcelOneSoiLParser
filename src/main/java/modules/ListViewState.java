package modules;

import java.util.HashMap;

import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

public class ListViewState {
    private boolean isListOpened;
    private Pane currPane;
    private HashMap<Integer, String> map;
    private String currentNote;
    private ListView<String> listView;
    
    public ListViewState(){}
    public ListViewState(boolean listState){
        this.isListOpened = listState;
    }

    public ListViewState(boolean listState, Pane currPane, HashMap<Integer, String> map, ListView<String> listView) {
        this.isListOpened = listState;
        this.currPane = currPane;
        this.map = map;
        this.listView = listView;
    }

    

    public ListViewState(boolean isListOpened, Pane currPane, String currentNote , ListView<String> listView) {
        this.isListOpened = isListOpened;
        this.currPane = currPane;
        this.currentNote = currentNote;
        this.listView = listView;
    }
    public boolean isListOpened() {
        return isListOpened;
    }

    public void setIsListOpened(boolean listState) {
        this.isListOpened = listState;
    }

    public Pane getCurrPane() {
        return currPane;
    }

    public void setCurrPane(Pane currPane) {
        this.currPane = currPane;
    }

    public HashMap<Integer, String> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer, String> map) {
        this.map = map;
    }
    public void setListOpened(boolean isListOpened) {
        this.isListOpened = isListOpened;
    }
    public String getCurrentNote() {
        return currentNote;
    }
    public void setCurrentNote(String currentNote) {
        this.currentNote = currentNote;
    }
    public ListView<String> getListView() {
        return listView;
    }
    public void setListView(ListView<String> listView) {
        this.listView = listView;
    }

    
}
