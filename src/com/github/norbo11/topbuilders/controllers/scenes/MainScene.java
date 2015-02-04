package com.github.norbo11.topbuilders.controllers.scenes;

import java.time.LocalDateTime;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.controllers.tabs.EmployeeHomeTab;
import com.github.norbo11.topbuilders.controllers.tabs.ManagerHomeTab;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.models.enums.UserType;
import com.github.norbo11.topbuilders.util.DateTimeUtil;
import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.TabHelper;

public class MainScene extends AbstractController {
    public static final String FXML_FILENAME = "scenes/MainScene.fxml";
    
    @FXML private Text welcomeText;
    @FXML private VBox dateBox;
    @FXML private TabPane tabPane;

    @FXML
    public void initialize() {
        welcomeText.setText(Resources.getResource("home.welcome") + ", " + Employee.getCurrentEmployee().getFullName());
        
        Label dateLabel = new Label();
        Label timeLabel = new Label();
        dateBox.getChildren().addAll(dateLabel, timeLabel);
        
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            LocalDateTime currentDate = LocalDateTime.now();
            timeLabel.setText(DateTimeUtil.formatTime(currentDate));
            dateLabel.setText(DateTimeUtil.formatDate(currentDate));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();
        
        TabHelper.setTabPane(tabPane);
        
        String scene = Employee.getCurrentEmployee().getUserType().isAtLeast(UserType.MANAGER) ? ManagerHomeTab.FXML_FILENAME : EmployeeHomeTab.FXML_FILENAME;
        TabHelper.createAndSwitchTab(Resources.getResource("home"), scene).setClosable(false);
    }
}