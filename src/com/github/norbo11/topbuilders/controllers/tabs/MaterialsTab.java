package com.github.norbo11.topbuilders.controllers.tabs;

import java.text.NumberFormat;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.controllers.custom.DoubleTextField;
import com.github.norbo11.topbuilders.models.StockedMaterial;
import com.github.norbo11.topbuilders.models.enums.QuantityType;
import com.github.norbo11.topbuilders.util.DeleteModelButtonCellFactory;
public class MaterialsTab extends AbstractController {
    public final static String FXML_FILENAME = "tabs/MaterialsTab.fxml";
    
    @FXML private Button addMaterial;
    @FXML private TableView<StockedMaterial> table;
    @FXML private TableColumn<StockedMaterial, StockedMaterial> xColumn, quantityInStockColumn;
    @FXML private TableColumn<StockedMaterial, String> nameColumn;
    @FXML private TableColumn<StockedMaterial, Number> requiredColumn, balanceColumn;
    
    
    private class RequiredCellValueFactory implements Callback<CellDataFeatures<StockedMaterial, Number>, ObservableValue<Number>> {
		@Override
		public ObservableValue<Number> call(CellDataFeatures<StockedMaterial, Number> param) {
			// TODO Query DB to count the amount required for this material
			return new ReadOnlyDoubleWrapper(5);
		}
    }
    
    private class BalanceCellValueFactory implements Callback<CellDataFeatures<StockedMaterial, Number>, ObservableValue<Number>> {
        @Override
        public ObservableValue<Number> call(CellDataFeatures<StockedMaterial, Number> param) {
            StockedMaterial material = param.getValue();
            DoubleBinding property = material.quantityInStockProperty().subtract(material.getQuantityRequired());
            return property;
        }
    }
    
    private class QuantityInStockCell extends TableCell<StockedMaterial, StockedMaterial> {
        
        private DoubleTextField textField;
        private ComboBox<QuantityType> comboBox;
        private HBox hbox;
        
        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                
                createTextField();
                createComboBox();
                createHbox();
                
                setText(null);
                setGraphic(hbox);
                
                textField.selectAll();
            }
        }
 
        @Override
        public void cancelEdit() {
            super.cancelEdit();
 
            updateItem(getItem(), false);
        }
 
        @Override
        public void updateItem(StockedMaterial item, boolean empty) {
            super.updateItem(item, empty);
 
            if (empty) {
                setText("");
                setGraphic(null);
            } else {
                if (isEditing()) {
                    textField.setText(getDisplayString());
                    comboBox.getSelectionModel().select(getQuantityType());
                        
                    setText(null);
                    setGraphic(hbox);
                } else {
                    setText(getDisplayString());
                    setGraphic(null);
                }
            }
        }
 
        private void createHbox() {
            hbox = new HBox(5);
            hbox.getChildren().addAll(textField, comboBox);
            HBox.setHgrow(textField, Priority.SOMETIMES);
        }
        
        private void createComboBox() {
            comboBox = new ComboBox<QuantityType>();
            comboBox.setPrefWidth(80);
            comboBox.getItems().addAll(QuantityType.values());      
            comboBox.getSelectionModel().select(getQuantityType());
            comboBox.setOnAction((e) -> commit());
        }
        
        private void createTextField() {
            textField = new DoubleTextField(getItem().getQuantityInStock() + "");
            textField.setPrefWidth(60);
            textField.setOnAction((e) -> commit());
        }
        
        private void commit() {
            StockedMaterial material = getItem();            
            material.setQuantityInStock(Double.valueOf(textField.getText()));
            material.setQuantityTypeId(comboBox.getSelectionModel().getSelectedItem().getId());
            commitEdit(getItem());
        }
 
        private String getDisplayString() {
            NumberStringConverter converter = new NumberStringConverter(NumberFormat.getNumberInstance());
            return converter.toString(getItem().getQuantityInStock()) + " " + getItem().getQuantityType();
        }
        
        private QuantityType getQuantityType() {
            return getItem().getQuantityType();
        }
    }
    
    /* FXML methods */
    
    @FXML
	public void initialize() {			
        DeleteModelButtonCellFactory.assignCellFactory(xColumn);
		
		nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nameColumn.setOnEditCommit(editEvent -> {
			StockedMaterial material = (StockedMaterial) editEvent.getTableView().getItems().get(editEvent.getTablePosition().getRow());
			material.setName(editEvent.getNewValue());
			material.save();
        });
		
		quantityInStockColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<StockedMaterial>(param.getValue()));
		quantityInStockColumn.setCellFactory(param -> new QuantityInStockCell());
		quantityInStockColumn.setOnEditCommit(editEvent -> {
			StockedMaterial material = (StockedMaterial) editEvent.getTableView().getItems().get(editEvent.getTablePosition().getRow());
			material.save();
        });

		requiredColumn.setCellValueFactory(new RequiredCellValueFactory());
		balanceColumn.setCellValueFactory(new BalanceCellValueFactory());
	    update();
	}
    
    @FXML
    public void generateReport() {
    	
    }

    @FXML
    public void addMaterial() {
    	StockedMaterial material = new StockedMaterial("New Material", 0, QuantityType.METERS);
    	table.getItems().add(material);
    	//TODO this doesnt work
    	table.edit(9, nameColumn);
    }
    
    /* Instance methods */

    @Override
    public void update() {
        table.getItems().clear();
        table.getItems().addAll(StockedMaterial.getAllStockedMaterials());
    }
}
