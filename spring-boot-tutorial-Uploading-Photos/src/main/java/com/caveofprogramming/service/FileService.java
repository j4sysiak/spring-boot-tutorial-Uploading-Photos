package com.caveofprogramming.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.caveofprogramming.exceptions.InvalidFileException;

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
	
	
    public void /*FileInfo*/ saveImageFile(MultipartFile file, String baseDirectory, String subDirPrefix, String filePrefix 
			/*, int width, int height */) throws InvalidFileException, IOException /*, ImageTooSmallException*/ {

		int nFilename = random.nextInt(1000);

		String filename = String.format("%s%03d", filePrefix, nFilename);

		String extension = getFileExtensions(file.getOriginalFilename());

		if(extension == null) {
			throw new InvalidFileException("No file extension");
		}

		if(isImageExtension(extension) == false) {
			throw new InvalidFileException("Not an image file.");
		}

		File subDirectory = makeSubdirectory(baseDirectory, subDirPrefix);

		Path filepath = Paths.get(subDirectory.getCanonicalPath(), filename + "." + extension);

		Files.deleteIfExists(filepath);

		Files.copy(file.getInputStream(), filepath);

		// BufferedImage resizedImage = resizeImage(file, width, height);
		//
		// ImageIO.write(resizedImage, extension, filepath.toFile());
		//
		// return new FileInfo(filename, extension, subDirectory.getName(),
		// baseDirectory);

    }
	
}






























