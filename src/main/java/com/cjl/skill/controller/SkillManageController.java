package com.cjl.skill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cjl.skill.service.SkillManageService;


@Controller
@RequestMapping("/admin/skill")
public class SkillManageController {
	@Autowired
	private SkillManageService skillService;
	
	@GetMapping("/init")
	public @ResponseBody String initSkill() {
		try {
			skillService.init();
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	//库存缓存预热
	@GetMapping("/load/stock")
	public @ResponseBody String loadStock() {
		try {
			skillService.loadStock();
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	@GetMapping("/report")
	public @ResponseBody Object reportSkill(int productId) {
		try {
			int orderCount = skillService.getOrderCountByProductId(productId);
			int stock = skillService.getStockByProductId(productId);
			Report report = new Report();
			report.setOrderCount(orderCount);
			report.setProductStock(stock);
			return report;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	class Report {
		private int orderCount;
		private int productStock;

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
