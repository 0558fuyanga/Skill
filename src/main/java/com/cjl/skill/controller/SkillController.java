package com.cjl.skill.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cjl.skill.pojo.Order;
import com.cjl.skill.pojo.Product;
import com.cjl.skill.service.SkillService;

@Controller
public class SkillController {
	@Autowired
	private SkillService skillService;
	
	@GetMapping("/skill")
	public String skillPage(@RequestParam(defaultValue = "1")int id,Model model) {
		Product p = skillService.getById(id);
		model.addAttribute("p", p);
		return "skill";
	}
	
	@PostMapping("/skill")
	public @ResponseBody String skill(int userId,int productId) {
		try {
			boolean skill = skillService.skill(productId, userId);
			if(skill) {
				return "ok";
			}else {
				return "not enough";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	@GetMapping("/admin/skill/init")
	public @ResponseBody String initSkill() {
		try {
			skillService.init();
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	@GetMapping("/admin/skill/report")
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
