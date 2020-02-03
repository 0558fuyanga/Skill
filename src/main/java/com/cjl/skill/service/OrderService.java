package com.cjl.skill.service;

import com.cjl.skill.pojo.Order;

public interface OrderService {
	int add(Order order);
	
	int deleteByuserId(int userId);
	
	int deleteAll();
	
	//创建秒杀订单
	Order createSkillOrder(Order order);
}
