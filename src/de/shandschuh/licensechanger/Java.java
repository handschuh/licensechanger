package de.shandschuh.licensechanger;

import java.io.File;

public class Java implements Language {

	public String getCommentClosing() {
		return " **/";
	}

	public String getCommentInitiator() {
		return "/**";
	}

	public String getCommentMarker() {
		return " * ";
	}

	public boolean accept(File pathname) {
		return pathname.toString().endsWith(".java");
	}

	public String identifier() {
		return "java";
	}
	
}
