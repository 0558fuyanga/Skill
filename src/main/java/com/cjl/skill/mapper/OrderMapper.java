package com.cjl.skill.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cjl.skill.pojo.Order;
@Mapper
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
    
    int deleteAll();
    
    List<Order> selectAll();

	int selectCountByProductId(int productId);

	int deleteByUserId(int userId);
}