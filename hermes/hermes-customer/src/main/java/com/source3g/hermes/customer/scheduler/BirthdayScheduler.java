package com.source3g.hermes.customer.scheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.source3g.hermes.customer.service.CustomerService;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.Remind;
import com.source3g.hermes.entity.merchant.Merchant;

@Component
public class BirthdayScheduler {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private MongoTemplate mongoTemplate;

	public void addBirthdayRemind() {
		List<Merchant> merchants = mongoTemplate.find(new Query(Criteria.where("canceled").is(false)), Merchant.class);
		for (Merchant merchant : merchants) {
			if (!merchant.getSetting().isBirthdayRemind()) {
				continue;
			}

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, merchant.getSetting().getBirthdayRemindAdvancedTime());

			String birthday = calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

			List<Customer> customersTodayBirthday = customerService.findCustomersByBirthday(merchant.getId(), birthday);
			Remind remind = new Remind();
			remind.setAlreadyRemind(false);
			remind.setMerchantRemindTemplate(merchant.getSetting().getBirthdayRemindTemplate());
			remind.setRemindTime(new Date(calendar.getTimeInMillis()));
			for (Customer c : customersTodayBirthday) {
				Update update = new Update();
				update.addToSet("reminds", remind);
				mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(c.getId())), update, Customer.class);
			}
		}

	}
}
