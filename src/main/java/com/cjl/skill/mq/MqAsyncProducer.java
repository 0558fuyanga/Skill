package com.cjl.skill.mq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqAsyncProducer {
	@Autowired
	private DefaultMQProducer producer;
	
	public void send(String topic,String tags,String keys,String body) {
        try {
        	//Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(topic,
                tags,
                keys,
                body.getBytes(RemotingHelper.DEFAULT_CHARSET));
			producer.send(msg, new SendCallback() {
			    @Override
			    public void onSuccess(SendResult sendResult) {
			        System.out.printf("OK %s %n",sendResult.getMsgId());
			    }
			    @Override
			    public void onException(Throwable e) {
			        e.printStackTrace();
			    }
			});
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
