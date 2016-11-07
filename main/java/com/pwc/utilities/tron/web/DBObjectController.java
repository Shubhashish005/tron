package com.pwc.utilities.tron.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pwc.utilities.tron.model.entity.Environment;
import com.pwc.utilities.tron.model.entity.Notification;
import com.pwc.utilities.tron.model.entity.Patch;
import com.pwc.utilities.tron.model.entity.PatchDb;
import com.pwc.utilities.tron.services.AdminService;

@RestController
@RequestMapping("/db-objects")
public class DBObjectController {

	Logger logger = Logger.getLogger(DBObjectController.class);
	
	@Autowired
	AdminService adminService;
	
	@RequestMapping(method = RequestMethod.POST)
	public String generateBluePrint(@RequestBody Map<String, List<Map<String, List<Map<String, String>>>>> payload, HttpServletRequest request) {

		Path pathToOracle = Paths.get("tools/Oracle").toAbsolutePath();
		Path storagePath  = Paths.get("storage").toAbsolutePath();
		
		Patch patch = (Patch) request.getSession().getAttribute("patch");
		
		

		try {
			
			// Create Package folder if not existing
			File file = new File(storagePath+File.separator+patch.getName()+File.separator+"Blueprint");
			if (!file.exists()) {
				if (file.mkdir()) {
					logger.info(">>> Package Created under "+file);
					storagePath = file.toPath();
				}
				else {
					logger.error(">> Couldnot create folder "+file);
				}
			}
			
			
			File inpFile = new File(file+File.separator+"input.inp");
			File inpListFile = new File(file+File.separator+"input-list.inp");
			File logFile = new File(file+File.separator+"output.log");
			
			logger.info("Creating BluePrint File under path >>> "+inpFile);
			
			PrintWriter inpFileWriter = new PrintWriter(inpFile);
			PrintWriter inpListFileWriter = new PrintWriter(inpListFile);
			PrintWriter logFileWriter = new PrintWriter(logFile);
			
			Iterator<Map<String, List<Map<String, String>>>> payloadListItr = payload.get("data").iterator();
			String objName = "";
			while(payloadListItr.hasNext())
			{
				Map<String, List<Map<String, String>>> payLoadMap = payloadListItr.next();
				List<Map<String, String>> tableRows = payLoadMap.get("objectType");
				Iterator<Map<String, String>> tableRowItr = tableRows.iterator();
				BigDecimal seqNo = BigDecimal.TEN;
				while(tableRowItr.hasNext()) {
					
					Map<String, String> tableDetails = tableRowItr.next();
					
					if(!tableDetails.get("objName").equalsIgnoreCase(objName))
					{
						objName = tableDetails.get("objName");
						inpFileWriter.println("########################################################################");
						inpListFileWriter.println("########################################################################");
						
						inpFileWriter.println("# "+objName);
						inpListFileWriter.println("# "+objName);
						
						inpFileWriter.println("########################################################################");
						inpListFileWriter.println("########################################################################");
					}
					inpFileWriter.println(tableDetails.get("table")+";"+tableDetails.get("condition")+";"+tableDetails.get("exclution"));
					inpListFileWriter.println(tableDetails.get("table")+";"+tableDetails.get("condition")+";"+tableDetails.get("inSw")+";"+tableDetails.get("upSw")+";"+tableDetails.get("dlSw")+";"+tableDetails.get("frSw")+";");
					PatchDb patchDb = new PatchDb();
					patchDb.setPatch(patch);
					patchDb.setSeqNo(seqNo);
					patchDb.setTableName(tableDetails.get("table"));
					patchDb.setFilterCriteria(tableDetails.get("condition"));
					patchDb.setFieldExclusions(tableDetails.get("exclution"));
					patchDb.setInserAllowedSw(tableDetails.get("inSw"));
					patchDb.setUpdateAllowedSw(tableDetails.get("upSw"));
					patchDb.setDelete_allow_sw(tableDetails.get("dlSw"));
					patchDb.setFreshInstallSw(tableDetails.get("frSw"));
					adminService.addPatchDb(patchDb);	
					seqNo = seqNo.add(BigDecimal.TEN);
					
				}
				//inpFileWriter.println("");
				//inpFileWriter.println("########################################################################");
				//inpListFileWriter.println("########################################################################");
			}
			
			inpFileWriter.close();
			inpListFileWriter.close();
			
			Environment environment = patch.getEnv();
			
			long timestamp = new Date().getTime();
			String scriptName = "extractBlueprint"+timestamp+".cmd";
			
		
			File scriptFile = new File(pathToOracle+File.separator+scriptName);
			PrintWriter scriptFileWriter = new PrintWriter(scriptFile);
			scriptFileWriter.println("set PATH=%cd%\\OracleClient32Bit;%PATH%;");
			scriptFileWriter.println("set SID="+environment.getDbSid());
			scriptFileWriter.println("set CISADM_USER="+environment.getDbUser());
			scriptFileWriter.println("set CISADM_PSWD="+environment.getDbPswd());
			scriptFileWriter.println("set NLS_LANG=AL32UTF8");
			scriptFileWriter.println("set CM_OWNER=CM");
			scriptFileWriter.println("OraSDBp.exe -d %CISADM_USER%,%CISADM_PSWD%,%SID% -i %1 -o %2 -c %NLS_LANG%");
			scriptFileWriter.close();
			
			
			ProcessBuilder pb = new ProcessBuilder(pathToOracle+File.separator+scriptName,file+File.separator+"input.inp",file+File.separator+"blueprint");
			pb.directory(new File(pathToOracle.toString()));
			Process process = pb.start();
			process.waitFor();			
			
			
			final BufferedReader wr = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        final BufferedWriter writer = new BufferedWriter(
	                new OutputStreamWriter(process.getOutputStream()));
	        String line = "";
	        while ((line = wr.readLine()) != null) {
	        	logger.info(line);
	        	logFileWriter.println(line);
	        }
	        logFileWriter.close();
			process.waitFor();
			
			if(scriptFile.exists())
				scriptFile.delete();
			
		} 
		catch (IOException e) 
		{		
			logger.error("IO Error Happned"+e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		finally
		{
			
		}
		return null;
	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	public Map<String, List<HashMap<String, String>>> retreive(HttpServletRequest request) {
		Patch patch = (Patch) request.getSession().getAttribute("patch");
		return adminService.retreiveSysTableLst(patch.getEnv().getProduct());
		
	}
	
}
