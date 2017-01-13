package io.smsc.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.smpp.core.SmppConstants;
import org.springframework.messaging.*;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.atomic.AtomicInteger;

@ContextConfiguration("classpath:TestSmppConnection-context.xml")
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSmppConnection {

    @Value("${smpp.systemId}")
    private String destination;

    @Autowired
    private SubscribableChannel inboundChannel;

    @Autowired
    private MessageChannel outboundChannel;

    @Autowired
    private SubscribableChannel receiptChannel;

    private Message<String> messageOut;

    private AtomicInteger count = new AtomicInteger();

    @Before
    public void setUp() {
        messageOut = MessageBuilder.withPayload("This is the message")
                .setHeader(SmppConstants.DST_ADDR, destination)
                .setHeader(SmppConstants.SRC_ADDR, destination)
                .build();
    }

    @Test
    public void testSmppConnection() throws Throwable {
        MessageHandler standardInboundHandler = new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println("Standard Inbound channel receive: " + message);
                count.incrementAndGet();
            }
        };
        MessageHandler receiptHandler = new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String received = message.getPayload().toString();
                System.out.println("Outbound channel output receive receipt: " + received);
            }
        };
        inboundChannel.subscribe(standardInboundHandler);
        receiptChannel.subscribe(receiptHandler);

        outboundChannel.send(messageOut);

        Thread.sleep(5000);
    }

}