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

public class QuotesTab extends Tab {

    private GridPane jobsGrid;
    private int lastRow;
    
    public void addNewJobRow() {        
        TextField[] fields = new TextField[3];
        
        for (int i = 0; i < fields.length; i++)
        {
            fields[i] = new TextField();
            fields[i].setPrefWidth(200);
        }
     
        jobsGrid.addRow(lastRow++, fields);
    }
    
    public QuotesTab() {        
        setText("Quotes and invoices");
        
        Label[] labels = new Label[6];

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
        
        labels[0] = new Label("Job Description");
        labels[1] = new Label("Materials Required");
        labels[2] = new Label("Cost");
        
        for (int i = 0; i <= 2; i++) {
        	labels[i].setPrefWidth(200);
        	labels[i].setPadding(new Insets(0, 0, 0, 3));
        }
        
        GridPane titleGrid = new GridPane();
        titleGrid.setHgap(10);
        titleGrid.setVgap(10);
        titleGrid.add(labels[0], 0, 0);
        titleGrid.add(labels[1], 1, 0);
        titleGrid.add(labels[2], 2, 0);
        
        jobsGrid = new GridPane();
        jobsGrid.setHgap(10);
        jobsGrid.setVgap(10);
        addNewJobRow();
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(Constants.WINDOW_HEIGHT);
        scrollPane.setPrefWidth(700);
        scrollPane.setStyle("-fx-background-color:transparent;"); //Get rid of gray borders
        scrollPane.setContent(jobsGrid);
        
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(titleGrid, scrollPane);
        
        HBox centerBox = new HBox(30);
        centerBox.setPadding(new Insets(20));
        centerBox.getChildren().addAll(projectDetailsGrid, vbox);        
        contents.getChildren().addAll(centerBox);
        
        //BOTTOM
        Button exportButton = new Button("Export to Excel");
        
        Button newJobButton = new Button("Add new job");
        newJobButton.setOnAction(e -> addNewJobRow());
        
        Button saveButton = new Button("Save quote");

        HBox botBox = new HBox(20);
        botBox.setAlignment(Pos.BOTTOM_CENTER);
        botBox.getChildren().addAll(exportButton, newJobButton, saveButton);
        
        contents.getChildren().add(botBox);
        
        setContent(contents);
    }

}
