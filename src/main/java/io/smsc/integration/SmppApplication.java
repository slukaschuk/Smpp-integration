package io.smsc.integration;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

import java.io.IOException;

@SpringBootApplication
@ImportResource("classpath:/META-INF/integration.xml")
@Log4j2
public class SmppApplication {


	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext ctx = new SpringApplication(SmppApplication.class).run(args);
		System.out.println("Hit Enter to terminate");
		System.in.read();
		ctx.close();


	}
}
