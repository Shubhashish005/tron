package com.pwc.utilities.tron.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.io.Files;
import com.pwc.utilities.tron.model.entity.Environment;
import com.pwc.utilities.tron.model.entity.Notification;
import com.pwc.utilities.tron.services.AdminService;

@RestController
@RequestMapping("/apply-package")
public class ApplyCMController {

	private Logger logger = Logger.getLogger(ApplyCMController.class);
	
	@Autowired
	AdminService adminService;
	
	@RequestMapping(value = "/status", method = RequestMethod.GET, produces = "application/json")
	public Iterable<Notification> applyStatus() {		
		return adminService.getAllNotifications();
	}
	
	@RequestMapping(value = "/check-package", method = RequestMethod.POST)
	public Map<String, Boolean> checkFile(@RequestBody String fileName, HttpServletRequest request){
		Environment environment  = (Environment) request.getSession().getAttribute("environment");
		File file = new File(environment.getPathToPackages()+File.separator+fileName);
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("FileExist", file.exists());
		return map;
	}
	
	@RequestMapping(method = RequestMethod.POST) 
	public void applyCm(@RequestBody List<Map<String, String>> payload, HttpServletRequest request)  {
		
		Environment environment = (Environment) request.getSession().getAttribute("environment");		
		adminService.deleteAllNotifications();

		for (Map<String, String> map : payload) {

			try {
				
				String packageName = map.get("fileName");
				adminService.unzip(packageName);

				if (packageName.indexOf(".") > 0)
					packageName = packageName.substring(0, packageName.lastIndexOf("."));

				// Apply All Prereq				
				adminService.applyPrereq(packageName, environment);
				
				// Apply DB Blueprint
				adminService.applyBlueprint(packageName, environment);

				// Apply App Package				
				adminService.applyPackage(packageName, environment);
				
				// Apply Bundle
				adminService.applyBundle(packageName, environment);


			} 
			catch (Exception e) {
				logger.info(e.toString());
			}
		}
	}
	
	@RequestMapping(value = "/service-pack", method = RequestMethod.POST)
	public void applySP(@RequestBody List<Map<String, String>> payload, HttpServletRequest request) throws IOException, InterruptedException{
		
		Environment environment = (Environment) request.getSession().getAttribute("environment");
		adminService.deleteAllNotifications();
		
		for (Map<String, String> map : payload) {
			
			
				String spName = map.get("fileName");
				adminService.unzip(spName);
				logger.info("spName >>> "+spName);
				if (spName.indexOf(".") > 0)
					spName = spName.substring(0, spName.lastIndexOf("."));
				logger.info("spName >>> "+spName);
				adminService.applySP(spName, environment);

				} 

		}	
}
