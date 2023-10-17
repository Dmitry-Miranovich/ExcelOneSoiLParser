package models;

import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;

public class MultipleSelectionListView<T> extends ListView<T> {
        public MultipleSelectionListView() {
            setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    int selectedItem = getSelectionModel().getSelectedIndex();
                    if (getSelectionModel().getSelectedItems().contains(selectedItem)) {
                        getSelectionModel().clearSelection(selectedItem);
                    } else {
                        getSelectionModel().select(selectedItem);
                    }
                }
            });
        }
    }
