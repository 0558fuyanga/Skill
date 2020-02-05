package com.cjl.skill.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.cjl.skill.mapper.OrderMapper;
import com.cjl.skill.mapper.ProductMapper;
import com.cjl.skill.pojo.Product;
import com.cjl.skill.util.ConstantUtil;

@Service
public class SkillManageServiceImpl implements SkillManageService {
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Resource(name = "stringRedisTemplate")
	private StringRedisTemplate stringRedisTemplate;
	
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

	/**
	 * load kill goods stock
	 */
	@Override
	public void loadStock() {
		//业务流程，去活动表中查找商品，得到ids,再查找所有商品的库存
		List<Product> list = productMapper.selectSkillGoods();
		for (Product product : list) {
			stringRedisTemplate.opsForValue().set(ConstantUtil.REDIS_KEY_STOCK_PREFIX+product.getId(), product.getStock().toString());
		}
	}

	
}
