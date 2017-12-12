package com.pwc.utilities.tron.web;



import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pwc.utilities.tron.model.entity.Role;
import com.pwc.utilities.tron.services.AdminService;

@RestController
@RequestMapping("/roles")
public class RoleController {

	@Autowired
	private AdminService adminService;
	
	private Logger logger = Logger.getLogger(RoleController.class);

	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Role> getRoles() {
		return adminService.getRoles();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public Role createRole(@RequestBody Role role) {
		logger.info("> > > Role Name: "+role.getName());
		return adminService.createRole(role);
	}
}

