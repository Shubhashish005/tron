package com.pwc.utilities.tron.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pwc.utilities.tron.model.entity.DBConfiguration;
import com.pwc.utilities.tron.model.repository.DBConfigurationRepository;


@RestController
@RequestMapping("/configurations")
public class ConfigurationController {

	//@Autowired
	private DBConfigurationRepository repo;
	
	//@Autowired
	//private AdminService patchService;
	
//	  @RequestMapping(method = RequestMethod.POST)
//	  public DBConfiguration addDBConfiguration(@RequestBody DBConfiguration config) {
//		config.setId(1);
//	    return repo.save(config);
//	  }
	  
	  @RequestMapping(method = RequestMethod.GET)
	  public DBConfiguration getDBConfiguration() {
		return repo.findOne(1);
	  }
	  
	 
}
