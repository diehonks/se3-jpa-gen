package testsuite.util;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.Comment;
import japa.parser.ast.CompilationUnit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileAccess {
	
	private File refFilesPath;
	private File genFilesPath;
	private BufferedReader br;

	public FileAccess(String referenceFilePath){
		File referenceFile = new File(referenceFilePath);
		if(!referenceFile.exists()){
			System.out.println("Path to soruceReferences File does not exists: " + referenceFile.getAbsoluteFile());
			return;
		}
		 try {
			br = new BufferedReader(new FileReader(referenceFile));
			refFilesPath = new File(br.readLine());
			if (!refFilesPath.exists()){
				System.out.println("Reference Source folder does not exists: " + refFilesPath.getAbsoluteFile());
				return;
			}
			genFilesPath = new File(br.readLine());
			if (!genFilesPath.exists()){
				System.out.println("Generated Source folder does not exists: " + genFilesPath.getAbsoluteFile());
				return;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<File> getFiles(File dir) {
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
	
	public ArrayList<File> getReferenceFiles(){
		return getFiles(refFilesPath); 
	}
	
	public ArrayList<File> getGeneratetFiles() {
		return getFiles(genFilesPath); 
	}
	
	private boolean filesAreEqual(ArrayList<File> genFiles, ArrayList<File> refFiles, boolean checkContent){		
		//dont check Comments inside of javafiles:
		List<Comment> emptyComments = new ArrayList<Comment>();		
		
		for (File refFile : refFiles){
			boolean fileEqual = false;
			String refFileName = refFile.getName();
			for(File genFile : genFiles){
				if (refFileName.equals(genFile.getName())){
					fileEqual = true;
				}
				if (fileEqual&&checkContent&&refFileName.toLowerCase().endsWith("java")){
					try {
						CompilationUnit cuRefFile = JavaParser.parse(refFile);
						CompilationUnit cuGenFile = JavaParser.parse(genFile);
						cuRefFile.setComments(emptyComments);
						cuGenFile.setComments(emptyComments);
						fileEqual = cuRefFile.equals(cuGenFile);
						break;
						
					} catch (ParseException | IOException e) {
						System.out.println("Exception");
						fileEqual = false;
						break;
					}
				}
			}
			if(!fileEqual){
				System.out.println((checkContent)?"DIFFERENT"+ " ... "+ refFileName :"MISSING" + " ... "+ refFileName);
				return false;
			}else{
				System.out.println((checkContent)?"IDENTICAL" + " ... " +refFileName:"OK" + " ... " +refFileName);
			}
		}
		return true;
	}
	
	public boolean fileNamesAreEqual(ArrayList<File> genFiles, ArrayList<File> refFiles){		
		return filesAreEqual(genFiles,refFiles,false);
	}
	
	public boolean fileContentAreEqual(ArrayList<File> genFiles, ArrayList<File> refFiles){		
		return filesAreEqual(genFiles,refFiles,true);
	}
}
