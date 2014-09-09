package com.github.norbo11.builderscentral.tabs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;

import com.github.norbo11.builderscentral.Main;
import com.github.norbo11.builderscentral.scenes.LoginScene;
import com.github.norbo11.builderscentral.util.TabManager;

public class HomeTab {
    public static Tab getTab() {
        Tab tab = new Tab();
        tab.setText("Home");
        tab.setClosable(false);
        
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setId("menuGrid");
        gridPane.setPadding(new Insets(25));
        
        Button[] button = new Button[8];
        for (int i = 0; i < 8; i++)
        {
            button[i] = new Button();
            button[i].setPrefSize(200, 110);
            gridPane.add(button[i], i % 4, i <= 3 ? 0 : 1); //Buttons 0-3 have columnIndex of 0, while 4-7 have columnIndex of 1
        }
        button[0].setText("Quotes and invoices");
        button[0].setOnAction(e -> TabManager.newTab(QuotesTab.getTab()));
        button[1].setText("Quote requests");
        button[2].setText("Manage employees");
        button[3].setText("Settings");
        button[4].setText("Current projects");
        button[5].setText("Material stock");
        button[6].setText("Manage users");
        button[7].setText("Log out");
        button[7].setOnAction(e -> Main.changeMainScene(LoginScene.getScene()));
        
        tab.setContent(gridPane);
        return tab;
    }
}
