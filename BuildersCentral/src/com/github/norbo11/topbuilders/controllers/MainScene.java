package com.github.norbo11.topbuilders.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.enums.UserType;
import com.github.norbo11.topbuilders.util.TabHelper;

public class MainScene extends AbstractController {
    public static final String FXML_FILENAME = "MainScene.fxml";
    
    @FXML private ResourceBundle resources;
    @FXML private Text welcomeText;
    @FXML private VBox dateBox;
    @FXML private TabPane tabPane;

    @FXML
    public void initialize() {
        welcomeText.setText(resources.getString("home.welcome") + ", " + Employee.getCurrentEmployee());
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMMM YYYY");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("kk:mm:ss");
        Label dateLabel = new Label();
        Label timeLabel = new Label();
        dateBox.getChildren().addAll(dateLabel, timeLabel);
        
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            LocalDateTime currentDate = LocalDateTime.now();
            timeLabel.setText(currentDate.format(timeFormatter));
            dateLabel.setText(currentDate.format(dateFormatter));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();
        
        TabHelper.setTabPane(tabPane);
        
        String scene = Employee.getCurrentEmployee().getUserType().isAtLeast(UserType.MANAGER) ? ManagerHomeTab.FXML_FILENAME : EmployeeHomeTab.FXML_FILENAME;
        TabHelper.createAndSwitchTab(resources.getString("home"), scene).setClosable(false);
    }
}