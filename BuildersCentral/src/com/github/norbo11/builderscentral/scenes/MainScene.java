package com.github.norbo11.builderscentral.scenes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;

import com.github.norbo11.builderscentral.Constants;
import com.github.norbo11.builderscentral.models.User;
import com.github.norbo11.builderscentral.tabs.HomeTab;
import com.github.norbo11.builderscentral.util.TabHelper;

public class MainScene extends StyledScene {
    
    private TabPane tabPane;

    public TabPane getTabPane() {
        return tabPane;
    }
    
    
    public MainScene()
    {
    	super(new BorderPane(), Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, "main.css");
        
    	BorderPane borderPane = (BorderPane) getRoot();
        borderPane.setPrefSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        
        //TOP
        Text welcomeText = new Text("Welcome, " + User.getCurrentUser().getUsername());
        welcomeText.setId("welcomeText");
        
        Line topLine = new Line();
        topLine.setEndX(Constants.WINDOW_WIDTH);
        
        VBox topVBox = new VBox();
        VBox.setMargin(welcomeText, new Insets(50, 0, 0, 0));
        topVBox.getChildren().addAll(welcomeText, topLine);
        borderPane.setTop(topVBox);
        
        //CENTER
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        TabHelper.newTab(tabPane, new HomeTab());
        borderPane.setCenter(tabPane);
        
        //BOTTOM
        Line botLine = new Line();
        botLine.setEndX(Constants.WINDOW_WIDTH);
        
        VBox dateBox = new VBox(2);
        dateBox.setId("dateBox");
        
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
        
        AnchorPane botAnchor = new AnchorPane();
        AnchorPane.setRightAnchor(dateBox, 10.0);
        AnchorPane.setBottomAnchor(dateBox, 0.0);
        botAnchor.getChildren().addAll(dateBox);
        
        VBox botBox = new VBox();
        botBox.getChildren().addAll(botLine, botAnchor);
        borderPane.setBottom(botBox);
    }
}
