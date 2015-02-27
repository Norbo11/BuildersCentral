package com.github.norbo11.topbuilders.util;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import com.github.norbo11.topbuilders.util.helpers.GuiUtil;

public class ModelFinder<T> {
    private ListView<T> searchList;
    private ArrayList<T> models;
    private ModelFinderComparator<T> comparator;
    
    public ModelFinder(ListView<T> searchList, ArrayList<T> models, ModelFinderComparator<T> comparator) {
        this.searchList = searchList;
        this.models = models;
        this.comparator = comparator;
        
        searchList.getItems().addAll(models);
        GuiUtil.hideNodeManaged(searchList);
    }

    public void search(String oldVal, String newVal) {          
        //If the number of characters in the text box is less than last time it must be because the user pressed delete
        if (newVal.length() < oldVal.length()) {
            //Restore the lists original set of entries and start from the beginning
            searchList.getItems().clear();
            searchList.getItems().addAll(models);
        }
         
        //Filter out the entries that don't contain the entered text
        ObservableList<T> newEntries = FXCollections.observableArrayList();
        
        for (T entry : searchList.getItems()) {            
            //If the entry starts with the entered text then keep it in the search list
            if (comparator.compare(entry, newVal)) {
                newEntries.add(entry);
            }
        }
        
        searchList.setItems(newEntries);
        
        //Hide if the user has deleted the contents of the text field, otherwise show.
        if (newVal.equals("")) {
            GuiUtil.hideNodeManaged(searchList);
        } else GuiUtil.showNodeManaged(searchList);
    }
}
