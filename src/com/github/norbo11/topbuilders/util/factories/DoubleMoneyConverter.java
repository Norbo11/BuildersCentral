package com.github.norbo11.topbuilders.util.factories;

import java.text.DecimalFormat;

import javafx.util.StringConverter;

public class DoubleMoneyConverter extends StringConverter<Number> {

	private static DecimalFormat formatter = new DecimalFormat("£###,###,##0.00");
	
	@Override
	public Double fromString(String string) {
		return Double.valueOf(string);
	}

	@Override
	public String toString(Number object) {
		return formatter.format(object);
	}

}
