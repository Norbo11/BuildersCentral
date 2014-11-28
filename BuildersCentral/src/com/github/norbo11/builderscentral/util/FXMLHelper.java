package com.github.norbo11.builderscentral.util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import com.github.norbo11.builderscentral.Main;

public class FXMLHelper {
    public static Parent loadFxml(String filename) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.app.getClass().getResource("/com/github/norbo11/builderscentral/fxml/" + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }
}
