package com.source3g.hermes.merchant.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.source3g.hermes.admin.security.ShiroDbRealm.ShiroUser;
import com.source3g.hermes.enums.TypeEnum.LoginType;

@Controller
public class MerchantSecurityController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login() {
		return new ModelAndView("/merchant/login");
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(String username, String password,
			boolean rememberMe) {
		Subject currentUser = SecurityUtils.getSubject();
		boolean result = false;
		if (!currentUser.isAuthenticated()) {
			result = login(currentUser, username, password, rememberMe);
		} else {// 重复登录
			ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipal();
			if (shiroUser.getMerchant() != null
					&& shiroUser.getMerchant().getAccount()
							.equalsIgnoreCase(username))// 如果登录名不同
				currentUser.logout();
			result = login(currentUser, username, password, rememberMe);
		}
		if (result) {
			return new ModelAndView("redirect:/index/");
		} else {
			return new ModelAndView("redirect:/login");
		}
	}

	@RequestMapping(value = "/login/json", method = RequestMethod.POST)
	@ResponseBody
	public String loginJson(String username, String password, boolean rememberMe) {
		Subject currentUser = SecurityUtils.getSubject();
		boolean result = false;
		if (!currentUser.isAuthenticated()) {
			result = login(currentUser, username, password, rememberMe);
		} else {// 重复登录
			ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipal();
			if (shiroUser.getMerchant() != null
					&& shiroUser.getMerchant().getAccount()
							.equalsIgnoreCase(username))// 如果登录名不同
				currentUser.logout();
			result = login(currentUser, username, password, rememberMe);
		}
		return String.valueOf(result);
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout() {
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		return new ModelAndView("redirect:/login");
	}

	private boolean login(Subject currentUser, String username,
			String password, boolean rememberMe) {
		UsernamePasswordToken token = new UsernamePasswordToken(username,
				password);
		token.setRememberMe(rememberMe);
		try {
			currentUser.getSession().setAttribute("loginType",
					LoginType.merchant);
			currentUser.login(token);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@RequiresRoles("merchant")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String toMerchant(HttpServletRequest request) throws Exception {
		// initSession(request);
		//${pageContext.request.contextPath}/merchant/main/
		return "redirect:/merchant/main/";
	}

	@RequestMapping(value = "/api/customer/chart/line", method = RequestMethod.GET)
	public String toLine(String username, String password) {
		Subject currentUser = SecurityUtils.getSubject();
		boolean result = false;
		if (!currentUser.isAuthenticated()) {
			result = login(currentUser, username, password, true);
		} else {// 重复登录
			ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipal();
			if (shiroUser.getMerchant() != null&& shiroUser.getMerchant().getAccount().equalsIgnoreCase(username))// 如果登录名不同
				currentUser.logout();
			result = login(currentUser, username, password, true);
		}
		if (result) {
			return "/merchant/chart/lineChartApi";
		} else {
			return "redirect:/login";
		}
	}

	@RequestMapping(value = "/api/customer/chart/pie", method = RequestMethod.GET)
	public String toPie(String username, String password) {
		Subject currentUser = SecurityUtils.getSubject();
		boolean result = false;
		if (!currentUser.isAuthenticated()) {
			result = login(currentUser, username, password, true);
		} else {// 重复登录
			ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipal();
			if (shiroUser.getMerchant() != null&& shiroUser.getMerchant().getAccount().equalsIgnoreCase(username))// 如果登录名不同
				currentUser.logout();
			result = login(currentUser, username, password, true);
		}
		if (result) {
			return "/merchant/chart/callInPieChartApi";
		} else {
			return "redirect:/login";
		}
	}

	/*
	 * private void initSession(HttpServletRequest request) { String id =
	 * "50a1b46286c3d8c834f49723"; String uri = ConfigParams.getBaseUrl() +
	 * "merchant/" + id + "/"; Merchant merchant =
	 * restTemplate.getForObject(uri, Merchant.class);
	 * 
	 * if (merchant == null) { String deviceSn = "001"; String deviceBySnUri =
	 * ConfigParams.getBaseUrl() + "/device/sn/" + deviceSn + "/"; Device device
	 * = restTemplate.getForObject(deviceBySnUri, Device.class); if (device ==
	 * null) { device = new Device(); device.setId(new
	 * ObjectId("50a1b46286c3d8c834f49725")); device.setSn(deviceSn); String
	 * addDeviceUri = ConfigParams.getBaseUrl() + "device/add/";
	 * HttpEntity<Device> entity = new HttpEntity<>(device);
	 * restTemplate.postForObject(addDeviceUri, entity, String.class); }
	 * merchant = new Merchant(); List<ObjectId> deviceIds = new
	 * ArrayList<ObjectId>(); deviceIds.add(device.getId());
	 * merchant.setDeviceIds(deviceIds); merchant.setId(new
	 * ObjectId("50a1b46286c3d8c834f49723")); merchant.setName("安联");
	 * merchant.setAddr("呼家楼"); String uriAdd = ConfigParams.getBaseUrl() +
	 * "merchant/add/"; HttpEntity<Merchant> httpEntity = new
	 * HttpEntity<>(merchant); restTemplate.postForObject(uriAdd, httpEntity,
	 * String.class);
	 * 
	 * CustomerGroup customerGroup = new CustomerGroup();
	 * customerGroup.setMerchantId(merchant.getId());
	 * customerGroup.setName("普通顾客"); String addCustomerUri =
	 * ConfigParams.getBaseUrl() + "customerGroup/add/";
	 * HttpEntity<CustomerGroup> groupEntity = new
	 * HttpEntity<CustomerGroup>(customerGroup);
	 * restTemplate.postForObject(addCustomerUri, groupEntity, String.class); }
	 * 
	 * WebUtils.setSessionAttribute(request, "loginUser", merchant); }
	 */

}
