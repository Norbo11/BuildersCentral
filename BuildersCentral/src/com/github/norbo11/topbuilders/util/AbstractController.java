package com.github.norbo11.topbuilders.util;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

public class AbstractController implements Initializable {
	private ResourceBundle bundle;

	public ResourceBundle getBundle() {
		return bundle;
	}

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		bundle = rb;
	}
	
}
