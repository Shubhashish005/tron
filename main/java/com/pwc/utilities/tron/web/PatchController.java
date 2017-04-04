package com.pwc.utilities.tron.web;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.pwc.utilities.tron.model.entity.Patch;
import com.pwc.utilities.tron.model.entity.Prerequisite;
import com.pwc.utilities.tron.services.AdminService;

@RestController
@RequestMapping("/patches")
public class PatchController {
	
	Logger logger = Logger.getLogger(PatchController.class);
	
	@Autowired  
	AdminService adminService;
		
	
	
	@RequestMapping(method = RequestMethod.POST)
	public Patch addPatch(@RequestBody Patch patch, HttpServletRequest request) {
		  Patch newPatch = adminService.addPatch(patch);
		  request.getSession().setAttribute("patch", newPatch);
		  return newPatch;
	}	

	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Patch> getPatchs() {
		  return adminService.getPatches();
	}	
	
	@RequestMapping(value = "unset", method = {RequestMethod.GET})
	public void unsetSession(HttpServletRequest request){
		request.getSession().removeAttribute("patch");
	}
	
	@RequestMapping(value = "/download-package", method = {RequestMethod.GET})
	public ResponseEntity<InputStreamResource> downloadPatch(HttpServletRequest request) throws Exception {
		Patch patch = (Patch) request.getSession().getAttribute("patch");
		String srcFolder = Paths.get("storage/"+patch.getName()).toAbsolutePath().toString();
		String destZipFile = Paths.get("storage/"+patch.getName()+".zip").toAbsolutePath().toString();
		File zippedPackage = new File(destZipFile);
		adminService.zipFolder(srcFolder, destZipFile);
		
		InputStreamResource resource = new InputStreamResource(new FileInputStream(zippedPackage));
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-disposition", "attachment; filename=" + patch.getName()+".zip");

		 return ResponseEntity.ok()
				 	.headers(responseHeaders)
		            .contentLength(zippedPackage.length())
		            .contentType(MediaType.parseMediaType("application/zip"))
		            .body(resource);
	}
	
	
	@RequestMapping(value = "/details", method = {RequestMethod.GET})
	public Patch getDetails(HttpServletRequest request) {
		Patch patch = (Patch) request.getSession().getAttribute("patch");
		return adminService.getPatch(patch.getId());
	}
	
	
	@RequestMapping(value = "/add-bundle", method = {RequestMethod.POST})
	public Map<String, String> addBundle(@RequestBody Map<String, List<Map<String, String>>> payload, HttpServletRequest request){
	
		Patch patch = (Patch) request.getSession().getAttribute("patch");
		List<Map<String, String>> bundles = payload.get("data");
		Path storagePath  = Paths.get("storage").toAbsolutePath();
		
		//Loops Through each Prereqs and Moves Files If Uploaded.
		// Create Package folder if not existing
		File file = new File(storagePath+File.separator+patch.getName()+File.separator+"Bundle");
		if (!file.exists()) {
			if (file.mkdirs()) {
				logger.info(">>> Package Created under "+file);
				//storagePath = file.toPath();
			}
			else {
				logger.error(">> Couldnot create folder "+file);
			}
		}
		
		for (Map<String, String> map : bundles) {

			
			File bundle = new File(storagePath+File.separator+map.get("fileName"));
			logger.info("Source>>> "+bundle);
			logger.info("DEST>>> "+file.getPath());
			if(bundle.exists()) {
				bundle.renameTo(new File(file.getPath()+File.separator+bundle.getName()));
			}			
		}
		
		
		
		HashMap<String, String> response = new HashMap<String, String>();
		response.put("Response", "Ok");
		return response;
	}
	
	@RequestMapping(value = "/add-prereq", method = { RequestMethod.POST })
	public Map<String, String> addPrereq(@RequestBody Map<String, List<Map<String, String>>> payload, HttpServletRequest request) {
		
		Patch patch = (Patch) request.getSession().getAttribute("patch");
		List<Map<String, String>> prereqs = payload.get("data");
		Path storagePath  = Paths.get("storage").toAbsolutePath();
		
		//Loops Through each Prereqs and Moves Files If Uploaded.
		// Create Package folder if not existing
		File file = new File(storagePath+File.separator+patch.getName()+File.separator+"Prerequisite");
		if (!file.exists()) {
			if (file.mkdirs()) {
				logger.info(">>> Package Created under "+file);
				//storagePath = file.toPath();
			}
			else {
				logger.error(">> Couldnot create folder "+file);
			}
		}
		
		for (Map<String, String> map : prereqs) {
			Prerequisite prereq = new Prerequisite();
			prereq.setPackageName(map.get("packageName"));
			prereq.setPatch(patch);
			adminService.addPrerequisite(prereq);
			
			File preReqPackage = new File(storagePath+File.separator+map.get("packageName"));
			logger.info("Source>>> "+preReqPackage);
			logger.info("DEST>>> "+file.getPath());
			if(preReqPackage.exists()) {
				preReqPackage.renameTo(new File(file.getPath()+File.separator+preReqPackage.getName()));
			}			
		}
		
		HashMap<String, String> response = new HashMap<String, String>();
		response.put("Response", "Ok");
		return response;
	}
	
}
