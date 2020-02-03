package com.cjl.skill.service;

public interface SkillManageService {
	void init();
	
	int getOrderCountByProductId(int productId);
	
	int getStockByProductId(int productId);
}
