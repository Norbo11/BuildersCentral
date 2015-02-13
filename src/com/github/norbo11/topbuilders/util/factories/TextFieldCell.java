package com.github.norbo11.topbuilders.util.factories;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.StringConverter;

import com.github.norbo11.topbuilders.controllers.custom.DoubleTextField;

public class TextFieldCell<T, S> extends TableCell<T, S> {
    private TextField textField;
	private StringConverter<S> converter;

	public TextFieldCell(StringConverter<S> converter) {
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
        if (getItem() instanceof Double) textField = new DoubleTextField((Double) getItem());
        else textField = new TextField(getString());
        
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
        textField.setOnKeyPressed(e -> { if (e.getCode().equals(KeyCode.ENTER)) commit(); });
        
        //When focused changes to false
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> { if (!newValue) commit(); });
    }

    private void commit() {
    	commitEdit(converter.fromString(textField.getText()));
    }
    
    private String getString() {
        return getItem() == null ? "" : converter.toString(getItem());
    }
}