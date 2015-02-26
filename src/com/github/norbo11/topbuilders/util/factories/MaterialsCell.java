package com.github.norbo11.topbuilders.util.factories;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableCell;
import javafx.scene.layout.VBox;

import com.github.norbo11.topbuilders.controllers.custom.RequiredMaterialItem;
import com.github.norbo11.topbuilders.controllers.tabs.QuotesTab;
import com.github.norbo11.topbuilders.models.Job;
import com.github.norbo11.topbuilders.models.RequiredMaterial;
import com.github.norbo11.topbuilders.util.Resources;

public class MaterialsCell extends TreeTableCell<Job, Job> {
	private VBox vbox;
	private Button newMaterialButton;
	private Job job;
	private QuotesTab controller;
	private RequiredMaterialItem lastItem;
		    	
	public MaterialsCell(QuotesTab controller) {
	    super();
	    
	    this.controller = controller;
	    
	    /* Initialize components which never change */
	    newMaterialButton = new Button(Resources.getResource("quotes.newMaterial"));
        newMaterialButton.setMaxWidth(Double.MAX_VALUE);
        newMaterialButton.setOnAction(e -> {
            if (lastItem == null || !lastItem.getNameField().getText().equals("")) {
                removeButton(); //Remove the New Material button temporarily, so that a new material row can be added in its place
                
                /* Make a new required material, add it to the job */
                RequiredMaterial newMaterial = new RequiredMaterial();
                newMaterial.setNewModel(true);
                newMaterial.jobIdProperty().bind(job.idProperty());
                newMaterial.setParent(job);
                job.getChildren().add(newMaterial);
                
                addMaterialRow(newMaterial).requestFocus(); 
                
                addButton(); //Re-add the New Material button
            }
         });
        
        /* VBox */
        vbox = new VBox(5);
        vbox.setMaxWidth(Double.MAX_VALUE);
	}
	
    private TextField addMaterialRow(RequiredMaterial requiredMaterial) {
        lastItem = new RequiredMaterialItem(requiredMaterial, controller);
        vbox.getChildren().add(lastItem);
        return lastItem.getNameField(); //Return this for focus purposes
    }
	
	private void addButton() {
	    vbox.getChildren().add(newMaterialButton);
    }

    private void removeButton() {
        vbox.getChildren().remove(newMaterialButton);
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
                /* Initialize */
                vbox.getChildren().clear();
                this.job = job;
                this.lastItem = null;
                
                /* Add material row entries for all required materials for this job */
                for (RequiredMaterial requiredMaterial : job.getChildren()) {
                    addMaterialRow(requiredMaterial);
                }
                
                addButton();
                setGraphic(vbox);
            }
        }
    }
}