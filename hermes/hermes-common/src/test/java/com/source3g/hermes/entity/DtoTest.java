package com.source3g.hermes.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.source3g.hermes.entity.customer.Remind;

public class DtoTest {
	public static void main(String[] args) {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setAddress("北京朝阳");
		customerDto.setBirthday("2012-12-15 10:10:15");
		customerDto.setBlackList(false);
		customerDto.setEmail("123@163.com");
		customerDto.setName("张三");
		customerDto.setPhone("12057707697");
		customerDto.setQq("303844828");
		customerDto.setSex("男");

		List<Remind> reminds = new ArrayList<Remind>();
		Remind remind = new Remind();
		remind.setAdvancedTime("1");
		remind.setAlreadyRemind(false);
		remind.setName("红酒到期");
		remind.setRemindTime(new Date());
		reminds.add(remind);
		customerDto.setReminds(reminds);
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println("123");
		try {
			System.out.println(objectMapper.writeValueAsString(customerDto));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
