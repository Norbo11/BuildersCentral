package com.github.norbo11.topbuilders.util.helpers;

import javafx.scene.Node;

public class GuiUtil {
    public static void showNodeManaged(Node node) {
        node.setManaged(true);
        node.setVisible(true);
    }
    
    public static void hideNodeManaged(Node node) {
        node.setManaged(false); //This ensures that the button stops taking any space, instead of simply being invisible
        node.setVisible(false);
    }
}
