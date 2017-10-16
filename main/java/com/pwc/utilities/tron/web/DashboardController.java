package com.pwc.utilities.tron.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pwc.utilities.tron.model.entity.Environment;
import com.pwc.utilities.tron.services.AdminService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

	private Logger logger = Logger.getLogger(DashboardController.class);
	
	@Autowired
	private AdminService adminService;

	
	private class DashboardObj {
		private String envName;
		private boolean status;
		private String envVersion;
		private String fwVersion;
		private Date nextDeployment;
		private boolean continuesDeploy;
		private Integer totalNumOfPackages;
		
		public String getEnvName() {
			return envName;
		}
		public void setEnvName(String envName) {
			this.envName = envName;
		}
		public boolean isStatus() {
			return status;
		}
		public void setStatus(boolean status) {
			this.status = status;
		}
		public String getEnvVersion() {
			return envVersion;
		}
		public void setEnvVersion(String envVersion) {
			this.envVersion = envVersion;
		}
		public String getFwVersion() {
			return fwVersion;
		}
		public void setFwVersion(String fwVersion) {
			this.fwVersion = fwVersion;
		}
		public Date getNextDeployment() {
			return nextDeployment;
		}
		public void setNextDeployment(Date nextDeployment) {
			this.nextDeployment = nextDeployment;
		}
		public boolean isContinuesDeploy() {
			return continuesDeploy;
		}
		public void setContinuesDeploy(boolean continuesDeploy) {
			this.continuesDeploy = continuesDeploy;
		}
		public Integer getTotalNumOfPackages() {
			return totalNumOfPackages;
		}
		public void setTotalNumOfPackages(Integer totalNumOfPackages) {
			this.totalNumOfPackages = totalNumOfPackages;
		}
		
	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	public List<DashboardObj> getDashboard() throws FileNotFoundException, IOException {
		
		Environment env = null;
		List<DashboardObj> list = new ArrayList<DashboardObj>();
		
		Iterable<Environment> envs = adminService.getAllEnvironemnts();
		Iterator<Environment> itr = envs.iterator();
		while(itr.hasNext()) {
			
			env = itr.next();
			DashboardObj dObj = new DashboardObj();
			dObj.setEnvName(env.getEnvName());
			dObj.setNextDeployment(env.getNextRun());
			dObj.setContinuesDeploy(env.isContinuesDeploy());
			File basePath = new File(env.getEnvPath());
			if(basePath.exists()) {
				File envIni = new File(basePath.getAbsolutePath()+File.separator+"etc"+File.separator+"ENVIRON.INI");
				if(envIni.exists()) {
					Properties p = new Properties();	
					p.load(new FileInputStream(envIni));
					logger.info(checkServerStatus(p.getProperty("WEB_WLHOST"),p.getProperty("WEB_WLSSLPORT")));
					dObj.setStatus(checkServerStatus(p.getProperty("WEB_WLHOST"),p.getProperty("WEB_WLSSLPORT")));
					dObj.setFwVersion(p.getProperty("FW_VERSION"));
					dObj.setEnvVersion(p.getProperty("TOP_VERSION"));					
				}
				
				File installedPackages = new File(basePath.getAbsolutePath()+File.separator+"etc"+File.separator+"installed_packages.txt");
				if(installedPackages.exists()) {
					LineNumberReader lnr = new LineNumberReader(new FileReader(installedPackages));
					lnr.skip(Long.MAX_VALUE);
					dObj.setTotalNumOfPackages(lnr.getLineNumber() + 1);
				}
			}
			list.add(dObj);
		}
				
		return list;
		//return adminService.getDashboard();
	}
	
	
	private boolean checkServerStatus(String host, String port) {
		    try (Socket ignored = new Socket(host, Integer.parseInt(port))) {
		        return true;
		    } catch (IOException ignored) {
		        return false;
		    }
		}
}
