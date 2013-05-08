package com.source3g.hermes.merchant.api;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.dto.merchant.ElectricMenuDto;
import com.source3g.hermes.entity.merchant.ElectricMenu;
import com.source3g.hermes.merchant.service.ElectricMenuService;

@Controller
@RequestMapping("/merchant/electricMenu")
public class ElectricMenuApi {

	@Autowired
	private ElectricMenuService electricMenuService;

	@RequestMapping(value = "/list/{merchantId}")
	@ResponseBody
	public List<ElectricMenu> list(@PathVariable String merchantId) {
		return electricMenuService.findByMerchantId(new ObjectId(merchantId));
	}

	@RequestMapping(value = "/addItem", method = RequestMethod.POST)
	@ResponseBody
	public String addItem(@RequestParam("file") MultipartFile file, String menuId, String title, String unit) {
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/add/{merchantId}", method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody ElectricMenuDto electricMenuDto,@PathVariable ObjectId merchantId) {
		electricMenuService.addMenu(electricMenuDto.getMenus(),merchantId);
		return ReturnConstants.SUCCESS;
	}

}
