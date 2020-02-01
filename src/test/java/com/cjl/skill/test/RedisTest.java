package com.cjl.skill.test;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cjl.skill.pojo.Product;
import com.cjl.skill.util.JsonUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisTest {
	@Resource(name = "redisTemplateJDK")
	private RedisTemplate<String, List<Product>> redisTemplatejdk;
	@Test
	public void testjdk() {
		List<Product> pts = new ArrayList<Product>();
		Product pro = new Product();
		pro.setId(10);
		pro.setNote("note");
		pro.setPrice(132.00);
		pro.setProductName("xxxxx");
		pro.setStock(130);
		pro.setVersion(0);
		pts.add(pro);
		redisTemplatejdk.opsForValue().set("jdk", pts);
		
		List<Product> list = redisTemplatejdk.opsForValue().get("jdk");
		Integer id = list.get(0).getId();
		System.out.println(list);
	}

	@Resource(name = "redisTemplateJackson")
	private RedisTemplate<String, List<Product>> redisTemplateJackson;
	@Test
	public void testJackson() {
		List<Product> pts = new ArrayList<Product>();
		Product pro = new Product();
		pro.setId(10);
		pro.setNote("note");
		pro.setPrice(132.00);
		pro.setProductName("xxxxx");
		pro.setStock(130);
		pro.setVersion(0);
		pts.add(pro);
		redisTemplateJackson.opsForValue().set("jackson", pts);
		
		List<Product> list = redisTemplateJackson.opsForValue().get("jackson");
		Integer id = list.get(0).getId();
		System.out.println(list);
	}

	
	@Resource(name = "stringRedisTemplate")
	private StringRedisTemplate stringRedisTemplate;
	@Test
	public void testString() {
		List<Product> pts = new ArrayList<Product>();
		Product pro = new Product();
		pro.setId(10);
		pro.setNote("note");
		pro.setPrice(132.00);
		pro.setProductName("xxxxx");
		pro.setStock(130);
		pro.setVersion(0);
		pts.add(pro);
		stringRedisTemplate.opsForValue().set("string", JsonUtil.obj2String(pts));
		
		String json = stringRedisTemplate.opsForValue().get("string");
		pts = JsonUtil.string2Collection(json, List.class, Product.class);
		for(Product p:pts) {
			System.out.println(p.getProductName());
		}
	}
}
