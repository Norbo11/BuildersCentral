package com.github.norbo11.topbuilders.controllers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import com.github.norbo11.topbuilders.util.LoadedFXML;
import com.github.norbo11.topbuilders.util.TabHelper;

public class AbstractTab extends Tab {

	public AbstractTab(TabPane parentTabPane, LoadedFXML fxml) {
		setContent(fxml.getRoot());
		this.controller = (AbstractController) fxml.getController();
		this.parentTabPane = parentTabPane;
		this.fxmlFilename = fxml.getFxmlFilename();
		setOnClosed(e -> TabHelper.removeTab(controller));
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
	
    public void close() {
    	EventHandler<Event> handler = getOnClosed();
    	if (null != handler) {
            handler.handle(null);
        } else {
        	parentTabPane.getTabs().remove(this);
        }
	}
}
