package com.github.norbo11.topbuilders.controllers.scenes;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.util.Resources;

public abstract class AbstractValidationScene extends AbstractController {
    private ArrayList<Label> errors = new ArrayList<Label>();
    
    public void addErrorFromResource(String key) {
        addError(Resources.getResource(key));
    }
    
    public void addError(String string) {
        errors.add(new Label(string));
    }
    
    public boolean displayErrors() {
        return displayErrors(false);
    }
    
    public boolean displayErrors(boolean resize) {
        ObservableList<Node> errorList = getErrorsList().getChildren();
        errorList.clear();
        getErrorsLabel().setVisible(false);
        
        if (errors.size() > 0) {
            errorList.addAll(errors);
            errors.clear();
            
            if (resize) getErrorsLabel().getScene().getWindow().sizeToScene();
            getErrorsLabel().setVisible(true);
            return false;
        } 
        return true;
    }
    
    public abstract VBox getErrorsList();
    public abstract Label getErrorsLabel();
}