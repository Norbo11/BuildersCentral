package com.github.norbo11.topbuilders.util;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class StylableComboBox<T> extends ComboBox<T> {

    public StylableComboBox() {
        super();
        
        
        setCellFactory(p -> {
            return new ListCell<T>() {
                {
                    getStyleClass().add("styled-list-cell");
                }
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    Log.error("testing");
                    setText(item.toString());
                    setTextFill(Color.RED);
                    setFont(Font.font(16));
                }
            };
        });
    }
}
