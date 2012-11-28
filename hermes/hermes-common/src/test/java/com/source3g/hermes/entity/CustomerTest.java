package com.source3g.hermes.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;

import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.Remind;
import com.source3g.hermes.enums.Sex;

public class CustomerTest {

	@Test
	public void toSqlite() {
		Customer customer = new Customer();
		customer.setAddress("北京市");
		customer.setBirthday("80-15");
		customer.setBlackList(false);
		customer.setCustomerGroupId(new ObjectId("50b2fb5ed36861aa96e9f1f4"));
		customer.setEmail("123@123.com");
		customer.setId(new ObjectId("50b2fb5ed36861aa96e9f1f4"));
		customer.setLastCallInTime(new Date());
		customer.setMerchantId(new ObjectId("50b2fb5ed36861aa96e9f1f4"));
		customer.setName("张三");
		customer.setNote("爱吃红烧肉");
		customer.setPhone("13644058759");
		customer.setQq("123456");
		customer.setSex(Sex.MALE);

		Remind remind = new Remind();
		remind.setAdvancedTime("1");
		remind.setAlreadyRemind(true);
		remind.setName("红酒到期");
		remind.setRemindTime(new Date());

		Remind remind1 = new Remind();
		remind1.setAdvancedTime("1");
		remind1.setAlreadyRemind(true);
		remind1.setName("红酒到期");
		remind1.setRemindTime(new Date());

		List<Remind> reminds = new ArrayList<Remind>();
		reminds.add(remind);
		reminds.add(remind);
		customer.setReminds(reminds);
		try {
			System.out.println(customer.toInsertOrUpdateSql());
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
