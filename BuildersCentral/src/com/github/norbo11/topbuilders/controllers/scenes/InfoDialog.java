package com.github.norbo11.topbuilders.controllers.scenes;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.util.SceneHelper;

public class InfoDialog extends AbstractController {
    public static final String FXML_FILENAME = "scenes/InfoDialog.fxml";
    
    @FXML private ResourceBundle resources;
    @FXML private Label infoText;
    private Runnable runnable;
    
    
    public void setText(String text) {
        infoText.setText(text);
    }
    
    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }
    
    @FXML
    public void ok(ActionEvent event) {
        runnable.run();
        SceneHelper.closeScene((Node) event.getSource());
    }


}
