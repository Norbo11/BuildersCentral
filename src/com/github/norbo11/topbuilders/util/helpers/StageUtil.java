package com.github.norbo11.topbuilders.util.helpers;

import javafx.stage.Modality;
import javafx.stage.Stage;

public class StageUtil {

	public static Stage createDialogStage(String title) {
		Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        return stage;
	}

}
