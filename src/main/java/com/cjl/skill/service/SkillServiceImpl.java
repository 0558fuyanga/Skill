package com.cjl.skill.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.cjl.skill.mapper.OrderMapper;
import com.cjl.skill.mapper.ProductMapper;
import com.cjl.skill.pojo.Order;
import com.cjl.skill.pojo.Product;

@Service
public class SkillServiceImpl implements SkillService {
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	@Override
	public boolean skill(int productId, int userId) {
		Product product = productMapper.selectByPrimaryKey(productId);
		if(product.getStock()<1) {
			return false;
		}else {
			productMapper.decreaseStock(productId);
			Order record = new Order();
			record.setNote("购买时间："+new Date());
			record.setPrice(product.getPrice());
			record.setProductId(productId);
			record.setQuantity(1);
			record.setUserId(userId);
			record.setSum(product.getPrice());
			orderMapper.insertSelective(record );
			return true;
		}
	}

	@Override
	public Product getById(int id) {
		return productMapper.selectByPrimaryKey(id);
	}
	
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
