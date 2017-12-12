package com.pwc.utilities.tron.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pwc.utilities.tron.model.entity.LocalUser;
import com.pwc.utilities.tron.services.AdminService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private AdminService adminService;
	
	//private Logger logger = Logger.getLogger(RoleController.class);

	@RequestMapping(method = RequestMethod.GET)
	public Iterable<LocalUser> getUsers() {
		return adminService.getAllLocalUser();
	}	
	
	@RequestMapping(method = RequestMethod.POST)
	public LocalUser createUser(@RequestBody LocalUser user) {
		return adminService.addLocalUser(user);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public boolean deleteUser(@RequestBody LocalUser user){
		adminService.deleteLocalUser(user);
		return true;
	}
}
