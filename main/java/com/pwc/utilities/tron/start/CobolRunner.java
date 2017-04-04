package com.pwc.utilities.tron.start;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(20)
public class CobolRunner implements CommandLineRunner {

	private Logger logger = Logger.getLogger(CobolRunner.class);
	
	@Override
	public void run(String... arg0) throws Exception {

		logger.info("-------------------------------");
		logger.info(">>>     SERVER STARTED      >>>");
		logger.info("-------------------------------");
		
		/*
		ProcessBuilder pb = new ProcessBuilder("c:\\Spring\\cmdtest.cmd");
		pb.directory(new File("c:\\Spring\\"));
		pb.redirectOutput(new File("c:\\Spring\\out.log"));
		pb.start();
		*/
		
		//logger.info(pb.toString());

	}

}
