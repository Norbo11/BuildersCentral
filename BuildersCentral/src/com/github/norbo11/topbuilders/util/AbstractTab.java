package com.github.norbo11.topbuilders.util;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class AbstractTab extends Tab {

	
	public AbstractTab(TabPane tabPane, String filename) {
		fxmlFilename = filename;
		parentTabPane = tabPane;
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

	private String fxmlFilename;
	private TabPane parentTabPane = null;

	public String getFxmlFilename() {
		return fxmlFilename;
	}
}
