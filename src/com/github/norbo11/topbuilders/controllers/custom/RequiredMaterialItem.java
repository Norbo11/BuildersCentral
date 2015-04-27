package com.github.norbo11.topbuilders.controllers.custom;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import com.github.norbo11.topbuilders.controllers.tabs.QuotesTab;
import com.github.norbo11.topbuilders.models.RequiredMaterial;
import com.github.norbo11.topbuilders.models.StockedMaterial;
import com.github.norbo11.topbuilders.util.ModelFinder;
import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.helpers.FXMLUtil;

public class RequiredMaterialItem extends HBox {
    public static final String FXML_FILENAME = "RequiredMaterialItem.fxml";
    
    @FXML private Button deleteButton;
    @FXML private Label typeLabel;
    @FXML private TextField nameField;
    @FXML private DoubleTextField quantityField;
    @FXML private ListView<StockedMaterial> searchList;
    
    private ModelFinder<StockedMaterial> finder;
    private RequiredMaterial requiredMaterial;
    private QuotesTab quotesTab;
    private boolean mouseOverSearchList;
    
    public RequiredMaterialItem(RequiredMaterial requiredMaterial, QuotesTab quotesTab) {
        this.requiredMaterial = requiredMaterial;
        this.quotesTab = quotesTab;
        
        //Load this custom component by supplying this class as the root and as the controller
        FXMLUtil.loadFxml(FXML_FILENAME, this, this);
    }
    
	/* FXML Methods */
	
    @FXML
    public void initialize() { 
    	//Create a new model finder, supplying the search list and search field, the models, and the comparator
        this.finder = new ModelFinder<StockedMaterial>(searchList, nameField, StockedMaterial.getModels(), 
        		(entry, input) -> entry.getName().toUpperCase().startsWith(input.toUpperCase())
        );
        
        //On-click behaviour for search-list (set the correct stocked material)
        searchList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                updateStockedMaterial(newValue);
            }
        });
        
        //Modify the search cell appearance by displaying the quantity in stock as well as the material name
        searchList.setCellFactory(param -> {
            return new ListCell<StockedMaterial>() {
                @Override
                protected void updateItem(StockedMaterial material, boolean empty) {
                	super.updateItem(material, empty);
                    if (material != null) {
                        setText(material + " - " + material.getQuantityString() + " " + Resources.getResource("materials.inStock").toLowerCase());
                    } else {
                        setText("");
                    }
                }
            };
        });
        
        searchList.setOnMouseEntered(e -> { mouseOverSearchList = true; });
        searchList.setOnMouseExited(e -> { mouseOverSearchList  = false; });
             
        //If the name field loses focus, but the search list wasn't clicked (as the mouse wasn't over it)
        nameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
    		if (!newValue && !mouseOverSearchList)
    		{
    			//Attempt to set the new material based on what was typed
    			updateStockedMaterialBasedOnField();
    		}
        });
                
        //Update fields and labels
        updateAll();
    }

    @FXML
    public void delete() {
        requiredMaterial.delete();
        quotesTab.updateJobGroups();
    }
    
    @FXML
    public void updateStockedMaterialBasedOnField() {
        requiredMaterial.setQuantityRequired(quantityField.getDouble());
       
        //Get the material object by name
        StockedMaterial stockedMaterial = StockedMaterial.getStockedMaterialByName(nameField.getText());
        
        //If it exists, update it - otherwise, delete the entire required material entry
        if (stockedMaterial != null) {
        	updateStockedMaterial(stockedMaterial);
        } else {
        	delete();
        }
    }
    
    /* Instance methods */
    
	public void updateStockedMaterial(StockedMaterial stockedMaterial) {
		//Set the stocked material for the required material object and update fields/labels
        requiredMaterial.setStockedMaterial(stockedMaterial);
        updateAll();
    }
    
    public void updateAll() {     
        quantityField.setText(requiredMaterial.getQuantityRequired() + "");
        
        StockedMaterial stockedMaterial = requiredMaterial.getStockedMaterial();
        
        //If a stocked material is set, update the required labels and fields - otherwise, set them to blank
        if (stockedMaterial != null) {
            nameField.setText(stockedMaterial.getName());
            typeLabel.setText(stockedMaterial.getQuantityType().toString());
        } else {
            nameField.setText("");
            typeLabel.setText("");
        }

        //Hide the search results
        finder.hideSearchList();
    }
    
    public TextField getNameField() {
        return nameField;
    }
}
