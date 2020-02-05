package com.cjl.skill.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cjl.skill.pojo.Address;
import com.cjl.skill.pojo.Order;
import com.cjl.skill.pojo.Product;
import com.cjl.skill.pojo.User;
import com.cjl.skill.service.OrderService;
import com.cjl.skill.service.ProductService;
import com.cjl.skill.util.ConstantUtil;

@Controller
public class SkillController {
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;
	
	@Resource(name = "stringRedisTemplate")
	private StringRedisTemplate stringRedisTemplate;

	@GetMapping("/skill")
	public String skillPage(@RequestParam(defaultValue = "1") int id, Model model) {
		Product p = productService.getById(id);
		model.addAttribute("p", p);
		return "skill";
	}

	@PostMapping("/skill")
	public @ResponseBody String skill(int productId) {
		//后台校验秒杀时间，省略。。。。。
		
		//验证参数
		if (productId <= 0) {
			return "参数不合法";
		}
		// 验证是否登陆
		User user = getLoginUser();
		if (user == null) {
			return "没有登陆";
		}
		// 是否有默认收货地址
		Address address = getUserDefaultAddress(user);
		if (address == null) {
			return "没有默认收货地址";
		}

		//拦截1：缓存预减 (原子减法，没有并发问题)，但是有其他问题哦，退单时还原库存后，库存还是负数，会失效，例如减少到-100，怎么还原呢？？
		Long stock = stringRedisTemplate.opsForValue().decrement(ConstantUtil.REDIS_KEY_STOCK_PREFIX+productId);
		if(stock<0) {
			//库存减少到负数时，还原库存，一次加1就行，维持整体公平原则就行，不需要考虑并发性还原问题
			stringRedisTemplate.opsForValue().increment(ConstantUtil.REDIS_KEY_STOCK_PREFIX+productId);
			return "商品已售完";
		}
		
		
		Product product = productService.getById(productId);
		if (product == null) {
			return "商品不存在";
		}

		if (product.getStock() < 1) {
			return "商品已经抢完了";
		}

		// 生成订单
		return createOrder(product,address,user);

	}

	/**
	 * 下单操作
	 * @param p
	 * @param address
	 * @param user
	 * @return
	 */
	private String createOrder(Product p, Address address, User user) {
		Order record = new Order();
		record.setNote("购买时间：" + new Date());
		record.setPrice(p.getPrice());
		record.setProductId(p.getId());
		record.setQuantity(1);
		record.setUserId(user.getId());
		record.setSum(p.getPrice());
		try {
			if(orderService.createSkillOrder(record)!=null)
				return "ok";
			else {
				//下单失败，退库存
				stringRedisTemplate.opsForValue().increment(ConstantUtil.REDIS_KEY_STOCK_PREFIX+p.getId());
				return "秒杀下单失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			//下单失败，退库存
			stringRedisTemplate.opsForValue().increment(ConstantUtil.REDIS_KEY_STOCK_PREFIX+p.getId());
			return "秒杀下单失败";
		}
	}

	/**
	 * 获取默认收货地址
	 * 
	 * @param user
	 * @return
	 */
	private Address getUserDefaultAddress(User user) {
		return new Address(1, "中国", user.getId(), true);
	}

	/**
	 * 获取登陆用户
	 * 
	 * @return
	 */
	private User getLoginUser() {
		return new User(1, "killyou");
	}

}
