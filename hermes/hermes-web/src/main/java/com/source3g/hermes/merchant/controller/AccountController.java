package com.source3g.hermes.merchant.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.dto.customer.CustomerRemindDto;
import com.source3g.hermes.dto.merchant.ElectricMenuDto;
import com.source3g.hermes.entity.device.Device;
import com.source3g.hermes.entity.merchant.ElectricMenu;
import com.source3g.hermes.entity.merchant.ElectricMenuItem;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.merchant.MerchantRemindTemplate;
import com.source3g.hermes.entity.merchant.MerchantResource;
import com.source3g.hermes.entity.merchant.Setting;
import com.source3g.hermes.utils.ConfigParams;
import com.source3g.hermes.utils.LoginUtils;
import com.sourse3g.hermes.branch.Saler;

@Controller
@RequestMapping(value = "merchant/account")
public class AccountController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/toSwitch", method = RequestMethod.GET)
	public ModelAndView toSwitch(HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("merchant", merchant);
		return new ModelAndView("merchant/accountCenter/switch", model);
	}

	@RequestMapping(value = "/switch", method = RequestMethod.POST)
	public ModelAndView Switch(Setting setting, HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "merchant/switch/" + merchant.getId() + "/";
		HttpEntity<Setting> entity = new HttpEntity<Setting>(setting);
		String result = restTemplate.postForObject(uri, entity, String.class);
		Map<String, Object> model = new HashMap<String, Object>();
		merchant.setSetting(setting);
		model.put("merchant", merchant);
		if (ReturnConstants.SUCCESS.equals(result)) {
			model.put(ReturnConstants.SUCCESS, ReturnConstants.SUCCESS);
			return new ModelAndView("merchant/accountCenter/switch", model);
		}
		return new ModelAndView("merchant/error");

	}

	@RequestMapping(value = "/remindTemplate/get", method = RequestMethod.GET)
	@ResponseBody
	public MerchantRemindTemplate[] getMerchantRemindTemplates() throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant();
		String uri = ConfigParams.getBaseUrl() + "merchant/merchantRemindList/" + merchant.getId() + "/";
		MerchantRemindTemplate[] merchantRemindTemplates = restTemplate.getForObject(uri, MerchantRemindTemplate[].class);
		return merchantRemindTemplates;
	}

	@RequestMapping(value = "/remindTemplate/{remindTemplateId}/delete", method = RequestMethod.GET)
	public String deleteRemindTemplate(@PathVariable String remindTemplateId) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant();
		String uri = ConfigParams.getBaseUrl() + "merchant/" + merchant.getId() + "/remindTemplate/delete/" + remindTemplateId + "/";
		/*String result = */restTemplate.getForObject(uri, String.class);
		// if (ReturnConstants.SUCCESS.equals(result)) {
		//
		// }
		return "redirect:/merchant/account/remindSetting";
	}

	@RequestMapping(value = "/remindTemplate/add", method = RequestMethod.POST)
	public String addRemindTemplate(MerchantRemindTemplate merchantRemindTemplate, RedirectAttributes redirectAttributes) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant();
		String uri = ConfigParams.getBaseUrl() + "merchant/" + merchant.getId() + "/remindTemplate/add/";
		HttpEntity<MerchantRemindTemplate> entity = new HttpEntity<MerchantRemindTemplate>(merchantRemindTemplate);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			redirectAttributes.addFlashAttribute("success", "增加成功");
		} else {
			redirectAttributes.addFlashAttribute("error", result);
		}
		return "redirect:/merchant/account/remindSetting";
	}

	@RequestMapping(value = "/remindSetting", method = RequestMethod.GET)
	public ModelAndView toRemindSetting(HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "merchant/merchantRemindList/" + merchant.getId() + "/";
		MerchantRemindTemplate[] merchantRemindTemplates = restTemplate.getForObject(uri, MerchantRemindTemplate[].class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("merchantRemindTemplates", merchantRemindTemplates);
		return new ModelAndView("merchant/accountCenter/remindSetting", model);
	}

	@RequestMapping(value = "/remindSetting/json", method = RequestMethod.GET)
	@ResponseBody
	public MerchantRemindTemplate[] getRemindSettingJson(HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "merchant/merchantRemindList/" + merchant.getId() + "/";
		MerchantRemindTemplate[] merchantRemindTemplates = restTemplate.getForObject(uri, MerchantRemindTemplate[].class);

		return merchantRemindTemplates;
	}

	@RequestMapping(value = "/remindTemplate/save", method = RequestMethod.POST)
	public String remindSave(MerchantRemindTemplate merchantRemindTemplate, RedirectAttributes redirectAttributes, HttpServletRequest req)
			throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "merchant/" + merchant.getId() + "/remindTemplate/save/";
		HttpEntity<MerchantRemindTemplate> entity = new HttpEntity<MerchantRemindTemplate>(merchantRemindTemplate);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			redirectAttributes.addFlashAttribute("success", "保存成功");
		} else {
			redirectAttributes.addFlashAttribute("error", result);
		}
		return "redirect:/merchant/account/remindSetting";
	}

	@RequestMapping(value = "/toPasswordChange", method = RequestMethod.GET)
	public ModelAndView toPasswordChange() throws Exception {
		return new ModelAndView("merchant/accountCenter/passwordChange");

	}

	@RequestMapping(value = "/passwordChange/{password}/{newPassword}", method = RequestMethod.GET)
	public ModelAndView passwordChange(@PathVariable String password, @PathVariable String newPassword, HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "merchant/passwordChange/" + password + "/" + newPassword + "/" + merchant.getId() + "/";
		String result = restTemplate.getForObject(uri, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			Subject currentUser = SecurityUtils.getSubject();
			currentUser.logout();
			return new ModelAndView("merchant/login");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("error", result);
		return new ModelAndView("merchant/accountCenter/passwordChange", model);
	}

	// 验证密码
	@RequestMapping(value = "passwordValidate", method = RequestMethod.GET)
	@ResponseBody
	public Boolean passwordValidate(String password, HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "merchant/passwordValidate/" + password + "/" + merchant.getId() + "/";
		Boolean result = restTemplate.getForObject(uri, Boolean.class);
		return result;
	}

	@RequestMapping(value = "remind/toList", method = RequestMethod.GET)
	public String toRemindList(Model model) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant();
		String uri = ConfigParams.getBaseUrl() + "customer/todayReminds/" + merchant.getId() + "/";
		CustomerRemindDto[] result = restTemplate.getForObject(uri, CustomerRemindDto[].class);
		model.addAttribute("reminds", result);
		return "merchant/accountCenter/remindList";
	}

	@RequestMapping(value = "remind/list", method = RequestMethod.GET)
	@ResponseBody
	public CustomerRemindDto[] remindList() throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant();
		String uri = ConfigParams.getBaseUrl() + "customer/todayReminds/" + merchant.getId() + "/";
		CustomerRemindDto[] result = restTemplate.getForObject(uri, CustomerRemindDto[].class);
		return result;
	}

	@RequestMapping(value = "sendMessages/{title}", method = RequestMethod.GET)
	public String sendMessages(@PathVariable String title, HttpServletRequest req, Model model, RedirectAttributes redirectAttributes)
			throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "shortMessage/remindSend/" + title + "/" + merchant.getId() + "/";
		String result = restTemplate.getForObject(uri, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			redirectAttributes.addAttribute("success", result);
		} else {
			redirectAttributes.addAttribute("error", result);
		}
		return "redirect:/merchant/account/remind/toList/";
	}

	@RequestMapping(value = "ignoreSendMessages/{title}", method = RequestMethod.GET)
	public ModelAndView ignoreSendMessages(@PathVariable String title, HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "shortMessage/ignoreSendMessages/" + title + "/" + merchant.getId() + "/";
		@SuppressWarnings("unused")
		String result = restTemplate.getForObject(uri, String.class);
		return new ModelAndView("merchant/accountCenter/remindList");
	}

	@RequestMapping(value = "toResourceSetting", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toResourceSetting(HttpServletRequest req) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant(req);
		String uri = ConfigParams.getBaseUrl() + "merchant/merchantResource/" + merchant.getId() + "/";
		MerchantResource result = restTemplate.getForObject(uri, MerchantResource.class);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("merchantResource", result);
		return new ModelAndView("/merchant/accountCenter/resourceSetting", model);
	}

	@RequestMapping(value = "/addMerchantResource", method = RequestMethod.GET)
	public ModelAndView addMerchantResource(String name, RedirectAttributes redirectAttributes) throws Exception {
		if (StringUtils.isEmpty(name)) {
			return new ModelAndView("/merchant/accountCenter/resourceSetting/");
		}
		Merchant merchant = LoginUtils.getLoginMerchant();
		String uri = ConfigParams.getBaseUrl() + "merchant/addMerchantResource/" + merchant.getId() + "/" + name + "/";
		String result = restTemplate.getForObject(uri, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return new ModelAndView("redirect:/merchant/account/toResourceSetting/");
		} else {
			redirectAttributes.addFlashAttribute("error", result);
			return new ModelAndView("redirect:/merchant/account/toResourceSetting/");
		}
	}

	@RequestMapping(value = "/deletemerchantResource/{name}", method = RequestMethod.GET)
	public ModelAndView deletemerchantResource(@PathVariable String name) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant();
		String uri = ConfigParams.getBaseUrl() + "merchant/deletemerchantResource/" + merchant.getId() + "/" + name + "/";
		String result = restTemplate.getForObject(uri, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return new ModelAndView("redirect:/merchant/account/toResourceSetting/");
		}
		return new ModelAndView("/merchant/accountCenter/resourceSetting/");
	}

	@RequestMapping(value = "/updateMerchantResource", method = RequestMethod.POST)
	@ResponseBody
	public String updateMerchantResource(String messageContent) throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant();
		String uri = ConfigParams.getBaseUrl() + "merchant/updateMerchantResource/" + merchant.getId() + "/";
		HttpEntity<String> entity = new HttpEntity<String>(messageContent);
		String result = restTemplate.postForObject(uri, entity, String.class);
		if (ReturnConstants.SUCCESS.equals(result)) {
			return result;
		}
		return null;
	}

	@RequestMapping(value = "/info")
	public ModelAndView info() throws Exception {
		Merchant merchant = LoginUtils.getLoginMerchant();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("merchant", merchant);
		String ids = "";
		for (ObjectId id : merchant.getDeviceIds()) {
			ids += id;
			ids += ",";
		}
		ids = ids.substring(0, ids.length() - 1);
		String uri = ConfigParams.getBaseUrl() + "/device/" + ids + "/";
		Device[] devices = restTemplate.getForObject(uri, Device[].class);
		model.put("devices", devices);
		if (merchant.getSalerId() != null) {
			String url = ConfigParams.getBaseUrl() + "branchAndSalers/findSalerById/" + merchant.getSalerId();
			Saler saler = restTemplate.getForObject(url, Saler.class);
			model.put("saler", saler);
		}
		return new ModelAndView("/merchant/accountCenter/info", model);
	}

	@RequestMapping(value = "/electricMenu")
	public String electricMenu(Model model) throws Exception {
		String uri = ConfigParams.getBaseUrl() + "merchant/electricMenu/list/" + LoginUtils.getLoginMerchant().getId() + "/";
		ElectricMenu[] electricMenus = restTemplate.getForObject(uri, ElectricMenu[].class);
		model.addAttribute("menus", electricMenus);
		return "/merchant/accountCenter/electricMenu";
	}

	@RequestMapping(value = "/electricMenu/add", method = RequestMethod.POST)
	@ResponseBody
	public String electricMenuSubmit(ElectricMenuDto electricMenuDto, Model model) throws Exception {
		if (electricMenuDto.getMenus() == null) {
			return "类别名称不能为空";
		}
		String uri = ConfigParams.getBaseUrl() + "merchant/electricMenu/add/" + LoginUtils.getLoginMerchant().getId() + "/";
		HttpEntity<ElectricMenuDto> entity = new HttpEntity<ElectricMenuDto>(electricMenuDto);
		String str = restTemplate.postForObject(uri, entity, String.class);
		return str;
	}

	@RequestMapping(value = "/electricMenu/update", method = RequestMethod.POST)
	public String updateElectricMenu(ElectricMenu electricMenu, Model model) throws Exception {
		String uri = ConfigParams.getBaseUrl() + "merchant/electricMenu/update/" + LoginUtils.getLoginMerchant().getId() + "/";
		HttpEntity<ElectricMenu> entity = new HttpEntity<ElectricMenu>(electricMenu);
		restTemplate.postForObject(uri, entity, String.class);
		return "/merchant/accountCenter/addElectricMenu";
	}

	@RequestMapping(value = "/electricMenu/addItem", method = RequestMethod.GET)
	public String toAddElectricMenuItem(Model model) throws Exception {
		String uri = ConfigParams.getBaseUrl() + "merchant/electricMenu/list/" + LoginUtils.getLoginMerchant().getId() + "/";
		ElectricMenu[] electricMenus = restTemplate.getForObject(uri, ElectricMenu[].class);
		model.addAttribute("electricMenus", electricMenus);
		return "/merchant/accountCenter/addElectricMenu";
	}

	@RequestMapping(value = "/electricMenu/updateItem/{menuId}/{title}", method = RequestMethod.GET)
	public String toUpdateElectricMenuItem(@PathVariable String menuId, @PathVariable String title, Boolean detail, Model model) throws Exception {
		String uri = ConfigParams.getBaseUrl() + "merchant/electricMenu/findItem/" + menuId + "/" + title + "/";
		ElectricMenuItem electricMenuItem = restTemplate.getForObject(uri, ElectricMenuItem.class);
		String menusUri = ConfigParams.getBaseUrl() + "merchant/electricMenu/list/" + LoginUtils.getLoginMerchant().getId() + "/";
		ElectricMenu[] electricMenus = restTemplate.getForObject(menusUri, ElectricMenu[].class);
		model.addAttribute("electricMenuItem", electricMenuItem);
		model.addAttribute("electricMenus", electricMenus);
		model.addAttribute("menuId", menuId);
		model.addAttribute("update", true);
		model.addAttribute("detail", detail);
		return "/merchant/accountCenter/addElectricMenu";
	}

	@RequestMapping(value = "/electricMenu/updateItem/{menuId}/{itemId}", method = RequestMethod.POST)
	public String updateElectricMenuItem(@PathVariable String menuId, @PathVariable String itemId, String price, String title, String unit,
			MultipartFile Filedata, Model model) throws Exception {
		File fileToCopy = new File("/temp/file/" + new Date().getTime()
				+ Filedata.getOriginalFilename().substring(Filedata.getOriginalFilename().lastIndexOf("."), Filedata.getOriginalFilename().length()));
		FileUtils.copyInputStreamToFile(Filedata.getInputStream(), fileToCopy);
		Resource resource = new FileSystemResource(fileToCopy);
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		formData.add("file", resource);
		formData.add("title", title);
		formData.add("unit", unit);
		formData.add("price", String.valueOf(price));
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, requestHeaders);
		String uri = ConfigParams.getBaseUrl() + "merchant/electricMenu/updateItem/" + menuId + "/" + itemId + "/";
		restTemplate.postForObject(uri, requestEntity, String.class);
		String menuUri = ConfigParams.getBaseUrl() + "merchant/electricMenu/list/" + LoginUtils.getLoginMerchant().getId() + "/";
		ElectricMenu[] electricMenus = restTemplate.getForObject(menuUri, ElectricMenu[].class);
		model.addAttribute("menus", electricMenus);
		return "/merchant/accountCenter/electricMenu";
	}

	@RequestMapping(value = "/electricMenu/updateItemNoPic/{menuId}/{itemId}", method = RequestMethod.POST)
	@ResponseBody
	public String updateElectricMenuItemWidthoutPic(@PathVariable String menuId, @PathVariable String itemId, String price, String title,
			String unit, Model model) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("title", title);
		map.put("unit", unit);
		map.put("price", String.valueOf(price));
		String uri = ConfigParams.getBaseUrl() + "merchant/electricMenu/updateItemNoPic/" + menuId + "/" + itemId
				+ "/?title={title}&unit={unit}&price={price}";
		restTemplate.postForObject(uri, null, String.class, map);
		return "success";
	}

	@RequestMapping(value = "/electricMenu/deleteItem/{itemId}/{menuId}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteElectricMenuItem(@PathVariable String itemId, @PathVariable String menuId, Model model) throws Exception {
		String uri = ConfigParams.getBaseUrl() + "merchant/electricMenu/deleteItem/" + menuId + "/" + itemId + "/";
		String result = restTemplate.getForObject(uri, String.class);
		return result;
	}

	@RequestMapping(value = "/electricMenu/delete/{menuId}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteMenu(@PathVariable String menuId, Model model) throws Exception {
		String uri = ConfigParams.getBaseUrl() + "merchant/electricMenu/delete/" + menuId + "/";
		String result = restTemplate.getForObject(uri, String.class);
		return result;
	}

	@RequestMapping(value = "/electricMenu/addItem", method = RequestMethod.POST)
	public String addElectricMenuItem(String menuId, String title, double price, String unit, @RequestParam("Filedata") MultipartFile Filedata,
			Model model) throws Exception {
		File fileToCopy = new File("/temp/file/" + new Date().getTime()
				+ Filedata.getOriginalFilename().substring(Filedata.getOriginalFilename().lastIndexOf("."), Filedata.getOriginalFilename().length()));
		FileUtils.copyInputStreamToFile(Filedata.getInputStream(), fileToCopy);
		Resource resource = new FileSystemResource(fileToCopy);
		MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
		formData.add("file", resource);
		formData.add("title", title);
		formData.add("unit", unit);
		formData.add("price", String.valueOf(price));
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, requestHeaders);
		String uri = ConfigParams.getBaseUrl() + "merchant/electricMenu/addItem/" + menuId + "/";
		String result = restTemplate.postForObject(uri, requestEntity, String.class);
		model.addAttribute("result", result);
		String menuUri = ConfigParams.getBaseUrl() + "merchant/electricMenu/list/" + LoginUtils.getLoginMerchant().getId() + "/";
		ElectricMenu[] electricMenus = restTemplate.getForObject(menuUri, ElectricMenu[].class);
		model.addAttribute("menus", electricMenus);
		return "/merchant/accountCenter/electricMenu";
	}

	@RequestMapping(value = "/electricMenu/sync", method = RequestMethod.GET)
	public String sync() throws Exception {
		String uri = ConfigParams.getBaseUrl() + "merchant/electricMenu/sync/" + LoginUtils.getLoginMerchant().getId() + "/";
		restTemplate.getForObject(uri, String.class);
		return "/merchant/accountCenter/electricMenu";
	}

	@RequestMapping(value = "/titleValidate", method = RequestMethod.GET)
	@ResponseBody
	public Boolean titleValidate(String title, String oldTitle, String menuId) {
		if (StringUtils.isNotEmpty(title) && title.equals(oldTitle)) {
			return true;
		}
		String uri = ConfigParams.getBaseUrl() + "merchant/electricMenu/titleValidate/" + menuId + "/" + title + "/";
		Boolean result = restTemplate.getForObject(uri, Boolean.class);
		return result;
	}

}
