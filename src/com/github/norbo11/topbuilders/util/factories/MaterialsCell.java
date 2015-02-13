package com.github.norbo11.topbuilders.util.factories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import com.github.norbo11.topbuilders.controllers.custom.DoubleTextField;
import com.github.norbo11.topbuilders.controllers.tabs.QuotesTab;
import com.github.norbo11.topbuilders.models.Job;
import com.github.norbo11.topbuilders.models.RequiredMaterial;
import com.github.norbo11.topbuilders.models.StockedMaterial;
import com.github.norbo11.topbuilders.util.Resources;

public class MaterialsCell extends TreeTableCell<Job, Job> {
	private GridPane grid;
	private int lastRow = 0; //Need to keep track of the ID of the last material row so that the Add Material button is added in the correct place
	private Button newMaterialButton;
	private Job job;
	private QuotesTab controller;
		    	
	public MaterialsCell(QuotesTab controller) {
	    super();
	    
	    this.controller = controller;
	    
	    /* Initialize components which never change */
	    newMaterialButton = new Button(Resources.getResource("quotes.newMaterial"));
        newMaterialButton.setMaxWidth(Double.MAX_VALUE);
        newMaterialButton.setOnAction(e -> {
            removeButton(); //Remove the New Material button temporarily, so that a new material row can be added in its place
            
            /* Make a new required material, add it to the job */
            RequiredMaterial newMaterial = new RequiredMaterial();
            newMaterial.setNewModel(true);
            newMaterial.jobIdProperty().bind(job.idProperty());
            newMaterial.setParent(job);
            job.getChildren().add(newMaterial);
            
            addMaterialRow(newMaterial); 
            addButton(); //Re-add the New Material button
         });
        
        /* Grid pane */
        grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setMaxWidth(Double.MAX_VALUE);
        
        /* Below code ensures that the New Material button is always as wide as the whole cell */
        ColumnConstraints constraint = new ColumnConstraints();
        constraint.setFillWidth(true); 
        constraint.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(new ColumnConstraints(), constraint);
	    
        GridPane.setColumnSpan(newMaterialButton, 4); //The button spans 4 rows
        GridPane.setHgrow(newMaterialButton, Priority.ALWAYS);
	}
	
	private void addButton() {
	    grid.addRow(lastRow, newMaterialButton);
        lastRow++;
    }

    private void removeButton() {
	    grid.getChildren().remove(newMaterialButton);
        lastRow--;
    }
    
    private void searchMaterials(ListView<StockedMaterial> searchList, String oldVal, String newVal) {        	
    	//If the number of characters in the text box is less than last time it must be because the user pressed delete
        if (newVal.length() < oldVal.length()) {
            //Restore the lists original set of entries and start from the beginning
        	searchList.getItems().clear();
            searchList.getItems().addAll(StockedMaterial.getStockedMaterials());
        }
         
        //Break out all of the parts of the search text by splitting on white space
        String[] parts = newVal.toUpperCase().split(" ");
     
        //Filter out the entries that don't contain the entered text
        ObservableList<StockedMaterial> subentries = FXCollections.observableArrayList();
        for (StockedMaterial entry : searchList.getItems()) {
            boolean match = true;
            for (String part : parts) {
                //The entry needs to contain all portions of the search string *but* in any order
                if (!entry.getName().toUpperCase().startsWith(part)) {
                    match = false;
                    break;
                }
            }
     
            if (match) {
                subentries.add(entry);
            }
        }
        searchList.setItems(subentries);
        
        //Hide if the user has deleted the contents of the text field, otherwise show.
    	if (newVal.equals("")) {
    		hideNode(searchList);
    	} else showNode(searchList);
    }
    
    private void showNode(Node node) {
    	node.setManaged(true);
    	node.setVisible(true);
    }
    
    private void hideNode(Node node) {
    	node.setManaged(false); //This ensures that the button stops taking any space, instead of simply being invisible
    	node.setVisible(false);
    }

    //Called whenever a new material has to be made for a particular row
    private void addMaterialRow(RequiredMaterial requiredMaterial) {
    	/* Delete button */
        Button deleteButton = new Button("X");
        deleteButton.setOnAction(e -> {
            requiredMaterial.delete();
            requiredMaterial.deleteFromParent();
            controller.updateJobGroups();
        });
        
        /* Quantity field */
        DoubleTextField quantityField = new DoubleTextField(requiredMaterial.getQuantityRequired());
        quantityField.setMinWidth(50);
        quantityField.setPrefWidth(50);

        /* Material name area */
	    VBox nameVBox = new VBox(); //Container of name field and search field
	    
	    
	    // Name field
	    TextField nameField = new TextField();
		nameField.setMaxWidth(Double.MAX_VALUE);
	    	    
		// Search list
	    ListView<StockedMaterial> searchList = new ListView<StockedMaterial>();
	    searchList.getItems().addAll(StockedMaterial.getStockedMaterials());
	    searchList.setPrefHeight(70);
	    searchList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
	    	if (newValue != null) {
		        nameField.setText(newValue.getName());
		        quantityField.setText(newValue.getQuantityInStock() + "");
		        hideNode(searchList);
	    	}
        });
		
	    
		hideNode(searchList);
		
		nameField.textProperty().addListener((obs, oldVal, newVal) -> searchMaterials(searchList, oldVal, newVal));    	    
        nameVBox.getChildren().addAll(nameField, searchList);
        
        /* Type label */
        
        Label typeLabel = new Label();
        typeLabel.setMinWidth(30);
        typeLabel.setPrefWidth(30);
        
        /* Set properties */
        
        StockedMaterial stockedMaterial = requiredMaterial.getStockedMaterial();
        
        if (stockedMaterial != null) {
            nameField.setText(stockedMaterial.getName());
            typeLabel.setText(stockedMaterial.getQuantityType().toString());
            quantityField.setText(stockedMaterial.getQuantityRequired().toString());
        }
        
        /* Add to grid and return the name field for focus purposes */
        grid.addRow(lastRow, deleteButton, nameVBox, quantityField, typeLabel);
        lastRow++;
        nameField.requestFocus();
	}
	
    @Override
    protected void updateItem(Job job, boolean empty) {
        super.updateItem(job, empty);
        if (empty) {
            setText("");
            setGraphic(null);
        }
        else {            
            if (!job.isDummy()) {
                /*setOnMouseExited(e -> toggleButton());
                setOnMouseEntered(e -> toggleButton());*/
                
                /* Initialize */
                lastRow = 0;
                grid.getChildren().clear();
                this.job = job;
                
                /* Add material row entries for all required materials for this job */
                for (RequiredMaterial requiredMaterial : job.getChildren()) {
                	addMaterialRow(requiredMaterial);
                }
                
                addButton(); //Comment this, and uncomment all other lines, to add show-on-hover functionality to the New Material button
                setGraphic(grid);
            }
        }
    }
}