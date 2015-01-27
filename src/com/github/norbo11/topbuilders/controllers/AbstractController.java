package com.github.norbo11.topbuilders.controllers;

import javafx.scene.Scene;


public abstract class AbstractController {
    private Scene scene = null;
    
    public void update() {
        
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
