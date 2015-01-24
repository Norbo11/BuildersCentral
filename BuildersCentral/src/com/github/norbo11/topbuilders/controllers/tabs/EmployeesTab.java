package com.github.norbo11.topbuilders.controllers.tabs;

import java.util.ResourceBundle;
import java.util.Vector;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import javafx.util.Callback;

import com.github.norbo11.topbuilders.controllers.AbstractController;
import com.github.norbo11.topbuilders.controllers.scenes.AbstractScene;
import com.github.norbo11.topbuilders.controllers.scenes.ModifyEmployeeScene;
import com.github.norbo11.topbuilders.models.Employee;
import com.github.norbo11.topbuilders.util.SceneHelper;
import com.github.norbo11.topbuilders.util.StageHelper;

public class EmployeesTab extends AbstractController {
    public final static String FXML_FILENAME = "tabs/EmployeesTab.fxml";
    public final static Vector<EmployeesTab> tabs = new Vector<EmployeesTab>();
    
    public class EmployeeDefaultWageFactory implements Callback<CellDataFeatures<Employee, String>, ObservableValue<String>> {
        @Override
        public ObservableValue<String> call(CellDataFeatures<Employee, String> param) {
            Employee employee = param.getValue().getValue();
            
            if (employee.isDummy()) return new ReadOnlyStringWrapper("");
            else return employee.defaultWageProperty().asString();
        }
    }
    
    @FXML private ResourceBundle resources;
    @FXML private TreeTableView<Employee> table;
    @FXML private TreeItem<Employee> superusers, managers, employees;
    @FXML private TreeTableColumn<Employee, String> deleteCol;
    @FXML private TreeTableColumn<Employee, String> defaultWage;
    
    @FXML
	public void initialize() {	    
        defaultWage.setCellValueFactory(new EmployeeDefaultWageFactory());
        
        //Allow double clicking to modify employee
        table.setRowFactory(value -> {
            TreeTableRow<Employee> row = new TreeTableRow<Employee>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {
                    modifyEmployee(null);
                }
            });
            return row;
        });
        
    	update(); 
	}

    @FXML
    public void deleteEmployee(ActionEvent event) {
        table.getSelectionModel().getSelectedItem().getValue().delete();
    }
    
    @FXML
    public void addEmployee(ActionEvent event) {
        //Create new window
        Stage stage = StageHelper.createDialogStage(resources.getString("employees.add"));
        AbstractScene scene = SceneHelper.changeScene(stage, ModifyEmployeeScene.FXML_FILENAME);
        
        //Display details
        ModifyEmployeeScene controller = (ModifyEmployeeScene) scene.getController();
        controller.setEmployee(new Employee(), true);
        controller.updateFields();
    }
    
    @FXML
    public void modifyEmployee(ActionEvent event) {
        Employee employee = table.getSelectionModel().getSelectedItem().getValue();
        
        if (!employee.isDummy()) {
            //Create new window
            Stage stage = StageHelper.createDialogStage(employee.getFullName());
            AbstractScene scene = SceneHelper.changeScene(stage, ModifyEmployeeScene.FXML_FILENAME);
            
            //Display details
            ModifyEmployeeScene controller = (ModifyEmployeeScene) scene.getController();
            controller.setEmployee(employee, false);
            controller.updateFields();
        }
    }
    
    private void update() {
        superusers.getChildren().clear();
        managers.getChildren().clear();
        employees.getChildren().clear();
        
        for (Employee employee : Employee.getAllEmployees()) {
            TreeItem<Employee> item = new TreeItem<Employee>(employee);
                        
            switch (employee.getUserType()) {
            case SUPERUSER: superusers.getChildren().add(item); break;
            case MANAGER: managers.getChildren().add(item); break;
            case EMPLOYEE: employees.getChildren().add(item); break;
            }
        }
    }
    
    /* Static methods */
    
    public static void updateAllTabs() {
        for (EmployeesTab tab : tabs) {
            tab.update();
        }
    }

    public static Vector<EmployeesTab> getTabs() {
        return tabs;
    }
}