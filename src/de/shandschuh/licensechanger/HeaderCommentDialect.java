package de.shandschuh.licensechanger;

public interface HeaderCommentDialect {
	public String getCommentInitiator();
	
	public String getCommentMarker();
	
	public String getCommentClosing();
	
	public String getIgnoreCommentInitiator();
}
