package testsuite.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileAccess {
	
	final static String GENFILESPATH = "./src/";
	final static String REFFILESPATH = "./referenceCode/";
	
	private static ArrayList<File> getFiles(File dir) {
		ArrayList<File> currendList = new ArrayList<File>();
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				currendList.addAll(getFiles(files[i]));
			} else {
				currendList.add(files[i]);
			}
		}
		return currendList;
	}

	public static ArrayList<File> getGeneratetFiles() {
		File dir = new File(GENFILESPATH);
		return getFiles(dir);
	}

	public static ArrayList<File> getReferenceFiles() {
		File dir = new File(REFFILESPATH);
		return getFiles(dir);
	}

	public static boolean isFileInArrayEqualsByPath(File testFile,
			ArrayList<File> refList) {
		if(getPathToGenEquivalent(testFile, refList) != null){
			return true;
		}
		return false;
	}
	
	public static File getPathToGenEquivalent(File testFile,
			ArrayList<File> refList) {
		if (testFile == null) {
			throw new IllegalArgumentException("TestFile is null");
		}
		for (int i = 0; i < refList.size(); i++) {
			if (clipFolderName(refList.get(i).getPath()).equalsIgnoreCase(
					clipFolderName(testFile.getPath()))) {
				return refList.get(i);
			}
		}
		return null;
	}

	public static String clipFolderName(String path) {
		if (path.startsWith(REFFILESPATH)) {
			return path.substring(REFFILESPATH.length());
		}
		return path.substring(GENFILESPATH.length());
	}
	
	public static String getFileContent(File file){
		StringBuffer result = new StringBuffer();
		try{
			FileReader fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);
			for(String line = reader.readLine(); line != null; line = reader.readLine() ){
				result.append(line);
			}
			reader.close();
		}catch(IOException err){
			err.printStackTrace();
		}
		return result.toString();
	}
}
