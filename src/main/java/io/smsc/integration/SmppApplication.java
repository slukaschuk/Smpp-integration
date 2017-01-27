package io.smsc.integration;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.smpp.inbound.SmppInboundChannelAdapter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;

import java.io.IOException;

@SpringBootApplication
//@ImportResource({"classpath:/META-INF/integration.xml", "classpath:smppConnection-context.xml"})
@ImportResource({"classpath:smppConnection-context.xml"})
@Import({SmppConfiguration.class})
@Log4j2
public class SmppApplication {

	@Autowired
	SmppConfiguration smppConfiguration;

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext ctx = new SpringApplication(SmppApplication.class).run(args);
		SubscribableChannel in = ctx.getBean("inputChannel",SubscribableChannel.class);
		in.send(new GenericMessage<String>("Hello, how are you?"));
		System.out.println("Hit Enter to terminate");
		System.in.read();
		ctx.close();
	}

	@Bean
	public SubscribableChannel inputChannel() {
		return MessageChannels.publishSubscribe().interceptor(new WireTap(logInputChannel())).get();
	}

	@Bean
	public MessageChannel logInputChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "logInputChannel")
	public LoggingHandler logging() {
		LoggingHandler adapter = new LoggingHandler(LoggingHandler.Level.INFO);
		adapter.setLoggerName("io.smsc.integration");
		adapter.setLogExpressionString("'Incomming message: ' + payload.toUpperCase()");
		return adapter;
	}

	@Bean
	public SmppInboundChannelAdapter smppInboundChannelAdapter() throws Exception {
		SmppInboundChannelAdapter smppInboundChannelAdapter = new SmppInboundChannelAdapter();
		smppInboundChannelAdapter.setSmppSession(smppConfiguration.smppSessionFactory());
		smppInboundChannelAdapter.setChannel(inputChannel());
		return smppInboundChannelAdapter;
	}
}
