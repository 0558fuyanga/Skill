package com.cjl.skill.mq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

@Component
public class MQProducer {
	public void sendMessage(String group, String topic, String tag, byte[] message) throws Exception {

		DefaultMQProducer producer = new DefaultMQProducer(group);
		// Specify name server addresses.
		producer.setNamesrvAddr("localhost:9876");
		// Launch the instance.
		producer.start();

		producer.setRetryTimesWhenSendAsyncFailed(0);
		Message msg = new Message(topic, tag, message);

		producer.send(msg);

		producer.shutdown();
	}
}
