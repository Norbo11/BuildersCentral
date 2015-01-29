package com.github.norbo11.topbuilders.controllers.scenes;

import java.util.Vector;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.util.Resources;

public abstract class AbstractValidationScene extends AbstractController {
    private Vector<Label> errors = new Vector<Label>();
    
    public void addError(String key) {
        errors.add(new Label(Resources.getResource(key)));
    }


    public void clearErrors() {
        errors.clear();
    }
    
    public boolean displayErrors() {
        if (errors.size() > 0) {
            ObservableList<Node> errorList = getErrorsList().getChildren();
            errorList.clear();
            errorList.addAll(errors);
            
            getErrorsLabel().getScene().getWindow().sizeToScene();
            getErrorsLabel().setVisible(true);
            return false;
        } 
        return true;
    }
    
    public abstract VBox getErrorsList();
    public abstract Label getErrorsLabel();
    
}