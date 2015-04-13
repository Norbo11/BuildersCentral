package com.github.norbo11.topbuilders.controllers.custom;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LimitedTextField extends TextField {
    private int limit = 0;

	public LimitedTextField() {
        addEventFilter(KeyEvent.KEY_TYPED, e -> {                  
            //If the user isn't trying to delete characters and he has reached the limit, cancel the event
            if (!e.getCharacter().equals(KeyCode.BACK_SPACE) && getText().length() == limit) {
                e.consume();
            }
        });    
    }

	public int getLimit() {
	    return limit;
	}
	
	public void setLimit(int limit) {
	    this.limit = limit;
	}
}
