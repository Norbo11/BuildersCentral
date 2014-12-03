package com.github.norbo11.topbuilders.util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import com.github.norbo11.topbuilders.Main;

public class FXMLHelper {
    public static Parent loadFxml(String filename) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.getApp().getClass().getResource(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }
}
