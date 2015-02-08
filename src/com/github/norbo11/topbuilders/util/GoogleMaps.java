package com.github.norbo11.topbuilders.util;

import javafx.stage.Stage;

import com.github.norbo11.topbuilders.controllers.scenes.AbstractScene;
import com.github.norbo11.topbuilders.controllers.scenes.MapScene;
import com.github.norbo11.topbuilders.util.helpers.SceneUtil;
import com.github.norbo11.topbuilders.util.helpers.StageUtil;

public class GoogleMaps {

    public static final String API_KEY = "AIzaSyBYMuklisoW3mN2ID5Y5Pi8sK7y8MTyxpY";

    public static void openMap(String query) {
        Stage stage = StageUtil.createDialogStage(Resources.getResource("map.title"));
        AbstractScene scene = SceneUtil.changeScene(stage, MapScene.FXML_FILENAME);
        MapScene controller = (MapScene) scene.getController();
        controller.setQuery(query);
        controller.updateAll();
    }
    
}
