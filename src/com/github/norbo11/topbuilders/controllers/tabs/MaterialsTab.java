package com.github.norbo11.topbuilders.controllers.tabs;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.models.StockedMaterial;

public class MaterialsTab extends AbstractController {
    public final static String FXML_FILENAME = "tabs/MaterialsTab.fxml";
    
    private boolean isNew = false;
    @FXML private Button addMaterial;
    @FXML private TableView<StockedMaterial> table;
    @FXML private TableColumn<StockedMaterial, StockedMaterial> xColumn;
    @FXML private TableColumn<StockedMaterial, String> nameColumn;
    @FXML private TableColumn<StockedMaterial, Double> quantityInStockColumn, requiredColumn, balanceColumn;
    
    private class RequiredCellValueFactory implements Callback<CellDataFeatures<StockedMaterial, Double>, ObservableValue<Double>> {

		@Override
		public ObservableValue<Double> call(CellDataFeatures<StockedMaterial, Double> param) {
			// TODO Query DB to count the amount required for this material
			return null;
		}

    }
    
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
    
    void updateMaterial(StockedMaterial material) {
    	if (isNew) {
			material.add();
		} else {
			material.save();
		}
    	isNew = false;
    }

    @FXML
	public void initialize() {				
		xColumn.setCellFactory(column -> new ButtonCell());
	    
		nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nameColumn.setOnEditCommit(editEvent -> {
			StockedMaterial material = (StockedMaterial) editEvent.getTableView().getItems().get(editEvent.getTablePosition().getRow());
			material.setName(editEvent.getNewValue());
			material.save();
        });

		quantityInStockColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		quantityInStockColumn.setOnEditCommit(editEvent -> {
			StockedMaterial material = (StockedMaterial) editEvent.getTableView().getItems().get(editEvent.getTablePosition().getRow());
			material.setQuantityInStock(editEvent.getNewValue());			
			material.save();
        });
		
		requiredColumn.setCellValueFactory(new RequiredCellValueFactory());
			
	    update();
	}
    
    @FXML
    public void save() {
    	
    }
    
    @FXML
    public void generateReport() {
    	
    }

    @FXML
    public void addMaterial() {
    	StockedMaterial material = new StockedMaterial();
    	material.add();
    	table.getItems().add(material);
    	isNew = true;
    	
    }
    
    /* Instance methods */
    
    @Override
    public void update() {
        table.getItems().clear();
        table.getItems().addAll(StockedMaterial.getAllStockedMaterials());
    }
}
