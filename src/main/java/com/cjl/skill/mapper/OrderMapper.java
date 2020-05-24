package com.cjl.skill.mapper;

import com.cjl.skill.pojo.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

	int deleteByUserId(int userId);

	//custom
	int deleteAll();

	int selectCountByProductId(int productId);
}