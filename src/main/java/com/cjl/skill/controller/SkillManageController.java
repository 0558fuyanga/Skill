package com.cjl.skill.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cjl.skill.pojo.Activity;
import com.cjl.skill.pojo.ActivityProduct;
import com.cjl.skill.pojo.Product;
import com.cjl.skill.service.ActivityProductService;
import com.cjl.skill.service.ProductService;
import com.cjl.skill.service.SkillManageService;
import com.cjl.skill.util.AckMessage;

@Controller
@RequestMapping("/admin/skill")
public class SkillManageController {
	@Autowired
	private SkillManageService skillService;
	
	@Autowired
	private ActivityProductService activityProductService;
	
	@Autowired
	private ProductService productService;


	/**
	 * 初始化测试环境数据
	 * @return
	 */
	@GetMapping("/init")
	public @ResponseBody Object initSkill() {
		try {
			skillService.init();
			return AckMessage.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return AckMessage.error();
		}
	}

	/**
	 * 生成秒杀报告
	 * @param 
	 * @return
	 */
	@GetMapping("/report")
	public @ResponseBody Object reportSkill() {
		try {
			List<Report> reports = new ArrayList<SkillManageController.Report>();
			
			List<ActivityProduct> list = activityProductService.getByActId(1);
			for (ActivityProduct ap : list) {
				int orderCount = skillService.getOrderCountByProductId(ap.getProductId());
				Product p = productService.getById(ap.getProductId());
				Report report = new Report();
				report.setId(p.getId());
				report.setOrderCount(orderCount);
				report.setProductStock(p.getStock());
				report.setProductName(p.getProductName());
				reports.add(report);
			}
			return AckMessage.ok(reports);
		} catch (Exception e) {
			e.printStackTrace();
			return AckMessage.error();
		}
	}

	/**
	 * 维护活动
	 */
	@PostMapping("/activity")
	public @ResponseBody Object save(Activity activity, int[] pids) {
		System.out.println(pids);
		skillService.saveActivity(activity,pids);
		return AckMessage.ok();
	}
	
	/**
	 * 报告对象
	 * @author cjl
	 *
	 */
	class Report {
		private int id;
		private int orderCount;
		private int productStock;
		private String productName;
		
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public int getOrderCount() {
			return orderCount;
		}

		public void setOrderCount(int orderCount) {
			this.orderCount = orderCount;
		}

		public int getProductStock() {
			return productStock;
		}

		public void setProductStock(int productStock) {
			this.productStock = productStock;
		}

	}
}
