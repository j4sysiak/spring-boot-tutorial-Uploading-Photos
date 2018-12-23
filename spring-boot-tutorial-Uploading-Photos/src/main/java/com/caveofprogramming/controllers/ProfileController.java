package com.caveofprogramming.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.validation.Valid;

import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.auditing.config.AuditingHandlerBeanDefinitionParser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.caveofprogramming.exceptions.InvalidFileException;
import com.caveofprogramming.model.Profile;
import com.caveofprogramming.model.SiteUser;
import com.caveofprogramming.service.FileService;
import com.caveofprogramming.service.ProfileService;
import com.caveofprogramming.service.SiteUserService;

@Controller
public class ProfileController {
	
	@Autowired
	private SiteUserService siteUserService;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private PolicyFactory htmlPolicy;
	
	@Autowired
	FileService fileService;
	
	@Value("${photo.upload.directory}")
	private String photoUploadDirectory;
	
	private SiteUser getUser() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		
		return siteUserService.get(email);
	}
	 
	@RequestMapping(value="/profile")
	public ModelAndView showProfile(ModelAndView modelAndView) {
    
 		SiteUser user = getUser();
 		Profile profile = profileService.getUserProfile(user);
 		
		modelAndView.getModel().put("profile", profile);
		 
		if(profile == null) {
			profile = new Profile();
			profile.setUser(user);
			profileService.save(profile);
		}
		
 		Profile webProfile = new Profile();
 		webProfile.safeCopyFrom(profile);
 		
 		modelAndView.getModel().put("profile", webProfile);
 		modelAndView.setViewName("app.profile");
 		
		return modelAndView;
	}

	@RequestMapping(value="/edit-profile-about", method=RequestMethod.GET)
	public ModelAndView editProfileAbout(ModelAndView modelAndView) {
		
		SiteUser user = getUser();
		Profile profile = profileService.getUserProfile(user);
		
		Profile webProfile = new Profile();
		webProfile.safeCopyFrom(profile);
		
		modelAndView.getModel().put("profile", webProfile);
		modelAndView.setViewName("app.editProfileAbout");
		
		return modelAndView;
	}
	
	
	@RequestMapping(value="/edit-profile-about", method=RequestMethod.POST)
	public ModelAndView editProfileAbout(ModelAndView modelAndView, @Valid Profile webProfile, BindingResult result) {
		
		modelAndView.setViewName("app.editProfileAbout");
		
		SiteUser user = getUser();
		Profile profile = profileService.getUserProfile(user);
		
		profile.safeMergeFrom(webProfile, htmlPolicy);
		
		if(!result.hasErrors()) {
			profileService.save(profile);
			modelAndView.setViewName("redirect:/profile");
		}
		
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/upload-profile-photo", method = RequestMethod.POST)
//	@ResponseBody // Return data in JSON format
	public ModelAndView /*ResponseEntity<PhotoUploadStatus>*/  handlePhotoUploads(ModelAndView modelAndView, @RequestParam("file") MultipartFile file) {

		modelAndView.setViewName("redirect:/profile");
		
		try {
			fileService.saveImageFile(file, photoUploadDirectory, "photos", "profile");
		} catch (InvalidFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} /*catch (ImageTooSmallException e) {
			e.printStackTrace();
		}*/
 
		
//		SiteUser user = getUser();
//		Profile profile = profileService.getUserProfile(user);
//
//		Path oldPhotoPath = profile.getPhoto(photoUploadDirectory);
//		
//		PhotoUploadStatus status = new PhotoUploadStatus(photoStatusOK);
//
//		try {
//			FileInfo photoInfo = fileService.saveImageFile(file, photoUploadDirectory, "photos", "p" + user.getId(),
//					100, 100);
//
//			profile.setPhotoDetails(photoInfo);
//			profileService.save(profile);
//
//			if (oldPhotoPath != null) {
//				Files.delete(oldPhotoPath);
//			}
//
//		} catch (InvalidFileException e) {
//			status.setMessage(photoStatusInvalid);
//			e.printStackTrace();
//		} catch (IOException e) {
//			status.setMessage(photoStatusIOException);
//			e.printStackTrace();
//		} catch (ImageTooSmallException e) {
//			status.setMessage(photoStatusTooSmall);
//			e.printStackTrace();
//		}
//
//		return new ResponseEntity(status, HttpStatus.OK);
		
		return modelAndView; 
	}
	
}






































