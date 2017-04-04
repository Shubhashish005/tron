package com.pwc.utilities.tron.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pwc.utilities.tron.model.entity.Environment;
import com.pwc.utilities.tron.model.entity.Patch;
import com.pwc.utilities.tron.services.AdminService;


@RestController
public class EnvironmentController {

	@Autowired
	private AdminService adminService;

	@RequestMapping(value = "/environments", method = RequestMethod.POST)
	public Environment createNewEnvironment(@RequestBody Environment environment) throws Exception {
		//System.out.println(environment.getEnvName());
		return adminService.addEnvironment(environment);
	}

	@RequestMapping(value = "/environments", method = RequestMethod.GET)
	public Iterable<Environment> getEnvironments() {
		return adminService.getAllEnvironemnts();
	}

	@RequestMapping(value = "/environment/delete", method = { RequestMethod.POST })
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

}
