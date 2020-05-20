package com.cjl.skill.config;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQProducerConfig {
	
	@Bean
	public DefaultMQProducer defaultMQProducer() throws MQClientException {
		//Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("skill_group_name");
        // Specify name server addresses.
        producer.setNamesrvAddr("localhost:9876");
        //Launch the instance.
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(3);
        
        return producer;
	}
}
