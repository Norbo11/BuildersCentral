package com.github.norbo11.topbuilders.util;

import javafx.scene.control.TreeTableRow;

import com.github.norbo11.topbuilders.models.AbstractModel;

public class HeadingTreeTableRow<T extends AbstractModel> extends TreeTableRow<T> {
	//Add a "heading" CSS class to all dummy (title) rows
	@Override
    protected void updateItem(T job, boolean empty) {
        super.updateItem(job, empty);
        
        if (job != null && job.isDummy()) {
            if (!getStyleClass().contains("heading")) getStyleClass().add("heading");
        } else getStyleClass().remove("heading");
    }
}
