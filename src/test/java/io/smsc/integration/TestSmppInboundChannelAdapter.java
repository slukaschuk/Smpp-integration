package io.smsc.integration;

import java.util.concurrent.atomic.AtomicInteger;

import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.integration.smpp.core.SmppConstants;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration("classpath:TestSmppInboundChannelAdapter-context.xml")
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSmppInboundChannelAdapter {

	private String smsMessageToSend = "test SMPP message being sent from this time:"+ System.currentTimeMillis()+".";

	@Value("${test.dst.number}")
	private String number;

	private AtomicInteger atomicInteger = new AtomicInteger();

	@Value("#{outbound}")
	private SubscribableChannel out;

	@Value("#{inbound}")
	private SubscribableChannel in;

	@Autowired
	private ApplicationContext context;

	@Before
	public void before () throws Throwable {
		Assert.assertNotNull(this.number);
	}

	private String lastReceivedSms = null ;

	@Test
	public void testReceiving() throws Throwable {


		in.subscribe(new MessageHandler() {
			public void handleMessage(Message<?> message) throws MessagingException {
				lastReceivedSms =(message.getPayload().toString());
				atomicInteger.incrementAndGet();
			}
		});

		// lets send something
		Message<String> smsMsg = MessageBuilder.withPayload(this.smsMessageToSend)
					.setHeader(SmppConstants.SRC_ADDR, this.number)
					.setHeader(SmppConstants.DST_ADDR, this.number)
					.setHeader(SmppConstants.REGISTERED_DELIVERY_MODE, SMSCDeliveryReceipt.SUCCESS)
					.build();
		out.send(smsMsg);

		Thread.sleep(1000 * 10);

		Assert.assertTrue(atomicInteger.intValue()>0);
		Assert.assertEquals(atomicInteger.intValue() ,1);
		Assert.assertEquals(this.smsMessageToSend, lastReceivedSms);
	}
}
