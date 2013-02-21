package com.source3g.hermes.merchant.service;

import java.util.List;

import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.merchant.MessageChargeLog;
import com.source3g.hermes.utils.Page;

public interface MerchantServiceIntf {
	


	public  List<Merchant> list();

	public void deleteById(String id);
	
	public Merchant getMerchant(String id);
	
	public void update(Merchant merchant);
	
	public Page list(int pageNo,Merchant merchant);

	public  List<Merchant>  findByDeviceIds(String ids);

	public List<Merchant> findByGroupId(String id);

	public void chargeMsg(String id, int count);

	public void addMsgLog(MessageChargeLog messageLog);
	
}
