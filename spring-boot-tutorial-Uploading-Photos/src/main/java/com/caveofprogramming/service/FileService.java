package com.caveofprogramming.service;

import java.io.File;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileService {
	
	@Value("${photo.file.extensions}")
	private String imageExtensions;
	
	private Random random = new Random();
	
	private String getFileExtensions(String filename) {
		
		int dotPosition = filename.lastIndexOf(".");
		
		if(dotPosition < 0) {
			return null;
		}
		
		return filename.substring(dotPosition+1).toLowerCase();
	}
	
	private boolean isImageExtension(String extension) {
		
		String testExtension = extension.toLowerCase();
		
		for(String validExtension: imageExtensions.split(",")) {
			if(testExtension.equals(validExtension)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	// photo093
	
	private File makeSubdirectory(String basePath, String prefix) {
		
		int nDirectory = random.nextInt(1000);
		
		String sDirectory = String.format("%s%03d", prefix, nDirectory);
		
		File directory = new File(basePath, sDirectory);
		
		if(!directory.exists()) {
			directory.mkdir();
		}
		
		return directory;
	}
	
}
