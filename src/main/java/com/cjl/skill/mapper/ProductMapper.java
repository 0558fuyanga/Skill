package com.cjl.skill.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cjl.skill.pojo.Product;
@Mapper
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);
    
    int decreaseStock(Integer id);
    
    void deleteAll();

	int selectStockById(int id);

	List<Product> selectSkillGoods();
}