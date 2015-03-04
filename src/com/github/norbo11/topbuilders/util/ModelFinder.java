package com.github.norbo11.topbuilders.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ModelFinder<T> {
    public static final int LIST_CELL_HEIGHT = 24; //This must match in the css file
    private ListView<T> searchList;
    private ObservableList<T> models;
    private ModelFinderComparator<T> comparator;
    private TextField textField;
    
    public ModelFinder(ListView<T> searchList, TextField textField, ObservableList<T> models, ModelFinderComparator<T> comparator) {
        this.searchList = searchList;
        this.models = models;
        this.comparator = comparator;
        this.textField = textField;
                
        //searchList.setManaged(false);
        searchList.getItems().addAll(models);
        
        //Ensure height of search list is proportional to number of items in list
        //searchList.prefHeightProperty().bind(Bindings.size(searchList.getItems()).multiply(LIST_CELL_HEIGHT));
        
        //Search upon typing in the text field
        textField.textProperty().addListener((obs, oldVal, newVal) -> search(oldVal, newVal));
        
        hideSearchList();
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
            hideSearchList();
        } else showSearchList();
    }

    public void hideSearchList() {
        searchList.setVisible(false);
        textField.setText("");
    }
    
    public void showSearchList() {
       searchList.setManaged(false);
       // compute bounds of text field relative to suggestion box's parent:
       Parent parent = searchList.getParent(); // AnchorPane
       Bounds tfBounds = textField.getBoundsInLocal();
       Bounds tfBoundsInScene = textField.localToScene(tfBounds);
       Bounds tfBoundsInParent = parent.sceneToLocal(tfBoundsInScene);
       
       // position suggestion box:
       searchList.setLayoutX(tfBoundsInParent.getMinX());
       searchList.setLayoutY(tfBoundsInParent.getMaxY());
       //searchList.setPrefWidth(tfBoundsInParent.getWidth()); 
       searchList.setVisible(true);
    }
}
