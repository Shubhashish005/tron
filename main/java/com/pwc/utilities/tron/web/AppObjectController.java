package com.pwc.utilities.tron.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.io.Files;
import com.pwc.utilities.tron.model.entity.Patch;
import com.pwc.utilities.tron.model.entity.PatchApp;
import com.pwc.utilities.tron.services.AdminService;

@RestController
@RequestMapping("/app-objects")
public class AppObjectController {
	
	private Logger logger = Logger.getLogger(AppObjectController.class);
	
	@Autowired
	AdminService adminService;
	
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<PatchApp> getFilesForPatch(HttpServletRequest request) {
		Patch patch = (Patch) request.getSession().getAttribute("patch");
		return adminService.getAllPatchApp(patch);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public Map<String, String> createCodePackage(@RequestBody Map<String, List<Map<String, String>>> payload, HttpServletRequest request) throws IOException {
		
		Patch patch = (Patch) request.getSession().getAttribute("patch");
		Path storagePath  = Paths.get("storage").toAbsolutePath();
		File file = new File(storagePath+File.separator+patch.getName()+File.separator+"code");
		if (!file.exists()) {
			if (file.mkdirs()) {
				logger.info(">>> Package Created under "+file);
			}
			else {
				logger.error(">> Couldnot create folder "+file);
			}		
		}
		
		String foldersToCreate[] = {"cobol"+File.separator+"cm","etc","java","services","splapp","structures"};
		for (String folder : foldersToCreate) {
			File tempFile = new File(file.getAbsolutePath()+File.separator+folder);
			tempFile.mkdirs();
		}
		
		Files.copy(new File(storagePath+File.separator+"extra"+File.separator+"cm_jars_structure.xml"), new File(file.getAbsoluteFile()+File.separator+"structures"+File.separator+"cm_jars_structure.xml"));
		
		File envPath = new File(patch.getEnv().getEnvPath());		
		
		List<Map<String, String>> fileDetails = payload.get("data");
		
		adminService.createCodePackage(fileDetails, envPath, file, patch);

		
		HashMap<String, String> response = new HashMap<String, String>();
		response.put("Response", "Ok");
		return response;
	}
	
	

}
