package com.cjl.skill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.cjl.skill.exception.OrderFailException;
import com.cjl.skill.mapper.OrderMapper;
import com.cjl.skill.mapper.ProductMapper;
import com.cjl.skill.pojo.Order;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private ProductMapper productMapper;

	@Override
	public int add(Order order) {
		return orderMapper.insertSelective(order);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Order createSkillOrder(Order order) {
		// 扣减库存时，同时做判断，需要修改sql语句
		productMapper.decreaseStock(order.getProductId());

		if ("1".equals("1"))
			throw new OrderFailException();

		// 成功就下单
		orderMapper.insertSelective(order);
		// throw new RuntimeException("扣减库存失败"); //模拟下单失败
		return order;
	}
}
