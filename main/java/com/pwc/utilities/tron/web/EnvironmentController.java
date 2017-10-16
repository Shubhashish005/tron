package com.pwc.utilities.tron.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pwc.utilities.tron.model.entity.Environment;
import com.pwc.utilities.tron.services.AdminService;



@RestController
@EnableAspectJAutoProxy
public class EnvironmentController {

	@Autowired
	private AdminService adminService;
	
	private Logger logger = Logger.getLogger(AppObjectController.class);

	@RequestMapping(value = "/environments", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('MANAGE_ENV')")
	public Environment createNewEnvironment(@RequestBody Environment environment) throws Exception {
		logger.info(">>>> Create Env");
		return adminService.addEnvironment(environment);
	}

	@RequestMapping(value = "/environments", method = RequestMethod.GET)
	public Iterable<Environment> getEnvironments() {
		return adminService.getAllEnvironemnts();
	}

	@RequestMapping(value = "/environment/delete", method = { RequestMethod.POST })
	@PreAuthorize("hasAuthority('MANAGE_ENV')")
	public void deleteEnvironment(@RequestBody Environment environment) {
		System.out.println("Deleteing>>" + environment);
		adminService.deleteEnvironment(environment);
	}
	
	@RequestMapping(value = "/environment/set", method = { RequestMethod.POST })
	public Environment setEnvironment(@RequestBody  Environment environment, HttpServletRequest request) {
		environment = adminService.getEnvironment(environment.getId());
		request.getSession().setAttribute("environment", environment);
		return environment;
	}
	
	
	@RequestMapping(value = "/environment/list-packages", method = { RequestMethod.POST })
	public List<Map<String, String>> listFileFolder(@RequestBody Map<String, String> payload, HttpServletRequest request) {
		
			
		Environment environment = (Environment) request.getSession().getAttribute("environment");
		String path = payload.get("path");
		return adminService.getAppliedPackageList(environment, path);
	}		
	
	@RequestMapping(value = "/environment/retrieve-source", method = { RequestMethod.POST })
	public Map<String,String> readFile(@RequestBody Map<String, String> payload) throws IOException{
		String path = payload.get("path");
		return adminService.readFile(path);
		
	}
	
	@RequestMapping(value = "/environment/compare-environment", method = { RequestMethod.POST })
	public List<Map<String, String>> compareEnvironment(@RequestBody Map<String, Environment> payload) {
		Environment sourceEnv = payload.get("sourceEnv");
		Environment targetEnv = payload.get("targetEnv");		
		return adminService.compareEnvs(sourceEnv, targetEnv);
	}
	
}
