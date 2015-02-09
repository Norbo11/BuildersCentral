package com.github.norbo11.topbuilders.util.factories;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableCell;
import javafx.util.StringConverter;

import com.github.norbo11.topbuilders.util.Log;

public class TextFieldTreeCell<T, S> extends TreeTableCell<T, S> {
    private TextField textField;
	private StringConverter<S> converter;

	public TextFieldTreeCell(StringConverter<S> converter) {
		this.converter = converter;
	}

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getString());
        setGraphic(null);
    }

    @Override
    public void updateItem(S item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(null);
            }
        }
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
        textField.setOnAction(e -> commit());
        textField.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //If it lost focus then commit
                if (!newValue) {
                	commit();
                }
            }
        });
    }

    private void commit() {
    	commitEdit(converter.fromString(textField.getText()));
    }
    
    private String getString() {
    	Log.info(getItem());
        return getItem() == null ? "" : converter.toString(getItem());
    }
}