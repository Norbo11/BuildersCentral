package com.github.norbo11.topbuilders.util;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import com.github.norbo11.topbuilders.util.helpers.GuiUtil;

public class ModelFinder<T> {
    public static final int LIST_CELL_HEIGHT = 24; //This must match in the css file
    private ListView<T> searchList;
    private ObservableList<T> models;
    private ModelFinderComparator<T> comparator;
    
    public ModelFinder(ListView<T> searchList, TextField textField, ObservableList<T> models, ModelFinderComparator<T> comparator) {
        this.searchList = searchList;
        this.models = models;
        this.comparator = comparator;
                
        //searchList.setManaged(false);
        //searchList.getItems().addAll(models);
        
        //Ensure height of search list is proportional to number of items in list
        //searchList.prefHeightProperty().bind(Bindings.size(searchList.getItems()).multiply(LIST_CELL_HEIGHT));
        
        //Search upon typing in the text field
        textField.textProperty().addListener((obs, oldVal, newVal) -> search(oldVal, newVal));
        
        hideSearchList();
    }

    public void search(String oldVal, String newVal) {          
        ArrayList<T> entires = new ArrayList<T>();
                
        //Filter out the entries that don't contain the entered text
        for (T entry : models) {            
            //If the entry starts with the entered text then keep it in the search list
            if (!newVal.equals("") && comparator.compare(entry, newVal)) {
                entires.add(entry);
            }
        }

        searchList.getItems().setAll(entires);
        
        //Hide if the user has deleted the contents of the text field, otherwise show.
        if (newVal.equals("")) {
            hideSearchList();
        } else showSearchList();
    }

    public void hideSearchList() {
    	GuiUtil.hideNodeManaged(searchList);
    }
    
    public void showSearchList() {
        GuiUtil.showNodeManaged(searchList);
    }
}
