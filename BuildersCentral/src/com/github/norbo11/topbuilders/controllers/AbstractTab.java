package com.github.norbo11.topbuilders.controllers;

import com.github.norbo11.topbuilders.util.FXMLHelper;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class AbstractTab extends Tab {

	public AbstractTab(TabPane tabPane, String filename) {
		fxmlFilename = filename;
		parentTabPane = tabPane;
		loadFromFxml();
	}
	
	private String fxmlFilename;
	private TabPane parentTabPane = null;
	
	public void close() {
    	EventHandler<Event> handler = getOnClosed();
    	if (null != handler) {
            handler.handle(null);
        } else {
        	parentTabPane.getTabs().remove(this);
        }
	}
	
	public TabPane getParentTabPane() {
		return parentTabPane;
	}

	public String getFxmlFilename() {
		return fxmlFilename;
	}
	
    public void loadFromFxml() {
        //Create the tab object, fill it with the loaded FXML
        Parent root = FXMLHelper.loadFxml("/com/github/norbo11/topbuilders/fxml/" + fxmlFilename);
	    setContent(root);
	}
}
