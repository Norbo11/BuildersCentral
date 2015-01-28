package com.github.norbo11.topbuilders.controllers.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.models.StockedMaterial;

public class MaterialsTab extends AbstractController {
    public final static String FXML_FILENAME = "tabs/MaterialsTab.fxml";
    
    
    @FXML public TableView<StockedMaterial> table;
    @FXML public TableColumn<StockedMaterial, StockedMaterial> xCol;
    
    private class ButtonCell extends TableCell<StockedMaterial, StockedMaterial> {
        @Override
        protected void updateItem(StockedMaterial item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) setText("");
            else {
                Button button = new Button("X");
                button.setOnAction(e -> item.delete());
                setGraphic(button); //setGraphic allows me to set an actual node instead of text for these cell contents
            }
        }
    }
    
    /* FXML methods */

    @FXML
	public void initialize() {				
		xCol.setCellFactory(column -> new ButtonCell());
	    
	    update();
	}

    
    /* Instance methods */
    
    @Override
    public void update() {
        table.getItems().clear();
        table.getItems().addAll(StockedMaterial.getAllStockedMaterials());
    }
}
