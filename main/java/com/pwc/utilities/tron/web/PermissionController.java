package com.pwc.utilities.tron.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pwc.utilities.tron.model.entity.Permission;
import com.pwc.utilities.tron.services.AdminService;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

	@Autowired
	private AdminService adminService;
	
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Permission> getPermissions() {
		return adminService.getPermissions();
	}

}
