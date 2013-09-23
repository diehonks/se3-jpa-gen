package testsuite.tests;

import java.io.File;
import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import testsuite.util.FileAccess;

public class CheckFilesSemantic {
	
	FileAccess fileAccess;
	ArrayList<File> refFiles;
	ArrayList<File> genFiles;
	
	
	@BeforeClass
	public void init() {
		fileAccess = new FileAccess(getClass().getClassLoader().getResource("").getPath()+"/testsuite/ressource/sourceReferences");
		refFiles = fileAccess.getReferenceFiles();
		genFiles = fileAccess.getGeneratetFiles();
	}
	
	@Test
	public void checkFileCount() {
		System.out.println("\n@Test file count");
		try {
			System.out.println("  Expected files: " + refFiles.size() + "\n  Found files: " + genFiles.size());
			Assert.assertEquals(genFiles.size(),refFiles.size());
			System.out.println("PASSED");
		} catch (AssertionError err) {
			System.out.println("FAILURE: Generated file count not congenial to reference files.");
			throw err;
		}
	}
	
	@Test
	public void checkFileNames() {
		System.out.println("\n@Test file names");
		
		try {
			Assert.assertTrue(fileAccess.fileNamesAreEqual(refFiles, genFiles));
			System.out.println("  All expected files present.\nPASSED");
		} catch (AssertionError err) {
			System.out.println("FAILURE: Not all reference files are present in generated files.");
			throw err;
		}	
	}
	
	@Test
	public void checkFileContent() {
		System.out.println("\n@Test file content");
		
		try {
			Assert.assertTrue(fileAccess.fileContentAreEqual(refFiles, genFiles));
			System.out.println("  All genereated files are identical with the reference files.\nPASSED");
		} catch (AssertionError err) {
			System.out.println("FAILURE: Not all generated files are identical to the reference files.");
			throw err;
		}	
	}
}
