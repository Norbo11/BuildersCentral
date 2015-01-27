package com.github.norbo11.topbuilders.controllers.custom;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class DoubleTextField extends TextField {
	public DoubleTextField() {
		super();
	
		addEventFilter(KeyEvent.KEY_TYPED, e -> {
			if (!e.getCharacter().matches("\\d*")) {
				e.consume();
	        }                
	    });
	}
	
	public double getDouble() {
		return Double.valueOf(getText());
	}
}
