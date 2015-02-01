package com.github.norbo11.topbuilders.util;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

import com.github.norbo11.topbuilders.models.AbstractModel;

public class DeleteModelButtonCellFactory<T extends AbstractModel> extends TableCell<T, T> {
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText("");
            setGraphic(null);
        } else {
            Button button = new Button("X");
            button.setOnAction(e -> item.delete());
            setGraphic(button); //setGraphic allows me to set an actual node instead of text for these cell contents
        }
    }

    public static <T extends AbstractModel> void assignCellFactory(TableColumn<T, T> column) {
        //Set the cell value of each "delete model" cell to contain the actual message object, for retrieval by the cell factory
        column.setCellFactory(param -> new DeleteModelButtonCellFactory<T>());
        
        //Set the custom cell factory for delete model button cells
        column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<T>(param.getValue()));
    }
}