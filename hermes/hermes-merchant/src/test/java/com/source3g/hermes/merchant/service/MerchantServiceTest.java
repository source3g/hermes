package com.source3g.hermes.merchant.service;

import org.junit.Test;

import com.source3g.hermes.entity.merchant.Merchant;

public class MerchantServiceTest {
	
	
	@Test
	public void delete(){
		StringBuffer deviceIds=new StringBuffer();
		String a[] ={"aa","bb","cc"};
		for(String deviceId:a){
			deviceIds.append(deviceId);
			deviceIds.append(",");
		}
		System.out.println(deviceIds.length()-1);
		System.out.println(deviceIds.length());
		deviceIds.delete(deviceIds.length()-1, deviceIds.length());

		System.out.println(deviceIds.toString());
	}

}
