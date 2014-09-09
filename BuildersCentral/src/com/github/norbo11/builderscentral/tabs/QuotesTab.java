package com.github.norbo11.builderscentral.tabs;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.github.norbo11.builderscentral.Constants;

public class QuotesTab {

    private static GridPane jobsGrid;
    private static int lastRow;
    
    public static void addNewJobRow() {        
        TextField[] fields = new TextField[3];
        
        for (int i = 0; i < fields.length; i++)
        {
            fields[i] = new TextField();
            fields[i].setPrefWidth(200);
        }
     
        jobsGrid.addRow(lastRow++, fields);
    }
    
    public static Tab getTab() {        
        Tab tab = new Tab();
        tab.setText("Quotes and invoices");
        
        Label[] labels = new Label[6];
        labels[0] = new Label("Job Description");
        labels[1] = new Label("Materials Required");
        labels[2] = new Label("Cost");
        labels[3] = new Label("Select quote to edit");
        labels[4] = new Label("Client name");
        labels[5] = new Label("Project address");

        VBox contents = new VBox(20);
        contents.setPadding(new Insets(20, 0, 20, 0));
        
        //TOP
        HBox topBox = new HBox(20);
        topBox.setAlignment(Pos.TOP_CENTER);
        topBox.getChildren().addAll(labels[3], new ComboBox<String>());
        contents.getChildren().add(topBox);
        
        //CENTER     
        TextField clientNameField = new TextField();
        TextArea projectAddressArea = new TextArea();
        projectAddressArea.setPrefHeight(100);

        GridPane projectDetailsGrid = new GridPane();
        projectDetailsGrid.setPadding(new Insets(27, 0, 0, 0));
        projectDetailsGrid.setHgap(20);
        projectDetailsGrid.setVgap(20);
        projectDetailsGrid.addRow(0, labels[4], clientNameField);
        projectDetailsGrid.addRow(1, labels[5], projectAddressArea);
        
        jobsGrid = new GridPane();
        jobsGrid.setHgap(10);
        jobsGrid.setVgap(10);
        jobsGrid.add(labels[0], 0, 0);
        jobsGrid.add(labels[1], 1, 0);
        jobsGrid.add(labels[2], 2, 0);
        lastRow = 1;
        addNewJobRow();
        
        HBox centerBox = new HBox(30);
        centerBox.setPadding(new Insets(20));
        centerBox.getChildren().addAll(projectDetailsGrid, jobsGrid);        
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        scrollPane.setContent(centerBox);
        contents.getChildren().add(scrollPane);
        
        //BOTTOM
        Button exportButton = new Button("Export to Excel");
        
        Button newJobButton = new Button("Add new job");
        newJobButton.setOnAction(e -> addNewJobRow());
        
        Button saveButton = new Button("Save quote");

        HBox botBox = new HBox(20);
        botBox.setAlignment(Pos.BOTTOM_CENTER);
        botBox.getChildren().addAll(exportButton, newJobButton, saveButton);
        
        contents.getChildren().add(botBox);
        
        tab.setContent(contents);
        return tab;
    }

}
