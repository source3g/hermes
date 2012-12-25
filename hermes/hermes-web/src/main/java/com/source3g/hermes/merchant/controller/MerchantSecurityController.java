package com.source3g.hermes.merchant.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.source3g.hermes.admin.security.ShiroDbRealm.ShiroUser;
import com.source3g.hermes.entity.Device;
import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.enums.TypeEnum.LoginType;
import com.source3g.hermes.utils.ConfigParams;

@Controller
public class MerchantSecurityController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login() {
		return new ModelAndView("/merchant/login");
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(String username, String password, boolean rememberMe) {
		Subject currentUser = SecurityUtils.getSubject();
		boolean result = false;
		if (!currentUser.isAuthenticated()) {
			result = login(currentUser, username, password, rememberMe);
		} else {// 重复登录
			ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipal();
			if (shiroUser.getMerchant() != null && shiroUser.getMerchant().getAccount().equalsIgnoreCase(username))// 如果登录名不同
				currentUser.logout();
			result = login(currentUser, username, password, rememberMe);
		}
		if (result) {
			return new ModelAndView("redirect:/index/");
		} else {
			return new ModelAndView("redirect:/login");
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout() {
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		return new ModelAndView("redirect:/login");
	}

	private boolean login(Subject currentUser, String username, String password, boolean rememberMe) {
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		token.setRememberMe(rememberMe);
		try {
			currentUser.getSession().setAttribute("type", LoginType.merchant);
			currentUser.login(token);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@RequiresRoles("merchant")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String toMerchant(HttpServletRequest request) {
		// initSession(request);
		return "merchant/index";
	}

	@SuppressWarnings("unused")
	private void initSession(HttpServletRequest request) {
		String id = "50a1b46286c3d8c834f49723";
		String uri = ConfigParams.getBaseUrl() + "merchant/" + id + "/";
		Merchant merchant = restTemplate.getForObject(uri, Merchant.class);

		if (merchant == null) {
			String deviceSn = "001";
			String deviceBySnUri = ConfigParams.getBaseUrl() + "/device/sn/" + deviceSn + "/";
			Device device = restTemplate.getForObject(deviceBySnUri, Device.class);
			if (device == null) {
				device = new Device();
				device.setId(new ObjectId("50a1b46286c3d8c834f49725"));
				device.setSn(deviceSn);
				String addDeviceUri = ConfigParams.getBaseUrl() + "device/add/";
				HttpEntity<Device> entity = new HttpEntity<>(device);
				restTemplate.postForObject(addDeviceUri, entity, String.class);
			}
			merchant = new Merchant();
			List<ObjectId> deviceIds = new ArrayList<ObjectId>();
			deviceIds.add(device.getId());
			merchant.setDeviceIds(deviceIds);
			merchant.setId(new ObjectId("50a1b46286c3d8c834f49723"));
			merchant.setName("安联");
			merchant.setAddr("呼家楼");
			String uriAdd = ConfigParams.getBaseUrl() + "merchant/add/";
			HttpEntity<Merchant> httpEntity = new HttpEntity<>(merchant);
			restTemplate.postForObject(uriAdd, httpEntity, String.class);

			CustomerGroup customerGroup = new CustomerGroup();
			customerGroup.setMerchantId(merchant.getId());
			customerGroup.setName("普通顾客");
			String addCustomerUri = ConfigParams.getBaseUrl() + "customerGroup/add/";
			HttpEntity<CustomerGroup> groupEntity = new HttpEntity<CustomerGroup>(customerGroup);
			restTemplate.postForObject(addCustomerUri, groupEntity, String.class);
		}

		WebUtils.setSessionAttribute(request, "loginUser", merchant);
	}

}
