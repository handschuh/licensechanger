package de.shandschuh.licensechanger;

public class JavaHeaderCommentDialect implements HeaderCommentDialect {

	public String getCommentClosing() {
		return " **/";
	}

	public String getCommentInitiator() {
		return "/**";
	}

	public String getCommentMarker() {
		return " * ";
	}

	public String getIgnoreCommentInitiator() {
		return "/*";
	}

}
