package com.cjl.skill.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cjl.skill.pojo.Activity;
import com.cjl.skill.pojo.ActivityProduct;
import com.cjl.skill.pojo.Address;
import com.cjl.skill.pojo.Order;
import com.cjl.skill.pojo.Product;
import com.cjl.skill.pojo.User;
import com.cjl.skill.service.ActivityProductService;
import com.cjl.skill.service.ActivityService;
import com.cjl.skill.service.OrderService;
import com.cjl.skill.service.ProductService;
import com.cjl.skill.util.AckMessage;
import com.cjl.skill.util.RequestHelper;
import com.cjl.skill.vo.ProductActVo;

@Controller
public class SkillController {
	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private ActivityProductService activityProductService;

	/**
	 * 返回整个中控页面
	 * @param model
	 * @return
	 */
	@GetMapping("/skill")
	public String skillPage(Model model) {
		//获取所有商品
		List<Product> products = productService.getAll();
		
		//视图对象（里面多了一个属性）
		List<ProductActVo> proActs = new ArrayList<ProductActVo>();
		//初始化商品基本信息
		for (Product p : products) {
			ProductActVo pav = new ProductActVo();
			pav.setId(p.getId());
			//默认就是0
			pav.setActId(0);
			pav.setProductName(p.getProductName());
			pav.setCreateTime(p.getCreateTime());
			pav.setId(p.getId());
			pav.setPrice(p.getPrice());
			pav.setStatus(p.getStatus());
			pav.setStock(p.getStock());
			proActs.add(pav);
		}
		
		//获取参加该活动的商品，为便于测试，默认活动id是1
		List<ActivityProduct> aps =activityProductService.getByActId(1);
		//设置活动标识
		for (ActivityProduct activityProduct : aps) {
			for(ProductActVo v : proActs) {
				if(activityProduct.getProductId()==v.getId()) {
					v.setActId(activityProduct.getActivityId());
					break;
				}
			}
		}
		//添加vo对象到请求域，页面默认显示参加活动的商品进行业务测试
		model.addAttribute("proActs", proActs);
		
		//获取秒杀活动信息,默认活动是1
		Activity one = activityService.getOne(1);
		
		model.addAttribute("activity", one);
		
		return "skill";
	}

	/**
	 * 秒杀接口
	 * @param productId
	 * @param session
	 * @return
	 */
	@PostMapping("/skill")
	public @ResponseBody Object skill(int productId) {
		//后台校验秒杀时间
		if(!validSkillTime(productId)) {
			return AckMessage.error("time is error");
		}
		
		//验证参数
		if (productId <= 0) {
			return AckMessage.illegalArgs();
		}
		
		//验证是否登陆
		User user = getLoginUser();
		if (user == null) {
			return AckMessage.unauthorized();
		}
		
		//是否有默认收货地址
		Address address = getUserDefaultAddress(user);
		if (address == null) {
			return new AckMessage<>(601,"没有默认收货地址");
		}	
		
		//商品是否存在
		Product product = productService.getById(productId);
		if (product == null) {
			return new AckMessage<>(602,"商品不存在");
		}
		//检查库存
		if (product.getStock() < 1) {
			return new AckMessage<>(603,"商品已经抢完了");
		}

		//同步生成订单
		return createOrder(product,address,user);

	}

	//登录用户
	@PostMapping("/login")
	public @ResponseBody Object doLogin() {
		User user = new User(1,"cjl");
		HttpSession session = RequestHelper.getSession();
		session.setAttribute("user", user);
		return AckMessage.ok();
	}
	
	//刷新指定商品的库存 restful接口风格，可读性好，语义化
	@GetMapping("/refresh/{id}/stock")
	public @ResponseBody Object refreshStock(@PathVariable int id) {
		try {
			Product p = productService.getById(id);
			return AckMessage.ok( p.getStock());
		} catch (Exception e) {
			e.printStackTrace();
			return AckMessage.error();
		}
	}
	
	/**
	 * 下单操作
	 * @param p
	 * @param address
	 * @param user
	 * @return
	 */
	private Object createOrder(Product p, Address address, User user) {
		Order record = new Order();
		record.setNote("秒杀下单测试");
		record.setPrice(p.getPrice());
		record.setProductId(p.getId());
		record.setQuantity(1);
		record.setUserId(user.getId());
		record.setSum(p.getPrice());
		record.setStatus("待付款");
		try {
			orderService.createSkillOrder(record);
			return AckMessage.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return AckMessage.error();
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
		HttpSession session = RequestHelper.getSession();
		Object user = session.getAttribute("user");
		if(user!=null) {
			return (User)user;
		}else {
			return null;
		}
	}
	
	/**
	 * 校验秒杀时间
	 * @param productId
	 * @return
	 */
	private boolean validSkillTime(int productId) {
		Activity one = activityService.getOne(productId);
		long start = one.getStartTime().getTime();
    	long end = one.getEndTime().getTime();
    	long now = System.currentTimeMillis();
    	if(now < start || now > end) {
    		return false;
    	}else  {
    		return true;
    	}
    }

}
