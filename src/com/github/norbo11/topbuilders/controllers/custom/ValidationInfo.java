package com.github.norbo11.topbuilders.controllers.custom;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.helpers.FXMLUtil;

/* Custom component representing a box containing a list of error messages */
public class ValidationInfo extends VBox {
    public static final String FXML_FILENAME = "ValidationInfo.fxml";

    @FXML private VBox errorsList;
    @FXML private Label errorsLabel;
    
    private ArrayList<Label> errorLabels = new ArrayList<Label>();

    public ValidationInfo() {
    	//Load this custom component by supplying this class as the root and as the controller
        FXMLUtil.loadFxml(FXML_FILENAME, this, this);
    }
    
    public void addErrorFromResource(String key) {
        addError(Resources.getResource(key));
    }
    
    /* Create a new error label for the given string and add it to the errors list */
    public void addError(String string) {
        Label error = new Label(string);
        error.setWrapText(true);
        errorLabels.add(error);
    }
     
    /* Argument indicates if the scene should be resized to the size acommodate for the newly displayed errors */
    /* Returns true if no errors were displayed */
    public boolean displayErrors(boolean resize) {
    	
    	/* Clear the displayed errors list and hide the label */
        ObservableList<Node> errorList = errorsList.getChildren();
        errorList.clear();
        errorsLabel.setVisible(false);
        
        //If there are any errors
        if (errorLabels.size() > 0) {
        	//Add them all to the actual displayed list
            errorList.addAll(errorLabels);
            
            //Clear the added errors list
            errorLabels.clear();
            
            //Resize if needed
            if (resize) {
            	errorsLabel.getScene().getWindow().sizeToScene();
            }
            
            //Show the label
            errorsLabel.setVisible(true);
            
            return false;
        } 
        
        return true;
    }
}