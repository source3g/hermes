package com.source3g.hermes.merchant.api;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
import com.source3g.hermes.entity.merchant.ElectricMenuItem;
import com.source3g.hermes.merchant.service.ElectricMenuService;
import com.source3g.hermes.utils.Thumbnail;

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

	@RequestMapping(value = "/addItem/{menuId}", method = RequestMethod.POST)
	@ResponseBody
	public String addItem(@RequestParam("file") MultipartFile file,@PathVariable String menuId, String price, String title, String unit) throws IOException {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/");
		String suffxPath = dateFormat.format(date) + "/" + file.getOriginalFilename();
		String suffxDestPath = dateFormat.format(date) + "/" + file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")) + "_s" + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
		String path = electricMenuService.getPicPath();
		String filePath = path + suffxPath;
		File fileToCopy = new File(filePath);
		FileUtils.copyInputStreamToFile(file.getInputStream(), fileToCopy);
		Thumbnail thumbnail = new Thumbnail(filePath, path + suffxDestPath);
		thumbnail.resize(300, 200);
		ElectricMenuItem electricMenuItem = new ElectricMenuItem();
		electricMenuItem.setPicPath(filePath);
		electricMenuItem.setAbstractPicPath(path + suffxDestPath);
		electricMenuItem.setPrice(Double.parseDouble(price));
		electricMenuItem.setTitle(title);
		electricMenuItem.setUnit(unit);
		electricMenuService.addItem(electricMenuItem, new ObjectId(menuId));
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "findItem/{menuId}/{title}", method = RequestMethod.GET)
	@ResponseBody
	public ElectricMenuItem findItem(@PathVariable String menuId,@PathVariable  String title) {
		return electricMenuService.findItemByTitle(new ObjectId(menuId), title);
	}

	@RequestMapping(value = "deleteItem/{menuId}/{title}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteItem( @PathVariable  String menuId, @PathVariable  String title) {
		electricMenuService.deleteItem(title, new ObjectId(menuId));
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "update/{merchantId}", method = RequestMethod.POST)
	@ResponseBody
	public String update(@RequestBody ElectricMenu electricMenu) {
		electricMenuService.updateMenu(electricMenu);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "delete/{menuId}", method = RequestMethod.POST)
	@ResponseBody
	public String deleteMenu(String menuId) {
		electricMenuService.deleteMenu(new ObjectId(menuId));
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/add/{merchantId}", method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody ElectricMenuDto electricMenuDto, @PathVariable ObjectId merchantId) {
		electricMenuService.addMenu(electricMenuDto.getMenus(), merchantId);
		return ReturnConstants.SUCCESS;
	}

}
