package com.cjl.skill.controller;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class SkillController {
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;

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

	private String createOrder(Product p, Address address, User user) {
		Order record = new Order();
		record.setNote("购买时间：" + new Date());
		record.setPrice(p.getPrice());
		record.setProductId(p.getId());
		record.setQuantity(1);
		record.setUserId(user.getId());
		record.setSum(p.getPrice());
		try {
			orderService.createSkillOrder(record);
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
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
