package com.github.norbo11.topbuilders.controllers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import com.github.norbo11.topbuilders.util.LoadedFXML;

/* This class adds extra functionality to the Tab class by keeping track of the tab's controller, parent pane, and FXML filename */
public class AbstractTab extends Tab {

	public AbstractTab(TabPane parentTabPane, LoadedFXML fxml) {
		//Set this tab to display the root of the loaded FXML
		setContent(fxml.getRoot());
		
		this.controller = (AbstractController) fxml.getController();
		this.parentTabPane = parentTabPane;
		this.fxmlFilename = fxml.getFxmlFilename();
	}
	
	private String fxmlFilename;
	private TabPane parentTabPane = null;
	private AbstractController controller = null;
	
	/* Getters and setters */
	
	public AbstractController getController() {
        return controller;
    }
	
	public TabPane getParentTabPane() {
        return parentTabPane;
    }
    
    public String getFxmlFilename() {
        return fxmlFilename;
    }
    
    /* Instance methods */
	
    /* Closes this tab programmatically */
    public void close() {
    	//Get the event handler responsible for closing the tab
    	EventHandler<Event> handler = getOnClosed();
  
    	//If it exists, call it; otherwise, simply remove the tab from the list
    	if (handler != null) {
            handler.handle(null);
        } else {
        	parentTabPane.getTabs().remove(this);
        }
	}
}
