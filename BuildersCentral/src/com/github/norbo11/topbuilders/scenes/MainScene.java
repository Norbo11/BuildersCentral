package com.github.norbo11.topbuilders.scenes;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import com.github.norbo11.topbuilders.models.User;
import com.github.norbo11.topbuilders.tabs.EmployeeHomeTab;
import com.github.norbo11.topbuilders.util.TabHelper;

public class MainScene implements Initializable {
    public static final String FXML_FILENAME = "MainScene.fxml";
    
    @FXML private Text welcomeText;
    @FXML private VBox dateBox;
    @FXML private TabPane tabPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        welcomeText.setText("Welcome, " + User.getCurrentUser().getUsername());
        
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
        TabHelper.createAndSwitchTab("Home", EmployeeHomeTab.FXML_FILENAME).setClosable(false);
    }
}