package com.github.norbo11.topbuilders.util;

import java.io.File;

public class Util {

	public static String removeFileExtension(File file) {
		return file.getName().substring(0, file.getName().length() - 4);
	}

}
