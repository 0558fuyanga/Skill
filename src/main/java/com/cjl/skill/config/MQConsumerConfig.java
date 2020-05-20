package com.cjl.skill.config;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.curator.framework.CuratorFramework;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.cjl.skill.pojo.Order;
import com.cjl.skill.pojo.Product;
import com.cjl.skill.service.OrderService;
import com.cjl.skill.util.ConstantUtil;
import com.cjl.skill.util.JsonUtil;
import com.cjl.skill.util.LocalCache;

@Configuration
public class MQConsumerConfig implements ApplicationListener<ContextRefreshedEvent>{
	@Autowired
	private OrderService orderService;
	
	@Resource(name = "stringRedisTemplate")
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private CuratorFramework client;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("skill_group_name");
         
        // Specify name server addresses.
        consumer.setNamesrvAddr("localhost:9876");
        
        // Subscribe one more more topics to consume.
        try {
			consumer.subscribe(ConstantUtil.MQ_ORDER_TOPIC, ConstantUtil.MQ_ORDER_TOPIC_TAG_1);
		} catch (MQClientException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        // Register callback to execute on arrival of messages fetched from brokers.
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                ConsumeConcurrentlyContext context) {
            	//下订单
            	for(MessageExt msg: msgs) {
            		byte[] body = msg.getBody();
            		Order order;
					try {
						order = JsonUtil.string2Obj(new String(body,"utf-8"), Order.class);
						Product p = new Product();
	        			p.setId(order.getProductId());
	                	try {
	    					if(orderService.createSkillOrder(order)==null) {
	            				renewStock(p);
	            			}
	            		} catch (Exception e) {
	            			e.printStackTrace();
	            			renewStock(p);
	            		}
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
					System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
            	}
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        try {
			consumer.start();
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 下单操作失败，返回库存
	 * @param p
	 * @return
	 */
	public String renewStock(Product p) {
		//下单失败，退库存
		stringRedisTemplate.opsForValue().increment(ConstantUtil.REDIS_KEY_STOCK_PREFIX+p.getId());
		//商品退单，撤销本地缓存标记
		LocalCache.soldOutProducts.remove(p.getId());
		//修改zknode节点数据状态为 false
		try {
			client.setData()
			.forPath(ConstantUtil.ZK_SOLD_OUT_PRODUCT_ROOT_PATH+p.getId(), "false".getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "秒杀下单失败";
	}
}
