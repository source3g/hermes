package com.source3g.wcb.merchant.service;

import java.util.List;

import com.source3g.wcb.entity.merchant.Merchant;

public interface MerchantService {
	
	public void add(Merchant merchant);

	public  List<Merchant> list();

	public void deleteById(String id);
public Merchant getMerchant(String id);
	
	public void update(Merchant merchant);
	
}
