package de.shandschuh.licensechanger;

import java.io.File;
import java.io.FileFilter;

public class Cpp implements Language {

	public String getCommentClosing() {
		return " */";
	}

	public String getCommentInitiator() {
		return "/*";
	}

	public String getCommentMarker() {
		return " * ";
	}

	public boolean accept(File pathname) {
		return pathname.toString().endsWith(".cpp") || pathname.toString().endsWith(".hpp");
	}
	
	public String identifier() {
		return "cpp";
	}

}

