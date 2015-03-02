package com.github.norbo11.topbuilders.controllers.custom;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.github.norbo11.topbuilders.controllers.tabs.QuotesTab;
import com.github.norbo11.topbuilders.models.RequiredMaterial;
import com.github.norbo11.topbuilders.models.StockedMaterial;
import com.github.norbo11.topbuilders.models.enums.QuantityType;
import com.github.norbo11.topbuilders.util.ModelFinder;
import com.github.norbo11.topbuilders.util.Resources;
import com.github.norbo11.topbuilders.util.helpers.FXMLUtil;
import com.github.norbo11.topbuilders.util.helpers.SceneUtil;
import com.github.norbo11.topbuilders.util.helpers.StageUtil;

public class RequiredMaterialItem extends HBox {
    public static final String FXML_FILENAME = "RequiredMaterialItem.fxml";
    
    @FXML private Button deleteButton;
    @FXML private Label typeLabel;
    @FXML private TextField nameField;
    @FXML private DoubleTextField quantityField;
    @FXML private AnchorPane nameAnchorPane;
    @FXML private ListView<StockedMaterial> searchList;
    
    private Stage stage;
    private RequiredMaterial requiredMaterial;
    private QuotesTab quotesTab;
    
    public RequiredMaterialItem(RequiredMaterial requiredMaterial, QuotesTab quotesTab) {
        this.requiredMaterial = requiredMaterial;
        this.quotesTab = quotesTab;
        
        FXMLUtil.loadFxml(FXML_FILENAME, this, this);
    }
    
    @FXML
    public void delete() {
        requiredMaterial.delete();
        quotesTab.updateJobGroups();
    }
    
    @FXML
    public void commit() {
        requiredMaterial.setQuantityRequired(quantityField.getDouble());
       
        StockedMaterial stockedMaterial = StockedMaterial.getStockedMaterialByName(nameField.getText());
        
        if (stockedMaterial == null && (stage == null || !stage.isShowing())) {
            if (!nameField.getText().equals("")) {
                stage = StageUtil.createDialogStage(Resources.getResource("materials.add"));
                stage.setOnCloseRequest(e -> {
                    delete();
                });
                
                VBox vbox = new VBox(10);
                vbox.setPadding(new Insets(10));
                vbox.setAlignment(Pos.CENTER);
                SceneUtil.changeScene(stage, vbox);
                
                Label infoLabel = new Label(Resources.getResource("quotes.materialCreated", nameField.getText()));
                vbox.getChildren().add(infoLabel);
                
                GridPane grid = new GridPane();
                grid.setHgap(5);
                grid.setVgap(5);
                grid.setAlignment(Pos.CENTER);
                vbox.getChildren().add(grid);
                
                Label quantityLabel = new Label(Resources.getResource("materials.quantityInStock"));
                DoubleTextField quantity = new DoubleTextField();
                grid.addRow(0, quantityLabel, quantity);
                
                Label quantityTypeLabel = new Label(Resources.getResource("materials.quantityType"));
                ComboBox<QuantityType> combo = new ComboBox<QuantityType>(FXCollections.observableArrayList(QuantityType.values()));
                grid.addRow(1, quantityTypeLabel, combo);
                
                combo.getSelectionModel().selectFirst();
                
                Button add = new Button(Resources.getResource("materials.add"));
                add.setOnAction(e -> {
                    StockedMaterial newMaterial = new StockedMaterial();
                    newMaterial.setNewModel(true);
                    newMaterial.setName(nameField.getText());
                    newMaterial.setQuantityInStock(quantity.getDouble());
                    newMaterial.setQuantityTypeId(combo.getSelectionModel().getSelectedItem().getId());
                    newMaterial.save();
    
                    requiredMaterial.setStockedMaterial(newMaterial);
                    updateAll();
                    SceneUtil.closeScene(add);
                });
    
                vbox.getChildren().add(add);
                stage.sizeToScene();
            } else {
                delete();
            }
        } else {
            setStockedMaterial(stockedMaterial);
        }
    }
    
    public void setStockedMaterial(StockedMaterial stockedMaterial) {
        requiredMaterial.setStockedMaterial(stockedMaterial);
        updateAll();
    }
    
    @FXML
    public void initialize() { 
        new ModelFinder<StockedMaterial>(searchList, nameField, StockedMaterial.getModels(), (entry, input) -> entry.getName().toUpperCase().startsWith(input.toUpperCase()));
        
        // Search list
        searchList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                setStockedMaterial(newValue);
            }
        });
        searchList.setCellFactory(param -> {
            return new ListCell<StockedMaterial>() {
                @Override
                protected void updateItem(StockedMaterial material, boolean empty) {
                    if (material != null) {
                        setText(material + " - " + material.getQuantityString() + " " + Resources.getResource("materials.inStock").toLowerCase());
                    }
                }
            };
        });
               
        nameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) commit();
        });
                
        updateAll();
    }

    public void updateAll() {        
        quantityField.setText(requiredMaterial.getQuantityRequired() + "");
        
        StockedMaterial stockedMaterial = requiredMaterial.getStockedMaterial();
        
        if (stockedMaterial != null) {
            nameField.setText(stockedMaterial.getName());
            typeLabel.setText(stockedMaterial.getQuantityType().toString());
        } else {
            nameField.setText("");
            typeLabel.setText("");
        }
        
        searchList.setVisible(false);
    }
    
    public TextField getNameField() {
        return nameField;
    }
}
