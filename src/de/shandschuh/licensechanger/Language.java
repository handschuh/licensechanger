package de.shandschuh.licensechanger;

import java.io.FileFilter;

public interface Language extends LanguageComment, FileFilter {

	// Returns a short name of the language
	public String identifier();

}
