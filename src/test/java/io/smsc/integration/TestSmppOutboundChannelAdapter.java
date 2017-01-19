package io.smsc.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.integration.smpp.core.SmppConstants;
import org.springframework.integration.smpp.session.ExtendedSmppSession;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration("classpath:TestSmppOutboundChannelAdapter-context.xml")
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSmppOutboundChannelAdapter {

	private Log log = LogFactory.getLog(getClass());

	@Value("#{session}")
	private ExtendedSmppSession smppSession;

	@Value("#{outboundSms}")
	private MessageChannel messageChannel;

	private String smsMessageToSend = " SMS sent " + System.currentTimeMillis() + ")";

	@Before
	public void start (){

	}

	@Test
	public void testSendingAndReceivingASmppMessageUsingRawApi() throws Throwable {
		log.debug( "sending a message.");
		log.info( "sending a message.");
		log.warn( "sending a message.");
		Message<String> smsMsg = MessageBuilder.withPayload(this.smsMessageToSend)
				.setHeader(SmppConstants.SRC_ADDR, "0672221111")
				.setHeader(SmppConstants.DST_ADDR, "0664002211")
				.setHeader(SmppConstants.REGISTERED_DELIVERY_MODE, SMSCDeliveryReceipt.SUCCESS)
				.build();

		this.messageChannel.send(smsMsg);
	}
}
