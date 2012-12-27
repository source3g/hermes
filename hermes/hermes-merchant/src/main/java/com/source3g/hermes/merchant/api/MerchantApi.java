package com.source3g.hermes.merchant.api;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.source3g.hermes.constants.ReturnConstants;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.merchant.MerchantRemindTemplate;
import com.source3g.hermes.entity.merchant.RemindTemplate;
import com.source3g.hermes.merchant.service.MerchantService;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/merchant")
public class MerchantApi {

	private Logger logger = LoggerFactory.getLogger(MerchantApi.class);

	@Autowired
	private MerchantService merchantService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Merchant login(String username, String password) {
		return merchantService.login(username, password);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody Merchant merchant) {
		logger.debug("add merchant....");
		try {
			merchantService.add(merchant);
		} catch (Exception e) {
			return e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/accountValidate/{account}", method = RequestMethod.GET)
	@ResponseBody
	public boolean add(@PathVariable String account) {
		return merchantService.accountValidate(account);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Merchant getMerchant(@PathVariable String id) {
		return merchantService.getMerchant(id);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Page list(String pageNo, String name) {
		logger.debug("list merchant....");
		int pageNoInt = Integer.valueOf(pageNo);
		Merchant merchant = new Merchant();
		merchant.setName(name);
		return merchantService.list(pageNoInt, merchant);
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String cancel(@PathVariable String id) {
		logger.debug("cancel merchant....");
		merchantService.cancel(new ObjectId(id));
		return ReturnConstants.SUCCESS;
	}
	
	@RequestMapping(value = "/recover/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String recover(@PathVariable String id) {
		logger.debug("cancel merchant....");
		merchantService.recover(new ObjectId(id));
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public String update(@RequestBody Merchant merchant) {
		logger.debug("update merchant....");
		merchantService.updateInfo(merchant);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/findByDeviceIds/{ids}", method = RequestMethod.GET)
	@ResponseBody
	public List<Merchant> findByDeviceIds(@PathVariable String ids) {
		logger.debug("find merchant....");
		return merchantService.findByDeviceIds(ids);
	}

	@RequestMapping(value = "/findByGroupId/{id}", method = RequestMethod.GET)
	@ResponseBody
	public List<Merchant> findByGroupId(@PathVariable String id) {
		logger.debug("find merchants....");
		return merchantService.findByGroupId(id);
	}

	@RequestMapping(value = "/chargeMsg/{id}", method = RequestMethod.POST)
	@ResponseBody
	public String chargeMsg(String count, String type, @PathVariable String id) {
		int countInt = Integer.parseInt(count);
		merchantService.chargeMsg(id, countInt);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/msgLogList", method = RequestMethod.GET)
	@ResponseBody
	public Page msgLogList(String pageNo, String merchantId) {
		int pageNoInt = Integer.valueOf(pageNo);
		return merchantService.msgLogList(new ObjectId(merchantId),pageNoInt);
	}

	@RequestMapping(value = "/UpdateQuota/{id}", method = RequestMethod.POST)
	@ResponseBody
	public String UpdateQuota(@PathVariable String id, String count, String type) {
		int countInt = Integer.parseInt(count);
		if (type.equals("cut")) {
			countInt = 0 - countInt;
		}
		merchantService.UpdateQuota(id, countInt);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/switch", method = RequestMethod.POST)
	@ResponseBody
	public String Switch(@RequestBody Merchant merchant) {
		merchantService.Switch(merchant);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/remindAdd/{merchantId}/{templateId}", method = RequestMethod.GET)
	@ResponseBody
	public List<MerchantRemindTemplate> remindAdd(@PathVariable String merchantId, @PathVariable String templateId) {
		merchantService.remindAdd(new ObjectId(merchantId), new ObjectId(templateId));
		return merchantService.merchantRemindList(new ObjectId(merchantId));
	}

	@RequestMapping(value = "/merchantRemindList/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public List<MerchantRemindTemplate> merchantRemindList(@PathVariable String merchantId) {
		return merchantService.merchantRemindList(new ObjectId(merchantId));
	}

	@RequestMapping(value = "/remindSetting", method = RequestMethod.GET)
	@ResponseBody
	public List<RemindTemplate> remindSetting() {
		return merchantService.remindList();
	}

	@RequestMapping(value = "/remindSave/{merchantId}", method = RequestMethod.POST)
	@ResponseBody
	public String remindSave(@PathVariable String merchantId, @RequestBody MerchantRemindTemplate merchantRemindTemplate) {
		merchantService.remindSave(new ObjectId(merchantId), merchantRemindTemplate);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/remindDelete/{merchantId}/{templateId}", method = RequestMethod.GET)
	@ResponseBody
	public List<MerchantRemindTemplate> remindDelete(@PathVariable String merchantId, @PathVariable String templateId) {
		merchantService.remindDelete(new ObjectId(merchantId), new ObjectId(templateId));
		return merchantService.merchantRemindList(new ObjectId(merchantId));
	}
	
	@RequestMapping(value = "/passwordChange/{password}/{newPassword}/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public String passwordChange(@PathVariable String password, @PathVariable String newPassword,@PathVariable String merchantId) {
		try {
			merchantService.passwordChange(password, newPassword,new ObjectId(merchantId));
		} catch (Exception e) {
			return e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}
}
