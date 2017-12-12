package com.pwc.utilities.tron.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pwc.utilities.tron.model.entity.GeneralSetting;
import com.pwc.utilities.tron.services.AdminService;

@RestController
@RequestMapping("/general-settings")
public class GeneralSettingController {
	
	Logger logger = Logger.getLogger(GeneralSettingController.class);
	
	@Autowired  
	AdminService adminService;
	
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<GeneralSetting> getSettings() {
		  return adminService.getSettings();
	}	
	
	@RequestMapping(method = RequestMethod.POST)
	public GeneralSetting updateSetting(@RequestBody GeneralSetting setting){
		logger.info(setting);
		logger.info(setting.getName());
		return adminService.updateSetting(setting);	
	}
	
}
