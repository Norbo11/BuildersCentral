package com.github.norbo11.topbuilders.util.factories;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class TextFieldCell<T, S> extends TableCell<T, S> {
    private TextField textField;
	private StringConverter<S> converter;

	public TextFieldCell(StringConverter<S> converter) {
		this.converter = converter;
	}
	
	public TextFieldCell() {
		converter = new StringConverter<S>() {
			@Override
			public String toString(S object) {
				return object.toString();
			}

			@Override
			public S fromString(String string) {
				return null;
			}
		};
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
        textField.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //If it lost focus then commit
                if (!newValue) {
                    commitEdit(converter.fromString(textField.getText()));
                }
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : converter.toString(getItem());
    }
}