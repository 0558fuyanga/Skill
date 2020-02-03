package com.cjl.skill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

	@Override
	public int deleteByuserId(int userId) {
		return orderMapper.deleteByUserId(userId);
	}

	@Override
	public int deleteAll() {
		return orderMapper.deleteAll();
	}

	@Transactional
	@Override
	public Order createSkillOrder(Order order) {
		orderMapper.insertSelective(order);
		//扣减库存
		if(productMapper.decreaseStock(order.getProductId())<1) {
			throw new RuntimeException("扣减库存失败");
		}
		return order;
	}

}
