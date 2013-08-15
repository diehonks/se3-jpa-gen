package testsuite.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;

import bsh.util.Util;
import testsuite.util.*;
import java.util.ArrayList;
import java.io.File;

import javax.swing.text.StyledEditorKit.BoldAction;

public class CheckFiles {
	
	ArrayList<File> genFiles;
	ArrayList<File> refFiles;

	@BeforeClass
	public void init() {
		genFiles = FileAccess.getGeneratetFiles();
		refFiles = FileAccess.getReferenceFiles();
	}

	@Test
	public void checkFileCount() {
		System.out.println("\n@Test file count");
		try {
			System.out.println("  Expected files: " + refFiles.size()
					+ "\n  Found files: " + genFiles.size());
			Assert.assertEquals(genFiles.size() == refFiles.size(), true);
			System.out.println("PASSED");
		} catch (AssertionError err) {
			System.out.println("FAILURE: Generated file count not congenial to reference files.");
			throw err;
		}
	}

	@Test
	public void checkFileNames() {
		System.out.println("\n@Test file names");
		File currFile;
		AssertionError fileNotFound = null;
		for (int i = 0; i < refFiles.size(); i++) {
			currFile = refFiles.get(i);
			try {
				Assert.assertEquals(FileAccess.isFileInArrayEqualsByPath(
						currFile, genFiles), true);
				System.out.println("  " + currFile.getPath() + " ... OK");
			} catch (AssertionError err) {
				System.out.println("  " + currFile.getPath() + " ... MISSING");
				fileNotFound = err;
			}
		}
		if (fileNotFound != null) {
			System.out.println("FAILURE: Not all reference files are present in generated files.");
			throw fileNotFound;
		} else {
			System.out.println("  All expected files present.\nPASSED");
		}
	}

	@Test
	public void checkFileContent() {
		System.out.println("\n@Test file content");
		File refFile = null;
		File genFile = null;
		AssertionError fileNotFound = null;

		for (int i = 0; i < refFiles.size(); i++) {
			refFile = refFiles.get(i);
			genFile = FileAccess.getPathToGenEquivalent(refFile, genFiles);
			try{
				Assert.assertEquals(FileAccess.getFileContent(refFile).equals(FileAccess.getFileContent(genFile)), true);
				System.out.println("  " + genFile.getPath() + " ... IDENTICAL");
			}catch(AssertionError err) {
				System.out.println("  " + genFile.getPath() + " ... DIFFERENT");
				fileNotFound = err;
			}
		}
		if (fileNotFound != null) {
			System.out.println("FAILURE: Not all generated files are identical to the reference files.");
			throw fileNotFound;
		} else {
			System.out.println("  All genereated files are identical with the reference files.\nPASSED");
		}

	}
}
