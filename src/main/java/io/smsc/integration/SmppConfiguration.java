package io.smsc.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.smpp.session.ExtendedSmppSession;
import org.springframework.integration.smpp.session.ExtendedSmppSessionAdaptingDelegate;
import org.springframework.integration.smpp.session.SmppSessionFactoryBean;

import java.util.Objects;

@Configuration
@EnableIntegration
@PropertySource("classpath:smpp.properties")
public class SmppConfiguration {
    @Value("${smpp.host}")
    private String host;
    @Value("${smpp.port}")
    private Integer port;
    @Value("${smpp.systemId}")
    private String systemId;
    @Value("${smpp.password}")
    private String password;

    @Bean
    public ExtendedSmppSession smppSessionFactory() throws Exception {
        SmppSessionFactoryBean smppSessionFactoryBean = new SmppSessionFactoryBean();
        smppSessionFactoryBean.setSystemId(this.systemId);
        smppSessionFactoryBean.setPort(this.port);
        smppSessionFactoryBean.setPassword(this.password);
        smppSessionFactoryBean.setHost(this.host);
        smppSessionFactoryBean.afterPropertiesSet();
        ExtendedSmppSession extendedSmppSession = smppSessionFactoryBean.getObject();
        ExtendedSmppSessionAdaptingDelegate es = (ExtendedSmppSessionAdaptingDelegate) extendedSmppSession;
        return es;
    }

}
