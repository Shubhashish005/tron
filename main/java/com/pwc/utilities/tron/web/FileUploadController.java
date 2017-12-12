package com.pwc.utilities.tron.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.maven.shared.utils.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pwc.utilities.tron.model.entity.Environment;
import com.pwc.utilities.tron.model.entity.Patch;

@RestController
public class FileUploadController {

	private Logger logger = Logger.getLogger(FileUploadController.class);
	
	private List<Map<String, String>> childern = new ArrayList<Map<String,String>>();
	
	@RequestMapping(value = "/file-upload", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request){
		
		try {
				if(file.isEmpty()) {
					return new ResponseEntity(HttpStatus.BAD_REQUEST);
				}  
				logger.info(">> >> >> File ");
				logger.info(file.getOriginalFilename());
				logger.info(file.getName());
				logger.info(file.getSize());
				//logger.info(Paths.get("storage").toAbsolutePath());
				
				//Delete File is exists
				Files.deleteIfExists(Paths.get("storage"+File.separator+file.getOriginalFilename()));
				
				Patch patch = (Patch) request.getSession().getAttribute("patch");
				
				if(patch == null)
					Files.copy(file.getInputStream(), Paths.get("storage"+File.separator+file.getOriginalFilename()).toAbsolutePath());
				else {
					
					File docLocation = Paths.get("storage"+File.separator+patch.getName()+File.separator+"docs").toFile();
					if(!docLocation.exists())
						docLocation.mkdirs();
					Files.deleteIfExists(Paths.get("storage"+File.separator+patch.getName()+File.separator+"docs"+File.separator+file.getOriginalFilename()));
					Files.copy(file.getInputStream(), Paths.get("storage"+File.separator+patch.getName()+File.separator+"docs"+File.separator+file.getOriginalFilename()).toAbsolutePath());
				}
				
		}
		catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			if(e.getCause() != null)
				logger.info(e.getCause().getMessage());
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/upload-package", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> handlePackageUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request){
		
		try {
				if(file.isEmpty()) {
					return new ResponseEntity(HttpStatus.BAD_REQUEST);
					
				}  
				logger.info(">> >> >> Package ");
				logger.info(file.getOriginalFilename());
				logger.info(file.getName());
				logger.info(file.getSize());
				//logger.info(Paths.get("storage").toAbsolutePath());
				
				Path workingDir = Paths.get("storage/tmp");
				
				Environment environment = (Environment) request.getSession().getAttribute("environment");
				if(environment != null) {
					File packageLocation = new File(environment.getPathToPackages());
					if(!packageLocation.exists())
						packageLocation.mkdirs();
				}
				
				//Delete File is exists
				Files.deleteIfExists(Paths.get(workingDir+File.separator+file.getOriginalFilename()));				
				Files.copy(file.getInputStream(), Paths.get(workingDir+File.separator+file.getOriginalFilename()).toAbsolutePath());
				
		}
		catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			if(e.getCause() != null)
				logger.info(e.getCause().getMessage());
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.OK);
	}	
	
	@RequestMapping(value = "/delete-prereq", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> deletePrereq(@RequestBody Map<String, String> payload, HttpServletRequest request) {
		
		try
		{
			logger.info(payload.get("fileName"));
			String fileName = payload.get("fileName");
			Files.deleteIfExists(Paths.get("storage"+File.separator+fileName));
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			
		}
		
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/list-files", method = RequestMethod.POST)
	public List<Map<String, String>> listFileFolder(@RequestBody Map<String, String> payload, HttpServletRequest request) {
		List<Map<String, String>> response = new ArrayList<Map<String,String>>();
		logger.info(payload.get("path"));	
		
		Patch patch = (Patch) request.getSession().getAttribute("patch");
		
		
		File file = new File(patch.getEnv().getEnvPath()+File.separator+payload.get("path"));
		logger.info("File Path "+file);
		if(!file.exists())
		{
			file = new File(payload.get("path"));
		}
		if(file.isDirectory())
		{
			File folderListing[] = file.listFiles();
			for(File eachFile : folderListing) {
				Map<String, String> fileEntry = new HashMap<String, String>();
				fileEntry.put("id", eachFile.getAbsolutePath());
				fileEntry.put("parent", payload.get("path"));
				fileEntry.put("text", eachFile.getName());
				if(eachFile.isFile()) {
					fileEntry.put("icon", "jstree-icon jstree-file");
				}
				response.add(fileEntry);
			}
		}
		return response;
	}
	
	@RequestMapping(value = "/list-pacakge", method = RequestMethod.GET)
	public void listPackage(HttpServletRequest request) {
		Patch patch = (Patch) request.getSession().getAttribute("patch");
		
		
	}

	@RequestMapping(value = "/set-sp", method = RequestMethod.POST)
	public void setSP(@RequestBody Map<String, String> payload, HttpServletRequest request){
		String spName = payload.get("sp");
		request.getSession().setAttribute("spName", spName);
	}
	
	@RequestMapping(value = "/create-sp", method = RequestMethod.POST)
	public void createSP(@RequestBody List<Map<String, String>> payload, HttpServletRequest request) throws IOException {
		
		String spName = (String) request.getSession().getAttribute("spName");
		Path spPath = Paths.get("storage/servicepacks/"+spName);
		if(spPath.toFile().exists())
			FileUtils.deleteDirectory(spPath.toFile());
			//spPath.toFile().delete();
		spPath.toFile().mkdirs();
		File packageListFile = new File(spPath.toFile()+File.separator+"package-list.txt");
		PrintWriter pw = new PrintWriter(packageListFile);
		for (Map<String, String> packages : payload) {
			String packageName = packages.get("packageName");
			logger.info("Package >>>"+packageName);
			FileOutputStream foutStream  = new FileOutputStream(new File(spPath.toAbsolutePath()+File.separator+packageName));
			Files.copy(Paths.get("storage/tmp/"+packageName), foutStream);
			
			foutStream.close();
			
			if(packageName.lastIndexOf(".") > 0)
				packageName = packageName.substring(0, packageName.lastIndexOf("."));
			
			pw.println(packageName);
		}
		pw.close();
		logger.info("Service Pack Created");
		
	}
	
	@RequestMapping(value = "/parents-childerns", method = RequestMethod.POST)
	public List<Map<String, String>> listParentandChilderns(@RequestBody Map<String, String> payload, HttpServletRequest request){
		List<Map<String, String>> response = new ArrayList<Map<String,String>>();
		ArrayList<String> parentNames = new ArrayList<String>();
		parentNames.add("cobol");
		parentNames.add("etc");
		parentNames.add("java");
		childern = new ArrayList<Map<String,String>>();
		
		logger.info(payload.get("path"));
		boolean moreParents = true;
		
		Patch patch = (Patch) request.getSession().getAttribute("patch");
		File file = new File(patch.getEnv().getEnvPath()+File.separator+payload.get("path"));
		if(!file.exists())
		{
			file = new File(payload.get("path"));
		}
		
		File folderListing[] = file.listFiles();
		
		if(file.isDirectory())
		{
			logger.info("List Directory for >>> "+file.getAbsolutePath());
			listDirectory(file.getAbsolutePath());
			logger.info("list of childerns...");
			logger.info(childern);
			response.addAll(childern);
		}
		logger.info("Parents...");
		//Get all parent nodes
		File currentFile = file;
		while(moreParents) {
			Map<String, String> fileEntry = new HashMap<String, String>();
			File parent = currentFile.getParentFile();
			fileEntry.put("id", currentFile.getAbsolutePath());
			
			if(parentNames.contains(parent.getName())) {
				moreParents = false;
				fileEntry.put("parent",parent.getName());
			}
			else
				fileEntry.put("parent", parent.getAbsolutePath());
			
			fileEntry.put("text", currentFile.getName());
			if(currentFile.isFile()){
				fileEntry.put("icon", "jstree-icon jstree-file");
			}
			logger.info(fileEntry);
			response.add(fileEntry);
			currentFile = parent;

		}
		

		return response;
	}
	
	public void listDirectory(String dirPath) {
		logger.info("Listing for dirPath >>> "+dirPath);
	    File dir = new File(dirPath);
	    File[] firstLevelFiles = dir.listFiles();
	    if (firstLevelFiles != null && firstLevelFiles.length > 0) {
	        for (File aFile : firstLevelFiles) {

	            if (aFile.isDirectory()) {
	                logger.info("[" + aFile.getName() + "]");
	                Map<String, String> fileEntry = new HashMap<String, String>();
					fileEntry.put("id", aFile.getAbsolutePath());
					fileEntry.put("parent", aFile.getParentFile().getAbsolutePath());
					fileEntry.put("text", aFile.getName());
	                listDirectory(aFile.getAbsolutePath());
	                childern.add(fileEntry);
	            } else {
	                logger.info(aFile.getName());
	                Map<String, String> fileEntry = new HashMap<String, String>();
					fileEntry.put("id", aFile.getAbsolutePath());
					fileEntry.put("parent", aFile.getParentFile().getAbsolutePath());
					fileEntry.put("text", aFile.getName());
					fileEntry.put("icon", "jstree-icon jstree-file");
					childern.add(fileEntry);	                
	            }
	        }
	    }
	}
	

}
