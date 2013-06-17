package com.source3g.hermes.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.source3g.hermes.dto.customer.CustomerDto;
import com.source3g.hermes.dto.customer.RemindDto;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.CustomerGroup;
import com.source3g.hermes.entity.customer.Remind;

public class EntityUtils {

	public static void copyCustomerEntityToDto(Customer customer, CustomerDto customerDto) {
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
		if (!CollectionUtils.isEmpty(customer.getCallRecords())) {
			customerDto.setLastCallInDuration(customer.getCallRecords().get(customer.getCallRecords().size() - 1).getCallDuration());
		}
		List<RemindDto> reminds = new ArrayList<RemindDto>();
		if (customer.getReminds() != null) {
			for (Remind r : customer.getReminds()) {
				RemindDto remindDto = new RemindDto();
				remindDto.setAdvancedTime(String.valueOf(r.getMerchantRemindTemplate().getAdvancedTime()));
				remindDto.setAlreadyRemind(r.isAlreadyRemind());
				remindDto.setRemindTime(r.getRemindTime());
				if (r.getMerchantRemindTemplate() != null && r.getMerchantRemindTemplate().getRemindTemplate() != null
						&& StringUtils.isNotEmpty(r.getMerchantRemindTemplate().getRemindTemplate().getTitle())) {
					remindDto.setName(r.getMerchantRemindTemplate().getRemindTemplate().getTitle());
				}
				reminds.add(remindDto);
			}
		}
		customerDto.setReminds(reminds);
	}

	public static void copyCustomerDtoToEntity(CustomerDto customerDto, Customer customer, ObjectId merchantId) {
		if (customer == null || customerDto == null) {
			return;
		}
		customer.setMerchantId(merchantId);
		customer.setAddress(customerDto.getAddress());
		customer.setBirthday(customerDto.getBirthday());
		customer.setBlackList(customerDto.isBlackList());
		if (customerDto.getGroupId() != null) {
			CustomerGroup customerGroup = new CustomerGroup();
			customerGroup.setId(customerDto.getGroupId());
			customer.setCustomerGroup(customerGroup);
		}
		try {
			customer.setFavorite(Boolean.parseBoolean(customerDto.getFavorite()));
		} catch (Exception e) {
			customer.setFavorite(false);
		}
		customer.setEmail(customerDto.getEmail());
		customer.setName(customerDto.getName());
		customer.setNote(customerDto.getNote());
		customer.setPhone(customerDto.getPhone());
		customer.setQq(customerDto.getQq());
		customer.setSex(customerDto.getSex());
	}
}
