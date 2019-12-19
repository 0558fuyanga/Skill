package com.cjl.skill.service;

import com.cjl.skill.pojo.Product;

public interface SkillService {
	void init();
	
	boolean skill(int productId,int userId);

	Product getById(int id);
	
	int getOrderCountByProductId(int productId);
	
	int getStockByProductId(int productId);
}
