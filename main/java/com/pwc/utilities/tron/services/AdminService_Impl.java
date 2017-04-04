package com.pwc.utilities.tron.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;
import com.pwc.utilities.tron.model.entity.Dashboard;
import com.pwc.utilities.tron.model.entity.DbObj;
import com.pwc.utilities.tron.model.entity.Environment;
import com.pwc.utilities.tron.model.entity.InstallRecord;
import com.pwc.utilities.tron.model.entity.Notification;
import com.pwc.utilities.tron.model.entity.Patch;
import com.pwc.utilities.tron.model.entity.PatchApp;
import com.pwc.utilities.tron.model.entity.PatchDb;
import com.pwc.utilities.tron.model.entity.Prerequisite;
import com.pwc.utilities.tron.model.entity.ServicePack;
import com.pwc.utilities.tron.model.repository.DBConfigurationRepository;
import com.pwc.utilities.tron.model.repository.DashboardRepository;
import com.pwc.utilities.tron.model.repository.DbObjRepository;
import com.pwc.utilities.tron.model.repository.EnvironmentRepository;
import com.pwc.utilities.tron.model.repository.InstallRecordRepository;
import com.pwc.utilities.tron.model.repository.NotificationRepository;
import com.pwc.utilities.tron.model.repository.PatchAppRepository;
import com.pwc.utilities.tron.model.repository.PatchDbRepository;
import com.pwc.utilities.tron.model.repository.PatchRepository;
import com.pwc.utilities.tron.model.repository.PrerequisiteRepository;
import com.pwc.utilities.tron.model.repository.ServicePackRepository;

@Service
class AdminService_Impl implements AdminService {
	
	private Logger logger = Logger.getLogger(AdminService_Impl.class);

	//@Autowired
	private DBConfigurationRepository dbConfigRepo;
	
	//@Autowired
	private DbObjRepository dbObjRepo;
	
	@Autowired
	private EnvironmentRepository envrRepo;
	
	@Autowired
	private InstallRecordRepository instRecRepo;
	
	@Autowired
	private PatchDbRepository patchDbRepo;
	
	@Autowired
	private PatchAppRepository patchAppRepo;
	
	@Autowired
	private PatchRepository patchRepo;
	
	//@Autowired
	private ServicePackRepository servicePackRepo;
	
	//@Autowired
	private DashboardRepository dashboardRepo;
	
	@Autowired
	private PrerequisiteRepository prerequisiteRepo;

	@Autowired
	private NotificationRepository notificationRepo;

	@Override
	public Iterable<Environment> getAllEnvironemnts() {
		// TODO Auto-generated method stub
		return envrRepo.findAll();
	}
	
	@Override
	public Environment getEnvironment(Integer envID) {
		// TODO Auto-generated method stub
		return envrRepo.findOne(envID);
	}

	@Override
	public Environment addEnvironment(Environment environment) throws Exception {
		if(validateEnvironment(environment))
			return envrRepo.save(environment);
		else
			throw new Exception("Unable to Connect to Database");		
	}
	private boolean validateEnvironment(Environment environment) throws Exception {
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection(	"jdbc:oracle:thin:@"+environment.getDbHost()+":"+environment.getDbPort()+":"+environment.getDbSid(),environment.getDbUser(),environment.getDbPswd());  
		} catch (ClassNotFoundException e) {
			logger.error("CLASS NOT FOUND "+e.getMessage());
			return false;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}  
		  
		if(!new File(environment.getEnvPath()).exists())
			throw new Exception("Invalid SPLEBASE");
		
