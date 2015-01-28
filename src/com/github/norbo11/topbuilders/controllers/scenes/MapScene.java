package com.github.norbo11.topbuilders.controllers.scenes;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import com.github.norbo11.topbuilders.Constants;
import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.util.GoogleMaps;

public class MapScene extends AbstractController {
    public static final String FXML_FILENAME = "scenes/MapScene.fxml";
    private static final int MAP_WIDTH = Constants.WINDOW_WIDTH - 200;
    private static final int MAP_HEIGHT = Constants.WINDOW_HEIGHT - 100;

    private String query;
    @FXML private WebView webView;
    @FXML private WebEngine engine;

    @FXML
    public void initialize() {
        engine = webView.getEngine();
    }

    public void update() {
        String content = 
        "<html dir=\"ltr\">" +
            "<head></head>" +
            "<body>" +
                "<iframe id=\"maps\"" +
                " width=\"" + MAP_WIDTH + "\"" +
                " height=\"" + MAP_HEIGHT + "\"" +
                " frameborder=\"0\" style=\"border:0\"" +
                " src=\"https://www.google.com/maps/embed/v1/place?key=" + GoogleMaps.API_KEY + "&q=" + query + "\">" +
                "</iframe>" +
            "</body>" +
        "</html>";
        
        engine.loadContent(content);
        webView.setPrefWidth(MAP_WIDTH + 19);
        webView.setPrefHeight(MAP_HEIGHT + 19);
        webView.getScene().getWindow().sizeToScene();
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
