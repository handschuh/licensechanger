package de.shandschuh.licensechanger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
//import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Collection;


public class Changer {
	private File dir;
	
	private File newLicenseFile;
	
	private Collection<Language> languages;
	
	private String[] newLicenseFileContent;
	
	private String defaultProgramName;
	
	private String defaultCopyright;
	
	public Changer(File dir, File newLicenseFile, Collection<Language> languages, String defaultProgramName, String defaultCopyright) {
		this.dir = dir;
		this.newLicenseFile = newLicenseFile;
		this.languages = languages;
		this.defaultProgramName = defaultProgramName;
		this.defaultCopyright = defaultCopyright;
	}
	
	public void changeLicense() {
		newLicenseFileContent = getFileContent(newLicenseFile).split("\n");
		changeLicense(dir);
	}
	
	private void changeLicense(File dir) {
		File[] files = dir.listFiles();
		Language last_language = null;
		
		for (int n = 0, i = files != null ? files.length : 0; n < i; n++) {
			if (files[n].isDirectory()) {
				changeLicense(files[n]);
			} else {
				if (last_language != null) {
					// The last language used for a file in a directory will
					// be the most likely candidate for the next file
					if (last_language.accept(files[n])) {
						changeLicenseOfFile(files[n], last_language);
						continue;
					}
				}
				// Otherwise we have to go throught the complete list...
				for (Language language : languages) {
					if (language.accept(files[n])) {
						changeLicenseOfFile(files[n], language);
						last_language = language;
						continue;
					}
				}
			}
		}
	}
	
	private void changeLicenseOfFile(File file, Language language) {
		int position = 0;
		
		int lineCount = 0;
		
		String programName = defaultProgramName;
		
		String copyrightHolders = defaultCopyright;
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			for (String s = reader.readLine(); s != null && position < 4; s = reader.readLine(), lineCount++) {
				if (position > 0 && position < 3) {
					if (position == 2) {
						if (s.substring(language.getCommentMarker().length()).trim().length() > 0) {
							copyrightHolders = s.substring(language.getCommentMarker().length()).trim();
							// change here and modify the copyright
							position = 3;
						}
						
					} else if (position == 1 && s.substring(language.getCommentMarker().length()).trim().length() > 0) {
						programName = s.substring(language.getCommentMarker().length()).trim();
						position = 2;
					}
					
				} else if (s.equals(language.getCommentInitiator())) {
					position = 1;
				} else if (s.equals(language.getCommentClosing())){
					position = 4;
				} else if (!s.startsWith(language.getCommentMarker())) {
					position = 5; // indicates a not found license --> restart
				}
			}
			if (position == 5) {
				reader.close();
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			}
			
			String[] fileContent = getBufferedReaderContent(reader).split("\n");
			
			
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			
			writer.write(language.getCommentInitiator());
			writer.newLine();
			writer.write(language.getCommentMarker()+programName);
			writer.newLine();
			writer.write(language.getCommentMarker()+copyrightHolders);
			writer.newLine();
			writer.write(language.getCommentMarker());
			writer.newLine();
			for (int n = 0, i = newLicenseFileContent.length; n < i; n++) {
				writer.write(language.getCommentMarker()+newLicenseFileContent[n]);
				writer.newLine();
			}
			writer.write(language.getCommentClosing());
			writer.newLine();
			writer.newLine();
			for (int n = 0, i = fileContent.length; n < i; n++) {
				writer.write(fileContent[n]);
				writer.newLine();
			}
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getFileContent(File file) {
		try {
			return getBufferedReaderContent(new BufferedReader(new InputStreamReader(new FileInputStream(file))));
		} catch (Exception e) {
			return "";
		}
	}
	
	private String getBufferedReaderContent(BufferedReader reader) throws IOException {
		StringBuffer buffer = new StringBuffer();
		
		for (String s = reader.readLine(); s != null; s = reader.readLine()) {
			buffer.append(s+"\n");
		}
		reader.close();
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		if (args == null || args.length != 4) {
			System.out.println("Usage:\n java -jar licensechanger.jar /path/to/source /license/file.txt \"Program name\" \"Default copyright\"\n where DIALECT can be \"cpp\" or \"java\"");
			return;
		}
		args[0] = args[0].replace("~", System.getProperty("user.home"));
		args[2] = args[2].replace("~", System.getProperty("user.home"));
		
		ArrayList<Language> languages = new ArrayList<Language>();
		languages.add(new Java());
		languages.add(new Cpp());
		
		Changer changer = new Changer(new File(args[0]), new File(args[1]), languages, args[2], args[3]);
		changer.changeLicense();
	}
}
