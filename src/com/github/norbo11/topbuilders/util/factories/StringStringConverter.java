package com.github.norbo11.topbuilders.util.factories;

import javafx.util.StringConverter;

public class StringStringConverter extends StringConverter<String> {

	@Override
	public String toString(String string) {
		return string;
	}

	@Override
	public String fromString(String string) {
		return string;
	}

}
