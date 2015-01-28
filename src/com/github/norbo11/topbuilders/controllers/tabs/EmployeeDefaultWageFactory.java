package com.github.norbo11.topbuilders.controllers.tabs;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.util.Callback;

import com.github.norbo11.topbuilders.models.Employee;

public class EmployeeDefaultWageFactory implements Callback<CellDataFeatures<Employee, String>, ObservableValue<String>> {

    @Override
    public ObservableValue<String> call(CellDataFeatures<Employee, String> param) {
        Employee employee = param.getValue().getValue();
        
        if (employee.isDummy()) return new ReadOnlyStringWrapper("");
        else return employee.defaultWageProperty().asString();
    }
    
}