package com.github.norbo11.topbuilders.controllers.custom;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.helpers.FXMLUtil;

public class ValidationInfo extends VBox {
    public static final String FXML_FILENAME = "ValidationInfo.fxml";

    @FXML private VBox errorsList;
    @FXML private Label errorsLabel;
    
    private ArrayList<Label> errors = new ArrayList<Label>();

    public ValidationInfo() {
        FXMLUtil.loadFxml(FXML_FILENAME, this, this);
    }
    
    public void addErrorFromResource(String key) {
        addError(Resources.getResource(key));
    }
    
    public void addError(String string) {
        Label error = new Label(string);
        error.setWrapText(true);
        errors.add(error);
    }
    
    public boolean validate() {
        return displayErrors(false);
    }
    
    public boolean displayErrors(boolean resize) {
        ObservableList<Node> errorList = errorsList.getChildren();
        errorList.clear();
        errorsLabel.setVisible(false);
        
        if (errors.size() > 0) {
            errorList.addAll(errors);
            errors.clear();
            
            if (resize) errorsLabel.getScene().getWindow().sizeToScene();
            errorsLabel.setVisible(true);
            return false;
        } 
        return true;
    }
}