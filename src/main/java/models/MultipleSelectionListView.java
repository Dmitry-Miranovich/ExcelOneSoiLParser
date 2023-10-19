package models;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.util.Callback;

public class MultipleSelectionListView<T> extends ListView<T> {

    public MultipleSelectionListView() {
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setCellFactory(new Callback<ListView<T>, ListCell<T>>() {

            @Override
            public ListCell<T> call(ListView<T> arg0) {

                return new ListCell<>() {

                    @Override
                    public void updateSelected(boolean arg) {
                        super.updateSelected(arg);
                        if(getStyleClass().get(getStyleClass().size()-1) != "selected"){
                            getStyleClass().add("selected");
                        }else{
                            getStyleClass().remove(getStyleClass().size()-1);
                        }
                    }

                    @Override
                    protected void updateItem(T arg0, boolean arg1) {
                        super.updateItem(arg0, arg1);
                        if (arg0 != null) {
                            // Здесь вы можете настроить отображение элемента item в ячейке
                            setText(arg0.toString()); // Например, установите текст элемента
                        } else {
                            setText(null);
                        }
                    }
                };

            }
        }
        );
    }

}
