package com.cjl.skill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cjl.skill.mapper.OrderMapper;
import com.cjl.skill.mapper.ProductMapper;
import com.cjl.skill.pojo.Product;

@Service
public class SkillManageServiceImpl implements SkillManageService {
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Override
	public void init() {
		productMapper.deleteAll();
		orderMapper.deleteAll();
		
		//初始数据
		Product p = new Product();
		p.setNote("初始化秒杀商品时间："+System.currentTimeMillis());
		p.setPrice(1.0);
		p.setProductName("小米mate2大屏手机、双12大促销！！");
		p.setStock(10);
		productMapper.insertSelective(p);
	}

	@Override
	public int getOrderCountByProductId(int productId) {
		return orderMapper.selectCountByProductId(productId);
	}

	@Override
	public int getStockByProductId(int productId) {
		return productMapper.selectStockById(productId);
	}

	
}
