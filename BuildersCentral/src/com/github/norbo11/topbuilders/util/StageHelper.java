package com.github.norbo11.topbuilders.util;

import javafx.stage.Modality;
import javafx.stage.Stage;

public class StageHelper {

	public static Stage createDialogStage(String title) {
		Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        return stage;
	}

}
