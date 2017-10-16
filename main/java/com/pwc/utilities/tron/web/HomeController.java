package com.pwc.utilities.tron.web;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {
	
 Logger logger = Logger.getLogger(HomeController.class);
	
 @RequestMapping("/")
 public String home() {
  return "index";
 }
 
 @RequestMapping("/login")
 public String login(){
	 return "login";
 }
 
}