package com.github.norbo11.topbuilders.controllers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import com.github.norbo11.topbuilders.util.FXMLHelper;
import com.github.norbo11.topbuilders.util.LoadedFXML;

public class AbstractTab extends Tab {

	public AbstractTab(TabPane tabPane, String filename) {
		fxmlFilename = filename;
		parentTabPane = tabPane;
		loadFromFxml();
	}
	
	private String fxmlFilename;
	private TabPane parentTabPane = null;
	private AbstractController controller = null;
	
	public AbstractController getController() {
        return controller;
    }

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
	    LoadedFXML fxml = FXMLHelper.loadFxml("/com/github/norbo11/topbuilders/fxml/" + fxmlFilename);
        controller = (AbstractController) fxml.getController();
        setContent(fxml.getRoot());
	}
}
