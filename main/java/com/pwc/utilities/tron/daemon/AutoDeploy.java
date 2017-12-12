package com.pwc.utilities.tron.daemon;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pwc.utilities.tron.model.entity.Environment;
import com.pwc.utilities.tron.services.AdminService;

@Component
public class AutoDeploy {

	private Logger logger = Logger.getLogger(MonitorShareFolder.class);
	
	@Autowired
	AdminService adminService;
	
	@Scheduled(fixedRateString = "${scheduling.rate}")
	public void applyPackages() throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		
		Iterator<Environment> environments= adminService.getAllEnvironemnts().iterator();
		while(environments.hasNext()) {
			Environment environment = environments.next();
			logger.info(environment.getEnvName());
			boolean shouldUpdateTimeStamp = false;
			if(environment.getWatchFolder() == null)
				continue;
			
			
			if(!environment.isContinuesDeploy() && environment.getNextRun().after(new Date()))
				continue;
			
			Path bPath = Paths.get(environment.getWatchFolder());
			File basePath = bPath.toFile();
			logger.info(basePath);
			if(basePath.exists()) {
				File timeStampFile = new File(basePath.getAbsolutePath()+File.separator+"referencetime.json");
				if(!timeStampFile.exists()) {
					mapper.writeValue(timeStampFile, new Date());
				} 
				final Date timeReference = mapper.readValue(timeStampFile, Date.class);
				
				File directoryLising[] = basePath.listFiles(new FileFilter() {				
					@Override
					public boolean accept(File pathname) {
						try 
						{

							BasicFileAttributes attributes = Files.readAttributes(pathname.toPath(), BasicFileAttributes.class);
							logger.info("ATR TIME "+attributes.lastAccessTime().to(TimeUnit.MILLISECONDS));
							logger.info("REF TIME "+timeReference.getTime());
							if(attributes.lastAccessTime().to(TimeUnit.MILLISECONDS) > timeReference.getTime()) {
								return true;
							}
							
						} catch (IOException e) {
							e.printStackTrace();
						}
						return false;
					}
				});
				
				for (File file : directoryLising) {
					logger.info(file);
					//Files.copy(file.getInputStream(), Paths.get("storage"+File.separator+file.getOriginalFilename()).toAbsolutePath());
					Files.deleteIfExists(Paths.get("storage/tmp/"+file.getName()));
					Files.copy(file.toPath(), new FileOutputStream(Paths.get("storage/tmp/"+file.getName()).toFile()));
					adminService.unzip(file.getName());
					adminService.applyPackage(FilenameUtils.removeExtension(file.getName()) , environment);
					adminService.applyBundle(FilenameUtils.removeExtension(file.getName()), environment);
					shouldUpdateTimeStamp = true;
				}	
				if(shouldUpdateTimeStamp)
					mapper.writeValue(timeStampFile, new Date());
				    if(environment.getNextRun().before(new Date())){
				    	environment.setNextRun(null);
				    	adminService.addEnvironment(environment);
				    }
			}
			
			
		}
		
		
	}
	
}
