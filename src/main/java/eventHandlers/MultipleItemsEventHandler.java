package eventHandlers;

import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class MultipleItemsEventHandler implements EventHandler<MouseEvent> {

    private ListView<String> seasonList;
    private HashMap<Integer, String> seasonIDMap;
    private HashMap<String, String> seasonTitlesMap;

    public MultipleItemsEventHandler() {

    }

    public MultipleItemsEventHandler(ListView<String> seasonList, HashMap<Integer, String> seasonIDMap,
            HashMap<String, String> seasonTitlesMap) {
        this.seasonList = seasonList;
        this.seasonIDMap = seasonIDMap;
        this.seasonTitlesMap = seasonTitlesMap;
    }

    @Override
    public void handle(MouseEvent arg0) {
        int selectedIndex = seasonList.getSelectionModel().getSelectedIndex();
        if (seasonIDMap.get(selectedIndex) == null) {
            seasonIDMap.put(selectedIndex,
                    seasonTitlesMap.get(seasonList.getSelectionModel().getSelectedItem()));
        } else {
            seasonIDMap.remove(seasonList.getSelectionModel().getSelectedIndex());
            seasonList.getSelectionModel().clearSelection(selectedIndex);
        }

    }

}
