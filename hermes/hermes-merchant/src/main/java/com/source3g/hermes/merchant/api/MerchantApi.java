package com.source3g.hermes.merchant.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.source3g.hermes.entity.dictionary.MerchantTagNode;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.merchant.MerchantRemindTemplate;
import com.source3g.hermes.entity.merchant.MerchantResource;
import com.source3g.hermes.entity.merchant.RemindTemplate;
import com.source3g.hermes.entity.merchant.Setting;
import com.source3g.hermes.merchant.service.MerchantService;
import com.source3g.hermes.service.CommonBaseService;
import com.source3g.hermes.utils.Page;

@Controller
@RequestMapping("/merchant")
public class MerchantApi {

	private Logger logger = LoggerFactory.getLogger(MerchantApi.class);

	@Autowired
	private MerchantService merchantService;
	@Autowired
	private CommonBaseService commonBaseService;

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
			handleMerchantTags(merchant);
			merchantService.add(merchant);
		} catch (Exception e) {
			return e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/sn/{sn}", method = RequestMethod.GET)
	@ResponseBody
	public Merchant getBySn(@PathVariable String sn) throws Exception {
		return commonBaseService.findMerchantByDeviceSn(sn);
	}

	@RequestMapping(value = "/accountValidate/{account}", method = RequestMethod.GET)
	@ResponseBody
	public boolean accountValidate(@PathVariable String account) {
		return merchantService.accountValidate(account);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Merchant getMerchant(@PathVariable String id) {
		return merchantService.getMerchant(id);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Page list(String pageNo, String name,String account) {
		logger.debug("list merchant....");
		int pageNoInt = Integer.valueOf(pageNo);
		Merchant merchant = new Merchant();
		merchant.setName(name);
		merchant.setAccount(account);
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
		handleMerchantTags(merchant);
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
		return merchantService.msgLogList(new ObjectId(merchantId), pageNoInt);
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

	@RequestMapping(value = "/switch/{merchantId}", method = RequestMethod.POST)
	@ResponseBody
	public String saveSwitch(@PathVariable String merchantId, @RequestBody Setting setting) {
		merchantService.saveSwitch(new ObjectId(merchantId), setting);
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

	@RequestMapping(value = "/merchantRemindTemplateList/sn/{sn}", method = RequestMethod.GET)
	@ResponseBody
	public List<MerchantRemindTemplate> merchantRemindTemplateListBySn(@PathVariable String sn) throws Exception {
		Merchant merchant = commonBaseService.findMerchantByDeviceSn(sn);
		return merchantService.merchantRemindList(merchant.getId());
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
	public String passwordChange(@PathVariable String password, @PathVariable String newPassword, @PathVariable String merchantId) {
		try {
			merchantService.passwordChange(password, newPassword, new ObjectId(merchantId));
		} catch (Exception e) {
			return e.getMessage();
		}
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/passwordValidate/{password}/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public Boolean passwordValidate( @PathVariable String password,@PathVariable String merchantId) {
		ObjectId obj=new ObjectId(merchantId);
		return	merchantService.passwordValidate(password,obj);
	}
	
	@RequestMapping(value = "/addMerchantResource/{merchantId}/{name}", method = RequestMethod.GET)
	@ResponseBody
	public String addMerchantResource(@PathVariable ObjectId merchantId, @PathVariable String name) throws Exception {
		merchantService.addMerchantResource(merchantId, name);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/deletemerchantResource/{merchantId}/{name}", method = RequestMethod.GET)
	@ResponseBody
	public String deletemerchantResource(@PathVariable ObjectId merchantId, @PathVariable String name) {
		merchantService.deletemerchantResource(merchantId, name);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/updateMerchantResource/{merchantId}/", method = RequestMethod.POST)
	@ResponseBody
	public String updateMerchantResource(@RequestBody String messageContent, @PathVariable ObjectId merchantId) {
		merchantService.updateMerchantResource(messageContent, merchantId);
		return ReturnConstants.SUCCESS;
	}

	@RequestMapping(value = "/merchantResource/{merchantId}", method = RequestMethod.GET)
	@ResponseBody
	public MerchantResource getMerchantResource(@PathVariable ObjectId merchantId) {
		return merchantService.getMerchantResource(merchantId);
	}

	@RequestMapping(value = "/merchantResource/sn/{sn}", method = RequestMethod.GET)
	@ResponseBody
	public MerchantResource getMerchantResource(@PathVariable String sn) throws Exception {
		Merchant merchant = commonBaseService.findMerchantByDeviceSn(sn);
		MerchantResource merchantResource = merchantService.getMerchantResource(merchant.getId());
		if(merchantResource.getResourceList()!=null){
			Collections.sort(merchantResource.getResourceList());
		}
		return merchantResource;
	}

	private void handleMerchantTags(Merchant merchant) {
		if (merchant == null || merchant.getMerchantTagNodes() == null) {
			return;
		}
		List<MerchantTagNode> merchantTagNodes = merchant.getMerchantTagNodes();
		// 去掉空的id
		for (int i = merchantTagNodes.size() - 1; i >= 0; i--) {
			if (merchantTagNodes.get(i).getId() == null) {
				merchantTagNodes.remove(i);
			}
		}
		// 取重复
		Set<MerchantTagNode> set = new HashSet<MerchantTagNode>(merchantTagNodes);
		merchantTagNodes = new ArrayList<MerchantTagNode>();
		merchantTagNodes.addAll(set);
		merchant.setMerchantTagNodes(merchantTagNodes);
	}

}
