package com.source3g.hermes.merchant.api;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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
		List<ElectricMenu> list = electricMenuService.findByMerchantId(new ObjectId(merchantId));
		return processItemPicPath(list);
	}

	@RequestMapping(value = "/updateItemNoPic/{menuId}/{itemId}", method = RequestMethod.POST)
	@ResponseBody
	public String updateItemNoPic(@PathVariable String itemId, @PathVariable String menuId, String price, String title, String unit) throws IOException {
		ElectricMenuItem electricMenuItem = new ElectricMenuItem();
		electricMenuItem.setPrice(Double.parseDouble(price));
		electricMenuItem.setTitle(title);
		electricMenuItem.setUnit(unit);
		electricMenuItem.setId(new ObjectId(itemId));
		electricMenuService.updateItem(electricMenuItem, new ObjectId(menuId));
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/updateItem/{menuId}/{itemId}", method = RequestMethod.POST)
	@ResponseBody
	public String updateItem(@PathVariable String itemId, @RequestParam("file") MultipartFile file, @PathVariable String menuId, String price, String title, String unit) throws IOException {
		// title = FormateUtils.changeEncode(title, "iso-8859-1", "UTF-8");
		// unit = FormateUtils.changeEncode(unit, "iso-8859-1", "UTF-8");
		title = new String(title.getBytes("iso-8859-1"));
		unit = new String(unit.getBytes("iso-8859-1"));
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/");
		String suffxPath = dateFormat.format(date) + file.getOriginalFilename();
		String suffxDestPath = dateFormat.format(date) + file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")) + "_s" + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
		String path = electricMenuService.getPicPath();
		String filePath = path + suffxPath;
		File fileToCopy = new File(filePath);
		FileUtils.copyInputStreamToFile(file.getInputStream(), fileToCopy);
		if (fileToCopy.length() > 100 * 1024) {
			Thumbnail.compressPic(filePath, filePath);
			Thumbnail thumbnail = new Thumbnail(filePath, filePath);
			thumbnail.resize(800, 600);
		}
		Thumbnail thumbnail = new Thumbnail(filePath, path + suffxDestPath);
		thumbnail.resize(400, 300);
		ElectricMenuItem electricMenuItem = new ElectricMenuItem();
		electricMenuItem.setPicPath(suffxPath);
		electricMenuItem.setAbstractPicPath(suffxDestPath);
		electricMenuItem.setPrice(Double.parseDouble(price));
		electricMenuItem.setTitle(title);
		electricMenuItem.setUnit(unit);
		electricMenuItem.setId(new ObjectId(itemId));
		electricMenuService.updateItem(electricMenuItem, new ObjectId(menuId));
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/addItem/{menuId}", method = RequestMethod.POST)
	@ResponseBody
	public String addItem(@RequestParam("file") MultipartFile file, @PathVariable String menuId, String price, String title, String unit) throws IOException {
		// title = FormateUtils.changeEncode(title, "iso-8859-1", "UTF-8");
		// unit = FormateUtils.changeEncode(unit, "iso-8859-1", "UTF-8");
		title = new String(title.getBytes("iso-8859-1"));
		unit = new String(unit.getBytes("iso-8859-1"));
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/");
		String suffxPath = dateFormat.format(date) + file.getOriginalFilename();
		String suffxDestPath = dateFormat.format(date) + file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")) + "_s" + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
		String path = electricMenuService.getPicPath();
		String filePath = path + suffxPath;
		File fileToCopy = new File(filePath);
		FileUtils.copyInputStreamToFile(file.getInputStream(), fileToCopy);
		if (fileToCopy.length() > 100 * 1024) {
			Thumbnail.compressPic(filePath, filePath);
			Thumbnail thumbnail = new Thumbnail(filePath, filePath);
			thumbnail.resize(800, 600);
		}
		Thumbnail thumbnail = new Thumbnail(filePath, path + suffxDestPath);
		thumbnail.resize(400, 300);
		ElectricMenuItem electricMenuItem = new ElectricMenuItem();
		electricMenuItem.setPicPath(suffxPath);
		electricMenuItem.setAbstractPicPath(suffxDestPath);
		electricMenuItem.setPrice(Double.parseDouble(price));
		electricMenuItem.setTitle(title);
		electricMenuItem.setUnit(unit);
		electricMenuService.addItem(electricMenuItem, new ObjectId(menuId));
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "findItem/{menuId}/{title}", method = RequestMethod.GET)
	@ResponseBody
	public ElectricMenuItem findItem(@PathVariable String menuId, @PathVariable String title) {
		ElectricMenuItem item = electricMenuService.findItemByTitle(new ObjectId(menuId), title);
		return processItemPicPath(item);
	}

	private List<ElectricMenu> processItemPicPath(List<ElectricMenu> electricMenuList) {
		if (CollectionUtils.isNotEmpty(electricMenuList)) {
			for (ElectricMenu electricMenu : electricMenuList) {
				processItemPicPath(electricMenu);
			}
		}
		return electricMenuList;
	}

	private ElectricMenu processItemPicPath(ElectricMenu electricMenu) {
		if (CollectionUtils.isNotEmpty(electricMenu.getItems())) {
			for (ElectricMenuItem electricMenuItem : electricMenu.getItems()) {
				processItemPicPath(electricMenuItem);
			}
		}
		return electricMenu;
	}

	private ElectricMenuItem processItemPicPath(ElectricMenuItem item) {
		item.setPicPath(electricMenuService.getLocalUrl() + "menu/images/" + item.getPicPath() + "/");
		item.setAbstractPicPath(electricMenuService.getLocalUrl() + "menu/images/" + item.getPicPath() + "/");
		return item;
	}

	@RequestMapping(value = "deleteItem/{menuId}/{itemId}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteItem(@PathVariable String menuId, @PathVariable String itemId) {
		electricMenuService.deleteItem(new ObjectId(itemId), new ObjectId(menuId));
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "update/{merchantId}", method = RequestMethod.POST)
	@ResponseBody
	public String update(@RequestBody ElectricMenu electricMenu,@PathVariable ObjectId merchantId) {
		electricMenuService.updateMenu(electricMenu,merchantId);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "delete/{menuId}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteMenu(@PathVariable ObjectId menuId) {
		electricMenuService.deleteMenu(menuId);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/add/{merchantId}", method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody ElectricMenuDto electricMenuDto, @PathVariable ObjectId merchantId) {
		return 	electricMenuService.addMenu(electricMenuDto.getMenus(), merchantId);
	}
	
	@RequestMapping(value = "/sync/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public String sync(@PathVariable ObjectId merchantId) throws Exception {
		electricMenuService.sync(merchantId);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/titleValidate/{menuId}/{title}", method = RequestMethod.GET)
	@ResponseBody
	public Boolean titleValidate(@PathVariable  ObjectId menuId, @PathVariable String  title) {
		return electricMenuService.titleValidate(menuId,title);
	}
	
}
