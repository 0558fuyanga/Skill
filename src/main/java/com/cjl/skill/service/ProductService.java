package com.cjl.skill.service;

import com.cjl.skill.pojo.Product;

public interface ProductService {
	Product getById(int id);
	
	int decreaseStock(int id);
}
