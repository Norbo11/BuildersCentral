package com.github.norbo11.topbuilders.controllers.custom;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class DoubleTextField extends TextField {
	public DoubleTextField() {
		this(0);
	}
	
	public DoubleTextField(double initialValue) {
	    super(initialValue + "");
	    
        addEventFilter(KeyEvent.KEY_TYPED, e -> {
            String character = e.getCharacter();
            
            //If the character is a dot, and one already exists, cancel the event
            //Also cancel the event if there are no characters currently entered (the input cannot begin with a dot)
            if (character.equals(".")) {
                if (getText().contains(".") || getText().length() == 0) e.consume();
            }
            
            //Also, if the character isn't a dot but is anything but a number, cancel the event
            else if (!character.matches("\\d*")) {
                e.consume();
            }    
        });    
    }

    public double getDouble() {
		return Double.valueOf(getText());
	}

	public void setDouble(double value) {
		setText(value + "");
	}
}