		return true;
		
	}

	@Override
	public void deleteEnvironment(Integer envId) {
		// TODO Auto-generated method stub
		envrRepo.delete(envId);
	}
	
	@Override
	public void deleteEnvironment(Environment environment) {
		// TODO Auto-generated method stub
		envrRepo.delete(environment);
	}

	
	public Iterable<Patch> getPatches() {
		return patchRepo.findAll();
	}
	
	@Override
	public Patch getPatch(Integer patchId) {
		// TODO Auto-generated method stub
		return patchRepo.findOne(patchId);
	}

	@Override
	public Patch addPatch(Patch patch) {
		// TODO Auto-generated method stub
		return patchRepo.save(patch);
	}

	@Override
	public void deletePatch(Integer patchId) {
		// TODO Auto-generated method stub
		patchRepo.delete(patchId);
	}

	
	@Override
	public Iterable<DbObj> getAllDBObject() {
		// TODO Auto-generated method stub
		return dbObjRepo.findAll();
	}

	@Override
	public DbObj getDBObject(Integer dbObjId) {
		// TODO Auto-generated method stub
		return dbObjRepo.findOne(dbObjId);
	}

	@Override
	public DbObj addDBObject(DbObj dbObj) {
		// TODO Auto-generated method stub
		return dbObjRepo.save(dbObj);
	}

	@Override
	public void deleteDBBObject(Integer dbObjId) {
		// TODO Auto-generated method stub
		dbObjRepo.delete(dbObjId);
	}

	@Override
	public void deleteDBBObject(DbObj dbObject) {
		// TODO Auto-generated method stub
		dbObjRepo.delete(dbObject);
	}

	@Override
	public Iterable<PatchDb> getAllPatchDB() {
		// TODO Auto-generated method stub
		return patchDbRepo.findAll();
	}

	

	@Override
	public PatchDb getPatchDB(Integer patchId) {
		// TODO Auto-generated method stub
		return patchDbRepo.findOne(patchId);
	}

	@Override
	public PatchDb addPatchDb(PatchDb patchDb) {
		// TODO Auto-generated method stub
		return patchDbRepo.save(patchDb);
	}

	@Override
	public void deletePatchDB(Integer patchId) {
		// TODO Auto-generated method stub
		patchDbRepo.delete(patchId);
		
	}

	@Override
	public void deletePatchDB(PatchDb patchDB) {
		// TODO Auto-generated method stub
		patchDbRepo.delete(patchDB);
		
	}

	@Override
	public Iterable<PatchApp> getAllPatchApp(Patch patch) {
		return patchAppRepo.getFilesForPatch(patch);
	}

	@Override
	public PatchApp getPatchApp(Integer patchId) {
		// TODO Auto-generated method stub
		return patchAppRepo.findOne(patchId);
	}

	@Override
	public PatchApp addPatchApp(PatchApp patchApp) {
		return patchAppRepo.save(patchApp);
	}

	@Override
	public void deletePatchApp(Integer patchId) {
		// TODO Auto-generated method stub
		patchAppRepo.delete(patchId);
	}

	@Override
	public void deletePatchApp(PatchApp patchApp) {
		// TODO Auto-generated method stub
		patchAppRepo.delete(patchApp);
		
	}

	@Override
	public Iterable<ServicePack> getAllServicePackes() {
		// TODO Auto-generated method stub
		return servicePackRepo.findAll();
	}

	@Override
	public ServicePack getServicePack(Integer spId) {
		// TODO Auto-generated method stub
		return servicePackRepo.findOne(spId);
	}

	@Override
	public ServicePack addServicePack(ServicePack servicePack) {
		// TODO Auto-generated method stub
		return servicePackRepo.save(servicePack);
	}

	@Override
	public void deleteServicePack(Integer spId) {
		// TODO Auto-generated method stub
		servicePackRepo.delete(spId);
	}

	@Override
	public void deleteServicePack(ServicePack servicePack) {
		// TODO Auto-generated method stub
		servicePackRepo.delete(servicePack);
	}

	@Override
	public Iterable<InstallRecord> getAllInstallRecords() {
		// TODO Auto-generated method stub
		return instRecRepo.findAll();
	}

	@Override
	public InstallRecord getInstallRecord(Integer installId) {
		// TODO Auto-generated method stub
		return instRecRepo.findOne(installId);
	}

	@Override
	public InstallRecord addInstallRecord(InstallRecord installRecord) {
		// TODO Auto-generated method stub
		return instRecRepo.save(installRecord);
	}

	@Override
	public void deleteInstallRecord(Integer installId) {
		instRecRepo.delete(installId);
	}

	@Override
	public void deleteInstallRecord(InstallRecord installRecord) {
		instRecRepo.delete(installRecord);
	}


	@Override
	public Iterable<Dashboard> getDashboard() {
		return dashboardRepo.findAll();
	}

	
	

	@Override
	public void createNewEnvironment(String dbHost, int dbPort, String dbPswd, String dbUser, String dbSid,
			String envName, String envPath, String envVer, String project) {
		
	}

	@Override
	public Iterable<Object> getContentOfPatch() {
		return null;
	}

	@Override
	public Iterable<Prerequisite> getAllPrereqs() {
		return prerequisiteRepo.findAll();
	}

	@Override
	public Iterable<Prerequisite> getAllPrereqs(Integer patchId) {
		return null;
	}

	@Override
	public Prerequisite addPrerequisite(Prerequisite prerequisite) {
		return prerequisiteRepo.save(prerequisite);
	}

	@Override
	public void deletePrerequisites(Integer patchId) {
		
	}

	@Override
	public void applyPrereq(String packageName, Environment environment) throws IOException, InterruptedException {

		Notification preReqNotification = createNotification("Applying Prereq for Package " + packageName,"In Progress");
		
		File preReqFolder = Paths.get("storage/tmp/"+packageName+"/Prerequisite").toFile();
		File packageList[] = preReqFolder.listFiles();
		if(packageList != null)
		for (File file : packageList) {			
			Files.copy(file,  new File(Paths.get("storage/tmp/").toFile()+File.separator+file.getName()));
			unzip(file.getName());
			packageName = file.getName();
			
			if (packageName.indexOf(".") > 0)
				packageName = packageName.substring(0, packageName.lastIndexOf("."));
			
			
			applyPrereq(packageName, environment);
			
			applyBlueprint(packageName, environment);

			applyPackage(packageName, environment);

		}
			
		preReqNotification.setStatus("Completed");
		updateNotification(preReqNotification);
	}

	private boolean checkApplied(String packageName, Environment environment) throws IOException {
		File packageListFile = Paths.get(environment.getEnvPath()+File.separator+"etc"+File.separator+"installed_packages.txt").toFile();
		BufferedReader br = new BufferedReader(new FileReader(packageListFile));
		String p = null;
		while((p = br.readLine()) != null) {
			if(p.trim().equalsIgnoreCase(packageName.trim()))
				return true;
		}
		return false;
	}
	
	@Override
	public void applyPackage(String packageName, Environment environment) throws IOException {
		
		Notification applyPackageNotification = createNotification("Applying Package " + packageName, "In Progress");
		try {
		
		
			
		Path pathToCmPackaging = Paths.get("tools/CM_packaging").toAbsolutePath();			
		Path pathToPackage = Paths.get("storage/tmp/"+packageName+"/Code").toAbsolutePath();		

		//Create a Temp Script in the ApplyCm Folder, this script will be delete at the end
		long timestamp = new Date().getTime();
		String scriptName = "runApplyCm"+timestamp+".cmd";
		
		File scriptFile = new File(pathToCmPackaging+File.separator+scriptName);
		PrintWriter scriptFileWriter = new PrintWriter(scriptFile);
		scriptFileWriter.println("CALL "+environment.getEnvPath()+File.separator+"bin"+File.separator+"splenviron.cmd -e "+environment.getEnvName());
		scriptFileWriter.println("cd "+pathToPackage.toString());
		scriptFileWriter.println("CALL "+pathToCmPackaging.toString()+File.separator+"applyCM.cmd");
		scriptFileWriter.close();
		
		

		ProcessBuilder pb = new ProcessBuilder(pathToCmPackaging+File.separator+scriptName);
		pb.directory(pathToPackage.toFile());
		pb.redirectOutput(new File(pathToPackage+File.separator+"out_apply.log"));
		pb.redirectError(new File(pathToPackage+File.separator+"error_apply.log"));
		Process process = pb.start();
		process.waitFor();
		
		//Delete the temp Script File
		if(scriptFile.exists())
			scriptFile.delete();	
		} catch(Exception e){
			
		}	
		
		// Move Applied Package to Package Repository
		File targetFolder = new File(environment.getPathToPackages() + File.separator + packageName + ".zip");
		if (!targetFolder.exists())
			targetFolder.getParentFile().mkdirs();

		Files.copy(Paths.get("storage/tmp/" + packageName + ".zip").toFile(), targetFolder.getAbsoluteFile());
		packageName = packageName+"\n";
		//Update Patch Details in etc
		if(Paths.get(environment.getEnvPath()+File.separator+"etc"+File.separator+"installed_packages.txt").toFile().exists())
			java.nio.file.Files.write(Paths.get(environment.getEnvPath()+File.separator+"etc"+File.separator+"installed_packages.txt"),packageName.getBytes(), StandardOpenOption.APPEND);
		else
			java.nio.file.Files.write(Paths.get(environment.getEnvPath()+File.separator+"etc"+File.separator+"installed_packages.txt"),packageName.getBytes(), StandardOpenOption.CREATE_NEW);
		
		applyPackageNotification.setStatus("Completed");
		updateNotification(applyPackageNotification);
	
	}

	
	
	
	@Override
	public void applyBlueprint(String packageName, Environment environment) throws IOException, InterruptedException {

		Path pathToOracle = Paths.get("tools/Oracle").toAbsolutePath();
		Path storagePath  = Paths.get("storage/tmp/"+packageName+"/Blueprint/").toAbsolutePath();
		
		long timestamp = new Date().getTime();
		String scriptName = "uploadBlueprint"+timestamp+".cmd";
		
		Notification applyDBNotification = createNotification("Applying Blueprint for " + packageName, "In Progress");
	
		File scriptFile = new File(pathToOracle+File.separator+scriptName);
		PrintWriter scriptFileWriter = new PrintWriter(scriptFile);
		scriptFileWriter.println("set PATH=%cd%\\OracleClient32Bit;%PATH%;");
		scriptFileWriter.println("set SID="+environment.getDbSid());
		scriptFileWriter.println("set CISADM_USER="+environment.getDbUser());
		scriptFileWriter.println("set CISADM_PSWD="+environment.getDbPswd());
		scriptFileWriter.println("set NLS_LANG=AL32UTF8");
		scriptFileWriter.println("set CM_OWNER=CM");
		scriptFileWriter.println("OraSDUpg -d %CISADM_USER%,%CISADM_PSWD%,%SID%  -m -b -i %2 -l OraSDUpg.log -q -p %1");
		scriptFileWriter.close();
		
		
		ProcessBuilder pb = new ProcessBuilder(pathToOracle+File.separator+scriptName,storagePath+File.separator+"input-list.inp",storagePath+File.separator+"blueprint");
		pb.directory(new File(pathToOracle.toString()));
		Process process = pb.start();
		process.waitFor();
		
		applyDBNotification.setStatus("Completed");
		updateNotification(applyDBNotification);
		
	}

	@Override
	public void unzip(String packageName) {

	     byte[] buffer = new byte[1024];
	     
	     String OUTPUT_FOLDER = Paths.get("storage/tmp").toAbsolutePath().toString();
	     String zipFile = Paths.get("storage/tmp").toAbsolutePath()+File.separator+packageName;
	     
	     try{

	    	//create output directory is not exists
	    	File folder = new File(OUTPUT_FOLDER);
	    	if(!folder.exists()){
	    		folder.mkdir();
	    	}

	    	ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
	    	ZipEntry ze = zis.getNextEntry();
	    	
	    	while(ze!=null){

	    	   String fileName = ze.getName();
	           File newFile = new File(folder + File.separator + fileName);

	            //create all non exists folders
	            //else you will hit FileNotFoundException for compressed folder
	            new File(newFile.getParent()).mkdirs();
	            
	            if(!ze.isDirectory()) {
		            FileOutputStream fos = new FileOutputStream(newFile);	
		            int len;
		            while ((len = zis.read(buffer)) > 0) {
		            	
		       		fos.write(buffer, 0, len);
		            }
	
		            fos.close();
	            }
	            else
	            	newFile.mkdirs();
	            
	            ze = zis.getNextEntry();
	    	}

	        zis.closeEntry();
	    	zis.close();

	    	System.out.println("Done");

	    }catch(IOException ex){
	       ex.printStackTrace();
	    }		
	}

	@Override
	public Iterable<Notification> getAllNotifications() {
		return notificationRepo.findAll();
	}

	@Override
	public void deleteAllNotifications() {
		notificationRepo.deleteAll();
	}

	@Override
	public Notification createNotification(String message, String status) {
		Notification notification = new Notification();
		notification.setMessage(message);
		notification.setStatus(status);
		return notificationRepo.save(notification);
	}

	@Override
	public Notification updateNotification(Notification notification) {
		return notificationRepo.save(notification);
	}

	@Override
	public Map<String, List<HashMap<String, String>>> retreiveSysTableLst(String app) {
		BufferedReader bufferedReader = null;
		Map<String, List<HashMap<String, String>>> dbStore =  new HashMap<String, List<HashMap<String,String>>>();
		try 
		{
			bufferedReader = new BufferedReader(new FileReader(Paths.get("tools/etc").toAbsolutePath()+File.separator+app+"_SYSTABLES.LST"));
			StringBuilder sb = new StringBuilder();
            String line = bufferedReader.readLine();
            String key = null;
            List<HashMap<String, String>> storeVals = null;
            while (line != null) {
                if (line.startsWith("#$")) {
                    line = line.replace('#', ' ');
                    line = line.replace('$', ' ');

                    if (key != null) {
                        dbStore.put(key, storeVals);
                    }
                    key = line.trim();
                    //storeVals = new ArrayList<String>();
                    storeVals = new ArrayList<HashMap<String,String>>();
                    //storeVals.add(key);
                } else if ((!line.startsWith("##") && key != null)) {
                    line = line.replace('#', ' ');
                    line = line.replace('$', ' ');
                    String value = line.trim();
                    //storeVals.add(value + ";");
                    String values[] = value.split(";");
                    HashMap<String, String> myMap = new HashMap<String, String>();
                    myMap.put("table", values[0]);
                    myMap.put("condition", values[1]);
                    myMap.put("exclution", values[2]);
                    myMap.put("inSw", "T");
                    myMap.put("upSw", "T");
                    myMap.put("dlSw", "F");
                    myMap.put("frSw", "T");
                    myMap.put("objName", key);
                    storeVals.add(myMap);
                    
                    
                    //System.out.println("\""+key+"\",\""+value+"\"");
                }
                line = bufferedReader.readLine();
            }
            
		} 
		catch (FileNotFoundException e) 
		{
			logger.error("systabled.lst File not found in the path "+Paths.get("storage").toAbsolutePath());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return dbStore;

	}

	@Override
	public void createCodePackage(List<Map<String, String>> fileDetails, File envPath, File file, Patch patch) {

		for (Map<String, String> map : fileDetails) {
			String fileToCopy = map.get("id");
			String destPath = fileToCopy.replace((CharSequence)envPath.getAbsolutePath(), (CharSequence)file.getAbsolutePath());
			File sourceFile = new File(fileToCopy);
			logger.info("Source >>"+sourceFile.getAbsolutePath());
			try {
			if(sourceFile.exists() && sourceFile.isFile()) {
					logger.info("Moving File "+sourceFile.getAbsolutePath());
					File destFile = new File(destPath);	
					logger.info("Destination Folder "+destFile);
					logger.info(destFile.getParentFile().mkdirs());
					Files.copy(sourceFile, destFile);
					
					PatchApp patchApp = new PatchApp();
					patchApp.setPatch(patch);
					patchApp.setFile(destFile.getName());
					patchApp.setPath(destFile.getParent());
					addPatchApp(patchApp);
				}	
			} 
			catch (IOException e) {
				logger.error("ERROR >>> "+e.getMessage());;
			}
			finally {
				
			}
			
		}
		
	}

	 	public void zipFolder(String srcFolder, String destZipFile) throws Exception {
		    ZipOutputStream zip = null;
		    FileOutputStream fileWriter = null;

		    fileWriter = new FileOutputStream(destZipFile);
		    zip = new ZipOutputStream(fileWriter);

		    addFolderToZip("", srcFolder, zip);
		    zip.flush();
		    zip.close();
		  }

		  private void addFileToZip(String path, String srcFile, ZipOutputStream zip)
		      throws Exception {

		    File folder = new File(srcFile);
		    if (folder.isDirectory()) {
		      addFolderToZip(path, srcFile, zip);
		    } else {
		      byte[] buf = new byte[1024];
		      int len;
		      FileInputStream in = new FileInputStream(srcFile);
		      zip.putNextEntry(new ZipEntry(path + File.separator + folder.getName()));
		      while ((len = in.read(buf)) > 0) {
		        zip.write(buf, 0, len);
		      }
		    }
		  }

		  private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip)
		      throws Exception {
		    File folder = new File(srcFolder);

		    for (String fileName : folder.list()) {
		      if (path.equals("")) {
		        addFileToZip(folder.getName(), srcFolder + File.separator + fileName, zip);
		      } else {
		        addFileToZip(path + File.separator + folder.getName(), srcFolder + File.separator + fileName, zip);
		      }
		    }
		  }

		@Override
		public void applySP(String spName, Environment environment) throws IOException, InterruptedException {
			
			File packageList = new File("storage/tmp/"+spName+File.separator+"package-list.txt");
			BufferedReader br = new BufferedReader(new FileReader(packageList));
			String packageName = null;
			
			while ((packageName = br.readLine()) != null) {
				Files.copy(new File("storage/tmp/"+spName+File.separator+packageName+".zip"), new File("storage/tmp/"+packageName+".zip"));
				
				if(!checkApplied(packageName, environment)) {
					unzip(packageName+".zip");
					applyPrereq(packageName, environment);				
					applyBlueprint(packageName, environment);				
					applyPackage(packageName, environment);
				} else {
					createNotification("Package "+packageName+" Already applyed", "Skipped");
				}
				
	        }
			
		}	
	
	
		  
	

}
