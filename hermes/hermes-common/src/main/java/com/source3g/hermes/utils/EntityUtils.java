package com.source3g.hermes.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.source3g.hermes.dto.customer.CustomerDto;
import com.source3g.hermes.dto.customer.RemindDto;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.Remind;

public class EntityUtils {

	public static void copyCustomerEntityToDto(Customer customer,
			CustomerDto customerDto) {
		if (customer == null || customerDto == null) {
			return;
		}
		customerDto.setAddress(customer.getAddress());
		customerDto.setBirthday(customer.getBirthday());
		customerDto.setBlackList(customer.isBlackList());
		customerDto.setCallRecords(customer.getCallRecords());
		if (customer.getCustomerGroup() != null) {
			customerDto.setGroupId(customer.getCustomerGroup().getId());
		}
		customerDto.setFavorite(customer.getFavorite());
		customerDto.setEmail(customer.getEmail());
		customerDto.setLastCallInTime(customer.getLastCallInTime());
		customerDto.setName(customer.getName());
		customerDto.setNote(customer.getNote());
		customerDto.setOperateTime(customer.getOperateTime());
		customerDto.setPhone(customer.getPhone());
		customerDto.setQq(customer.getQq());
		customerDto.setSex(customer.getSex());
		List<RemindDto> reminds = new ArrayList<RemindDto>();
		if (customer.getReminds() != null) {
			for (Remind r : customer.getReminds()) {
				RemindDto remindDto = new RemindDto();
				remindDto.setAdvancedTime(String.valueOf(r
						.getMerchantRemindTemplate().getAdvancedTime()));
				remindDto.setAlreadyRemind(r.isAlreadyRemind());
				remindDto.setRemindTime(r.getRemindTime());
				if (r.getMerchantRemindTemplate() != null
						&& r.getMerchantRemindTemplate().getRemindTemplate() != null
						&& StringUtils.isNotEmpty(r.getMerchantRemindTemplate()
								.getRemindTemplate().getTitle())) {
					remindDto.setName(r.getMerchantRemindTemplate()
							.getRemindTemplate().getTitle());
				}
				reminds.add(remindDto);
			}
		}
		customerDto.setReminds(reminds);
	}
}
