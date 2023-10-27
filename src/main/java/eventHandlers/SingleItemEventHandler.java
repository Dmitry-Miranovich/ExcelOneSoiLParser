package eventHandlers;

import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import modules.ListViewState;

public class SingleItemEventHandler implements EventHandler<MouseEvent> {
    
    private ListViewState selectedId;
    private ListView<String> seasonList;
    private HashMap<String, String> seasonMap;

    public SingleItemEventHandler(){

    } 

    public SingleItemEventHandler(ListViewState selectedId, ListView<String> seasonList, HashMap<String, String> seasonMap) {
        this.selectedId = selectedId;
        this.seasonList = seasonList;
        this.seasonMap = seasonMap;
    }

    public ListViewState getSelectedId() {
        return selectedId;
    }



    public void setSelectedId(ListViewState selectedId) {
        this.selectedId = selectedId;
    }



    public ListView<String> getSeasonList() {
        return seasonList;
    }



    public void setSeasonList(ListView<String> seasonList) {
        this.seasonList = seasonList;
    }



    public HashMap<String, String> getSeasonMap() {
        return seasonMap;
    }



    public void setSeasonMap(HashMap<String, String> seasonMap) {
        this.seasonMap = seasonMap;
    }



    @Override
    public void handle(MouseEvent arg0) {
        selectedId.setCurrentNote(seasonMap.get(seasonList.getSelectionModel().getSelectedItem()));
    }
}
