package io.smsc.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

import java.io.IOException;

@SpringBootApplication
//@ImportResource("/META-INF/integration.xml")
public class SmppApplication {
	private static Logger log = LoggerFactory.getLogger(SmppApplication.class);

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext ctx = new SpringApplication(SmppApplication.class).run(args);
		//log.info("started");
		System.out.println("Hit Enter to terminate");
		System.in.read();
		ctx.close();


	}
}
